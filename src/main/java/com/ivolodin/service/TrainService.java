package com.ivolodin.service;

import com.ivolodin.dao.TrainDao;
import com.ivolodin.dao.TrainEdgeDao;
import com.ivolodin.entities.Station;
import com.ivolodin.entities.StationConnect;
import com.ivolodin.entities.Train;
import com.ivolodin.entities.TrainEdge;
import com.ivolodin.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TrainService {

    private final StationGraph graph;

    private final TrainDao trainDao;

    private final TrainEdgeDao trainEdgeDao;

    private final StationService stationService;

    @Autowired
    public TrainService(TrainDao trainDao, StationGraph graph, StationService stationService, TrainEdgeDao trainEdgeDao) {
        this.trainDao = trainDao;
        this.graph = graph;
        this.stationService = stationService;
        this.trainEdgeDao = trainEdgeDao;
    }

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

}
