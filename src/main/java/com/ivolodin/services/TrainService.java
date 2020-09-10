package com.ivolodin.services;

import com.ivolodin.dto.StationDto;
import com.ivolodin.dto.TrainDto;
import com.ivolodin.dto.TrainEdgeDto;
import com.ivolodin.entities.Station;
import com.ivolodin.entities.Train;
import com.ivolodin.entities.TrainEdge;
import com.ivolodin.exceptions.PathNotExistException;
import com.ivolodin.repositories.StationRepository;
import com.ivolodin.repositories.TrainEdgeRepository;
import com.ivolodin.repositories.TrainRepository;
import com.ivolodin.utils.MapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Transactional
@Service
public class TrainService {

    private TrainRepository trainRepository;

    private StationRepository stationRepository;

    private GraphService graphService;

    private EdgeService edgeService;

    private TrainEdgeRepository trainEdgeRepository;

    private AmqpTemplate template;

    /**
     * @return list of all trains
     */
    public List<TrainDto> getAllTrains() {
        List<Train> all = trainRepository.findAll();
        return MapperUtils.mapAll(all, TrainDto.class);
    }

    /**
     * Creates new train from given data. Creates path
     *
     * @param trainDto containing trainName, departing time, total seats, from and to stations
     * @return TrainDto containing information about resulting train
     */
    public TrainDto addNewTrain(TrainDto trainDto) {
        Station fromStation = stationRepository.findByName(trainDto.getFromStation());
        Station toStation = stationRepository.findByName(trainDto.getToStation());

        if (fromStation == null)
            throw new EntityNotFoundException("Station with name{" + trainDto.getFromStation() + "} not found");

        if (toStation == null)
            throw new EntityNotFoundException("Station with name{" + trainDto.getToStation() + "} not found");

        if (fromStation.equals(toStation))
            throw new IllegalArgumentException("Stations must be different");

        if (trainRepository.findTrainByTrainName(trainDto.getTrainName()) != null)
            throw new EntityExistsException("Train with such name already exists");

        if (trainDto.getDeparture().isBefore(LocalDateTime.now()))
            throw new DateTimeException("Train departure can't be in past");

        if (trainDto.getSeatsNumber() == null || trainDto.getSeatsNumber() <= 0)
            throw new IllegalArgumentException("Seats number must be more 0");

        Train train = new Train();
        train.setTrainName(trainDto.getTrainName());
        train.setDeparture(trainDto.getDeparture());
        train.setSeatsNumber(trainDto.getSeatsNumber());
        train.setFromStation(fromStation);
        train.setToStation(toStation);

        List<TrainEdge> path = createPathForTrain(train);

        train.setPath(path);
        train.setArrival(path.get(path.size() - 1).getArrival());
        train = trainRepository.save(train);

        sendRefreshMessageToTableau(train);
        log.info("Train {} added", train.toString());
        return MapperUtils.map(train, TrainDto.class);
    }

    /**
     * Creates path for given train using graph service.
     * Default standing is 0 minutes
     *
     * @param train containing train name, from and to stations, total seats and departing time
     * @return list of TrainEdge
     * @throws PathNotExistException if path does not exist
     */
    private List<TrainEdge> createPathForTrain(Train train) throws PathNotExistException {

        List<Station> stationList = graphService.getPathList(train.getFromStation(), train.getToStation());

        if (stationList.isEmpty()) throw new PathNotExistException("Not path found");

        List<TrainEdge> path = new ArrayList<>(stationList.size());

        TrainEdge firstEdge = new TrainEdge();
        firstEdge.setTrain(train);
        firstEdge.setStation(stationList.get(0));
        firstEdge.setOrder(1);
        firstEdge.setSeatsLeft(train.getSeatsNumber());
        firstEdge.setArrival(null);
        firstEdge.setStandingMinutes(0);
        firstEdge.setDeparture(train.getDeparture());
        path.add(firstEdge);

        for (int i = 1; i < stationList.size(); i++) {
            TrainEdge curEdge = new TrainEdge();
            curEdge.setTrain(train);
            curEdge.setStation(stationList.get(i));
            curEdge.setOrder(i + 1);
            curEdge.setSeatsLeft(train.getSeatsNumber());

            //count arrival
            TrainEdge prevEdge = path.get(i - 1);
            LocalDateTime departureFromPrevStation = prevEdge.getDeparture();
            Integer distance = edgeService.getDistanceBetweenStations(prevEdge.getStation(), curEdge.getStation());
            curEdge.setArrival(departureFromPrevStation.plusMinutes(distance));

            curEdge.setStandingMinutes(0);
            curEdge.setDeparture(curEdge.getArrival());

            path.add(curEdge);
        }

        TrainEdge lastEdge = path.get(path.size() - 1);
        lastEdge.setDeparture(null);
        lastEdge.setStandingMinutes(0);
        return path;
    }

    public TrainDto getTrainInfo(TrainDto trainDto) {
        Train train = trainRepository.findTrainByTrainName(trainDto.getTrainName());
        if (train == null)
            throw new EntityNotFoundException("Train with name " + trainDto.getTrainName() + " not found");

        return MapperUtils.map(train, TrainDto.class);
    }

    /**
     * Sets standings on trainEdge path from given list of trainEdgeDto
     *
     * @param stationUpdates list of TrainEdgeDto containing new standings
     */
    public void updateStandings(List<TrainEdgeDto> stationUpdates) {
        Train train = trainRepository.findTrainByTrainName(stationUpdates.get(0).getTrainName());
        if (train == null)
            throw new EntityNotFoundException("Train with name " + stationUpdates.get(0).getTrainName() + " not found");

        List<TrainEdge> path = train.getPath();
        for (TrainEdgeDto newEdge : stationUpdates) {
            for (TrainEdge oldEdge : path) {
                if (oldEdge.getStation().getName().equals(newEdge.getStationName())) {
                    oldEdge.setStandingMinutes(newEdge.getStanding());
                    break;
                }
            }
        }
        trainEdgeRepository.saveAll(path);
        refreshTrainTimes(train);
    }

    /**
     * Refreshes train times, depending on it's current path and current standings and saves them to database
     *
     * @param train train to update
     */
    public void refreshTrainTimes(Train train) {
        List<TrainEdge> path = train.getPath();

        TrainEdge first = path.get(0);
        TrainEdge second = path.get(1);
        second.setArrival(train.getDeparture()
                .plusMinutes(edgeService.getDistanceBetweenStations(first.getStation(), second.getStation())));

        for (int i = 1; i < path.size() - 1; i++) {
            TrainEdge curEdge = path.get(i);
            TrainEdge nextEdge = path.get(i + 1);
            Integer distance = edgeService.getDistanceBetweenStations(curEdge.getStation(), nextEdge.getStation());
            curEdge.setDeparture(curEdge.getArrival().plusMinutes(curEdge.getStandingMinutes()));
            nextEdge.setArrival(curEdge.getDeparture().plusMinutes(distance));
        }
        train.setArrival(path.get(path.size() - 1).getArrival());
        trainRepository.save(train);
        trainEdgeRepository.saveAll(path);

        sendRefreshMessageToTableau(train);
        log.info("Train {} updated", train.getTrainName());
    }

    /**
     * @param stationName station name
     * @param date        date
     * @return list of TrainDto passing through this station on given date
     */
    public List<TrainDto> getAllTrainsOnStation(String stationName, LocalDate date) {
        if (date == null)
            date = LocalDate.now();
        if (date.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Date is in past");
        if (stationRepository.findByName(stationName) == null)
            throw new EntityNotFoundException("No station found with name: " + stationName);
        List<Train> trains = trainRepository.findTrainsByStationAndDate(stationName, java.sql.Date.valueOf(date));
        return MapperUtils.mapAll(trains, TrainDto.class);
    }

    /**
     * @param station station dto
     * @param date    date
     * @return list of TrainDto departing from given station on given date
     */
    public List<TrainDto> getTrainsDepartingFromStation(StationDto station, LocalDate date) {
        return getTrainsDepartingFromStation(station.getName(), date);
    }

    /**
     * @param stationName station name
     * @param date        date
     * @return list of TrainDto departing from given station on given date
     */
    public List<TrainDto> getTrainsDepartingFromStation(String stationName, LocalDate date) {
        if (date.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Date is in past");
        if (stationRepository.findByName(stationName) == null)
            throw new EntityNotFoundException("No station found with name: " + stationName);
        List<Train> trains = trainRepository.findTrainsDepartingFromStation(stationName, java.sql.Date.valueOf(date));
        return MapperUtils.mapAll(trains, TrainDto.class);

    }

    /**
     * Returns trains passing from one station to another on given date
     *
     * @param from from station name
     * @param to   to station name
     * @param date date
     * @return list of train dto
     */
    public List<TrainDto> getTrainsInDate(String from, String to, LocalDate date) {
        StationDto frSt = new StationDto();
        frSt.setName(from);
        StationDto toSt = new StationDto();
        toSt.setName(to);
        return getTrainsInDate(frSt, toSt, date);

    }

    /**
     * Returns trains passing from one station to another on given date
     *
     * @param from stationDto containing station name
     * @param to   stationDto containing station name
     * @param date date
     * @return list of train dto
     */
    public List<TrainDto> getTrainsInDate(StationDto from, StationDto to, LocalDate date) {
        List<TrainDto> trainsOnFromStation = getTrainsDepartingFromStation(from, date);
        List<TrainDto> foundTrains = new ArrayList<>();
        for (TrainDto train : trainsOnFromStation) {
            boolean foundFromStation = false;
            boolean foundToStation = false;
            for (TrainEdgeDto edge : train.getPath()) {
                if (from.getName().equalsIgnoreCase(edge.getStationName())) {
                    foundFromStation = true;
                }
                if (to.getName().equalsIgnoreCase(edge.getStationName())) {
                    if (foundFromStation) {
                        foundToStation = true;
                    }
                    break;
                }
            }

            if (foundFromStation && foundToStation) {
                foundTrains.add(train);
            }
        }

        return foundTrains;
    }

    /**
     * Returns true if train has available seats on current piece of path
     *
     * @param train    train
     * @param fromEdge from Edge
     * @param toEdge   to Edge
     * @return true if train has available seats on current piece of path
     */
    public Boolean hasAvailableSeatsOnPath(Train train, TrainEdge fromEdge, TrainEdge toEdge) {
        if (!fromEdge.getTrain().equals(train) || !toEdge.getTrain().equals(train))
            return false;
        boolean foundStart = false;
        int minSeats = 0;

        for (TrainEdge edge : train.getPath()) {
            if (edge.equals(fromEdge)) {
                foundStart = true;
                minSeats = edge.getSeatsLeft();
            }
            if (foundStart) {
                minSeats = Math.min(edge.getSeatsLeft(), minSeats);
            }
            if (edge.equals(toEdge)) {
                break;
            }
        }
        if (!foundStart)
            return false;

        return minSeats > 0;
    }

    /**
     * Updates available seats for train between fromEdge and toEdge.
     * If val > 0 method adds available seats, else reduces
     *
     * @param train    the train needed to be updated
     * @param fromEdge Starting edge to update
     * @param toEdge   Ending edge to update
     * @param val      Value to add/reduce to available seats
     */
    public void updateSeatsForTrain(Train train, TrainEdge fromEdge, TrainEdge toEdge, int val) {
        if (!fromEdge.getTrain().equals(train) || !toEdge.getTrain().equals(train))
            return;
        if (val == 0)
            return;
        boolean foundStart = false;

        for (TrainEdge edge : train.getPath()) {
            if (edge.equals(toEdge)) {
                break;
            }
            if (edge.equals(fromEdge)) {
                foundStart = true;
            }
            if (foundStart) {
                edge.setSeatsLeft(edge.getSeatsLeft() + val);
            }

        }
        trainRepository.save(train);
    }

    /**
     * Updates available seats for train between fromStation and toStation.
     * If val > 0 method adds available seats, else reduces
     *
     * @param train       the train needed to be updated
     * @param fromStation Starting edge to update
     * @param toStation   Ending edge to update
     * @param val         Value to add/reduce to available seats
     */
    public void updateSeatsForTrain(Train train, String fromStation, String toStation, int val) {

        TrainEdge frEdge = trainEdgeRepository.getTrainEdgeByStationNameAndTrain(fromStation, train);
        TrainEdge toEdge = trainEdgeRepository.getTrainEdgeByStationNameAndTrain(toStation, train);
        if (frEdge == null || toEdge == null)
            throw new IllegalArgumentException("No edges with such name found: " + fromStation + " and " + toStation);
        updateSeatsForTrain(train, frEdge, toEdge, val);
    }

    /**
     * Sends message to RabbitMQ containing all station names
     * on train path, which must update their tableau
     *
     * @param train train
     */
    private void sendRefreshMessageToTableau(Train train) {
        StringBuilder sb = new StringBuilder();
        List<TrainEdge> trainPath = trainEdgeRepository.getTrainPath(train);
        for (TrainEdge edge : trainPath) {
            if (edge.getArrival() != null && edge.getArrival().toLocalDate().isEqual(LocalDate.now())) {
                sb.append(edge.getStation().getName()).append("/");
                continue;
            }
            if (edge.getDeparture() != null && edge.getDeparture().toLocalDate().isEqual(LocalDate.now())) {
                sb.append(edge.getStation().getName()).append("/");
            }
        }
        template.convertAndSend("stationUpdates", sb.toString());
        log.info("Message {} is sent to RabbitMQ", sb.toString());
    }

    /**
     * Sends message to RabbitMQ containing station names from given list
     *
     * @param stationNames list of station names
     */
    private void sendRefreshMessageToTableau(List<String> stationNames) {
        StringBuilder sb = new StringBuilder();
        stationNames.forEach(x -> sb.append(x).append('/'));

        template.convertAndSend("stationUpdates", sb.toString());
        log.info("Message {} is sent to RabbitMQ", sb.toString());
    }


    /**
     * Deletes train, train path and tickets from database
     *
     * @param trainName train name
     */
    public void deleteTrainByName(String trainName) {
        Train train = trainRepository.findTrainByTrainName(trainName);
        if (train == null)
            return;
        ArrayList<String> stations = new ArrayList<>();
        train.getPath().forEach(x -> stations.add(x.getStation().getName()));

        trainRepository.delete(train);

        sendRefreshMessageToTableau(stations);
        log.info("Train {} deleted", trainName);
    }

    @Autowired
    public void setTrainRepository(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    @Autowired
    public void setStationRepository(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Autowired
    public void setGraphService(GraphService graphService) {
        this.graphService = graphService;
    }

    @Autowired
    public void setEdgeService(EdgeService edgeService) {
        this.edgeService = edgeService;
    }

    @Autowired
    public void setTrainEdgeRepository(TrainEdgeRepository trainEdgeRepository) {
        this.trainEdgeRepository = trainEdgeRepository;
    }

    @Autowired
    public void setTemplate(AmqpTemplate template) {
        this.template = template;
    }
}
