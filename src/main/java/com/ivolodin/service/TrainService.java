package com.ivolodin.service;

import com.ivolodin.dao.TrainDao;
import com.ivolodin.dao.TrainEdgeDao;
import com.ivolodin.entities.Station;
import com.ivolodin.entities.StationConnect;
import com.ivolodin.entities.Train;
import com.ivolodin.entities.TrainEdge;
import com.ivolodin.exceptions.TrainException;
import com.ivolodin.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TrainService {
    @Autowired
    private final StationGraph graph;
    @Autowired
    private final TrainDao trainDao;
    @Autowired
    private final TrainEdgeDao trainEdgeDao;
    @Autowired
    private final StationService stationService;

    public void makeNewTrain(String frStat, String toStat, String departure, int seats) {
        Station frStation = stationService.getStationByName(frStat);
        Station toStation = stationService.getStationByName(toStat);
        makeNewTrain(frStation, toStation, departure, seats);
    }

    public void makeNewTrain(Station frStat, Station toStat, String departure, int seats) {
        if (frStat == null || toStat == null)
            return;
        List<Station> pathList = graph.getPathList(frStat, toStat);
        if (pathList == null)
            return;

        LocalDateTime departureDate = Utils.createDateTimeFromString(departure);
        Train train = new Train(seats, frStat, toStat, departureDate);
        trainDao.addTrain(train);

        List<TrainEdge> trainEdgeList = new ArrayList<>();

        for (int i = 0; i < pathList.size() - 1; i++) {
            StationConnect edge = stationService.getEdge(pathList.get(i), pathList.get(i + 1));
            TrainEdge trainEdge = new TrainEdge();

            trainEdge.setTrain(train);
            trainEdge.setSeatsLeft(train.getSeatsNumber());
            trainEdge.setStationConnect(edge);

            if (i == 0) {
                LocalDateTime trainDeparture = train.getDeparture();
                LocalDateTime arrival = trainDeparture.plus(Duration.ofMinutes(edge.getDistanceInMinutes()));
                trainEdge.setArrival(arrival);
            } else {
                LocalDateTime arrival = trainEdgeList.get(i - 1).getArrival();
                trainEdge.setArrival(arrival.plus(Duration.ofMinutes(edge.getDistanceInMinutes())));
            }

            trainEdgeList.add(trainEdge);

        }
        train.setArrival(trainEdgeList.get(trainEdgeList.size() - 1).getArrival());
        trainDao.update(train);

        for (TrainEdge edge : trainEdgeList)
            trainEdgeDao.addEdge(edge);
    }

    public List<Train> getAllTrains() {
        return trainDao.getAll();
    }

    public void deleteTrainById(int trainId) {
        Train train = trainDao.getById(trainId);
        if (train != null)
            trainDao.delete(train);
    }

    public List<Train> searchTrainInDate(Station fr, Station to, LocalDate trainDate) {
        //TODO: find bug here. These methods return value is not always right.
        Set<Train> trainsPassingFromStation = stationService.getTrainsPassingFromStation(fr);
        Set<Train> trainsPassingToStation = stationService.getTrainsPassingToStation(to);

        trainsPassingFromStation.retainAll(trainsPassingToStation);

        return chooseTrainsInCorrectDate(fr, to, trainDate, trainsPassingFromStation);
    }

    private List<Train> chooseTrainsInCorrectDate(Station fr, Station to, LocalDate trainDate, Set<Train> trainsPassingFromStation) {
        List<Train> resultList = new ArrayList<>();
        //find trains which departure to the FROM station happens on the same date
        for (Train train : trainsPassingFromStation) {
            List<TrainEdge> path = trainEdgeDao.getTrainPath(train);
            Train trainToAdd = new Train();
            trainToAdd.setId(train.getId());

            int seatsLeft = train.getSeatsNumber();
            trainToAdd.setSeatsNumber(seatsLeft);

            for (int i = 0; i < path.size(); i++) {
                TrainEdge edge = path.get(i);
                if (edge.getStationConnect().getFrom().equals(fr)
                        && edge.getArrival().isAfter(trainDate.atStartOfDay())) {
                    trainToAdd.setFromStation(fr);
                    trainToAdd.setToStation(to);
                    seatsLeft = Math.min(seatsLeft, edge.getSeatsLeft());
                    if (i != 0)
                        trainToAdd.setDeparture(path.get(i - 1).getArrival());

                    //counting arrival for the TO station
                    for (; i < path.size(); i++) {
                        seatsLeft = Math.min(seatsLeft, path.get(i).getSeatsLeft());
                        TrainEdge arrivalEdge = path.get(i);
                        if (arrivalEdge.getStationConnect().getTo().equals(to)) {
                            trainToAdd.setArrival(arrivalEdge.getArrival());
                            break;
                        }
                    }
                    trainToAdd.setSeatsNumber(seatsLeft);
                    if (trainToAdd.getDeparture() == null)
                        trainToAdd.setDeparture(train.getDeparture());
                    if (seatsLeft > 0 && trainToAdd.getArrival() != null)
                        resultList.add(trainToAdd);
                }
            }
        }

        sortTrainListByDeparture(resultList);

        return resultList;
    }

    private void sortTrainListByDeparture(List<Train> resultList) {
        Comparator<Train> trainComparator = (Train t1, Train t2) -> {
            if (t1.getDeparture().isBefore(t2.getDeparture()))
                return -1;
            else if (t1.getDeparture().isAfter(t2.getDeparture()))
                return 1;
            else return 0;
        };
        resultList.sort(trainComparator);
    }

    public void updateSeatsOnPath(Train train, Station frSt, Station toSt) throws TrainException {
        List<TrainEdge> path = train.getPath();
        int start = -1;
        int end = -1;
        for (int i = 0; i < path.size(); i++) {
            if (path.get(i).getStationConnect().getFrom().equals(frSt))
                start = i;
            if (path.get(i).getStationConnect().getTo().equals(toSt))
                end = i;
        }
        if (start < 0 || end < 0)
            throw new TrainException("Troubles with Train path");

        for (int i = start; i <= end; i++) {
            TrainEdge edge = path.get(i);
            edge.setSeatsLeft(edge.getSeatsLeft() - 1);
        }
        for (TrainEdge edge : path)
            trainEdgeDao.update(edge);
    }

    public LocalDateTime getDepartureTimeOnStation(Train train, Station frSt) {
        List<TrainEdge> path = train.getPath();
        for (TrainEdge edge : path) {
            if (edge.getStationConnect().getFrom().equals(frSt))
                return edge.getArrival();
        }
        return null;
    }

    public LocalDateTime getArrivalTimeOnStation(Train train, Station toSt) {
        List<TrainEdge> path = train.getPath();
        for (TrainEdge edge : path) {
            if (edge.getStationConnect().getTo().equals(toSt))
                return edge.getArrival();
        }
        return null;


    }
}
