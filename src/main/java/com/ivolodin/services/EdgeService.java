package com.ivolodin.services;

import com.ivolodin.dto.StationConnectDto;
import com.ivolodin.entities.Station;
import com.ivolodin.entities.StationConnect;
import com.ivolodin.entities.Train;
import com.ivolodin.entities.TrainEdge;
import com.ivolodin.repositories.EdgeRepository;
import com.ivolodin.repositories.StationRepository;
import com.ivolodin.repositories.TrainRepository;
import com.ivolodin.utils.MapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Transactional
@Service
public class EdgeService {

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private EdgeRepository edgeRepository;

    @Autowired
    private TrainRepository trainRepository;

    @Autowired
    private GraphService graphService;

    private TrainService trainService;

    public StationConnectDto addNewEdge(StationConnectDto newSc) {
        Station frSt = stationRepository.findByName(newSc.getFromStation());
        Station toSt = stationRepository.findByName(newSc.getToStation());

        checkConnectExisting(newSc, frSt, toSt);
        graphService.addEdge(frSt, toSt, newSc.getDistance());

        StationConnect scEntity = new StationConnect();
        scEntity.setFrom(frSt);
        scEntity.setTo(toSt);
        scEntity.setDistanceInMinutes(newSc.getDistance());

        StationConnect saved = edgeRepository.save(scEntity);
        log.info("Edge {} added" + newSc.toString());
        return MapperUtils.map(saved, StationConnectDto.class);
    }

    public void removeEdge(StationConnectDto sc) {

        Station frSt = stationRepository.findByName(sc.getFromStation());
        Station toSt = stationRepository.findByName(sc.getToStation());

        checkConnectExisting(sc, frSt, toSt);

        checkIfTrainsPassingThrough(frSt, toSt);

        edgeRepository.deleteByFromAndTo(frSt, toSt);

        graphService.deleteEdge(frSt, toSt);
        log.info("Edge {} deleted", sc.toString());
    }

    private void checkIfTrainsPassingThrough(Station frSt, Station toSt) {
        HashSet<Train> trainsFrom = new HashSet<>(trainRepository.findTrainsPassingThroughStation(frSt));
        HashSet<Train> trainsTo = new HashSet<>(trainRepository.findTrainsPassingThroughStation(toSt));

        trainsFrom.removeIf(x -> !trainsTo.contains(x));

        for (Train train : trainsFrom) {
            List<TrainEdge> path = train.getPath();
            for (int i = 0; i < path.size() - 1; i++) {
                TrainEdge cur = path.get(i);
                TrainEdge next = path.get(i + 1);
                if (cur.getStation().equals(frSt) && next.getStation().equals(toSt)) {
                    throw new IllegalArgumentException("Train edge can't be deleted." +
                            " Trains passing through this edge must be deleted first");
                }
            }
        }
    }

    public List<StationConnectDto> getAll() {
        return MapperUtils.mapAll(edgeRepository.findAll(), StationConnectDto.class);
    }

    public StationConnectDto update(StationConnectDto newSc) {
        Station frSt = stationRepository.findByName(newSc.getFromStation());
        Station toSt = stationRepository.findByName(newSc.getToStation());

        checkConnectExisting(newSc, frSt, toSt);

        StationConnect connect = edgeRepository.findByFromAndTo(frSt, toSt);
        connect.setDistanceInMinutes(newSc.getDistance());
        edgeRepository.save(connect);

        graphService.updateEdge(connect.getFrom(), connect.getTo(), connect.getDistanceInMinutes());

        HashSet<Train> trainsFrom = new HashSet<>(trainRepository.findTrainsPassingThroughStation(frSt));
        HashSet<Train> trainsTo = new HashSet<>(trainRepository.findTrainsPassingThroughStation(toSt));
        trainsFrom.removeIf(x -> !trainsTo.contains(x));

        List<Train> trainsToBeUpdated = new ArrayList<>();

        for (Train train : trainsFrom) {
            List<TrainEdge> path = train.getPath();
            for (int i = 0; i < path.size() - 1; i++) {
                TrainEdge cur = path.get(i);
                TrainEdge next = path.get(i + 1);
                if (cur.getStation().equals(frSt) && next.getStation().equals(toSt)) {
                    trainsToBeUpdated.add(train);
                    break;
                }
            }
        }

        for (Train train : trainsToBeUpdated)
            trainService.refreshTrainTimes(train);
        log.info("Edge {} updated", newSc.toString());
        return MapperUtils.map(connect, StationConnectDto.class);
    }

    public StationConnect getStationConnect(Station fr, Station to) {
        return edgeRepository.findByFromAndTo(fr, to);
    }

    private void checkConnectExisting(StationConnectDto newSc, Station frSt, Station toSt) {
        if (frSt == null || toSt == null) {
            if (frSt == null)
                throw new EntityNotFoundException("Station with name " + newSc.getFromStation() + " not found");
            else
                throw new EntityNotFoundException("Station with name " + newSc.getToStation() + " not found");
        }
    }

    public Integer getDistanceBetweenStations(Station fr, Station to) {
        return edgeRepository.findByFromAndTo(fr, to).getDistanceInMinutes();
    }

    @Autowired
    public void setTrainService(TrainService trainService) {
        this.trainService = trainService;
    }
}
