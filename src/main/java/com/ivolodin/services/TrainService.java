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
    @Autowired
    private TrainRepository trainRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private GraphService graphService;

    @Autowired
    private EdgeService edgeService;

    @Autowired
    private TrainEdgeRepository trainEdgeRepository;

    @Autowired
    private AmqpTemplate template;

    public List<TrainDto> getAllTrains() {
        List<Train> all = trainRepository.findAll();
        return MapperUtils.mapAll(all, TrainDto.class);
    }

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

        Train train = new Train();
        train.setTrainName(trainDto.getTrainName());
        train.setDeparture(trainDto.getDeparture());
        train.setSeatsNumber(trainDto.getSeatsNumber());
        train.setFromStation(fromStation);
        train.setToStation(toStation);
        trainRepository.save(train);

        createPathForTrain(train);
        sendRefreshMessageToTableau(train);
        log.info("Train {} added", train.toString());
        return MapperUtils.map(train, TrainDto.class);
    }

    private void createPathForTrain(Train train) throws PathNotExistException {

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

        trainEdgeRepository.saveAll(path);

        train.setArrival(lastEdge.getArrival());

        trainRepository.save(train);

        sendRefreshMessageToTableau(train);
    }

    public TrainDto getTrainInfo(TrainDto trainDto) {
        Train train = trainRepository.findTrainByTrainName(trainDto.getTrainName());
        if (train == null)
            throw new EntityNotFoundException("Train with name " + trainDto.getTrainName() + " not found");

        return MapperUtils.map(train, TrainDto.class);
    }

    public void updateStandings(List<TrainEdgeDto> stationUpdates) {
        Train train = trainRepository.findTrainByTrainName(stationUpdates.get(0).getTrainName());
        if (train == null)
            throw new EntityNotFoundException("Train with name " + stationUpdates.get(0).getTrainName() + " not found");

        List<TrainEdge> trainPath = trainEdgeRepository.getTrainPath(train);
        for (TrainEdgeDto newEdge : stationUpdates) {
            for (TrainEdge oldEdge : trainPath) {
                if (oldEdge.getStation().getName().equals(newEdge.getStationName())) {
                    oldEdge.setStandingMinutes(newEdge.getStanding());
                    break;
                }
            }
        }
        updateTrainTimes(train, trainPath);
        sendRefreshMessageToTableau(train);
    }

    private void updateTrainTimes(Train train, List<TrainEdge> trainPath) {
        for (int i = 1; i < trainPath.size() - 1; i++) {
            TrainEdge curEdge = trainPath.get(i);
            TrainEdge nextEdge = trainPath.get(i + 1);
            Integer distance = edgeService.getDistanceBetweenStations(curEdge.getStation(), nextEdge.getStation());
            curEdge.setDeparture(curEdge.getArrival().plusMinutes(curEdge.getStandingMinutes()));
            nextEdge.setArrival(curEdge.getDeparture().plusMinutes(distance));
        }
        train.setArrival(trainPath.get(trainPath.size() - 1).getArrival());
        trainRepository.save(train);
        trainEdgeRepository.saveAll(trainPath);
        log.info("Train {} updated", train.toString());
    }

    public List<TrainDto> getTrainsOnStation(StationDto stationDto, LocalDate date) {
        if (date.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Date is in past");
        List<Train> trains = trainRepository.findTrainsByStationAndDate(stationDto.getName(), java.sql.Date.valueOf(date));
        return MapperUtils.mapAll(trains, TrainDto.class);
    }

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
}
