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


    private StationRepository stationRepository;

    private EdgeRepository edgeRepository;

    private TrainRepository trainRepository;

    private GraphService graphService;

    private TrainService trainService;

    /**
     * Adds new {@link StationConnect} between from and to stations to database and graph
     *
     * @param newSc the {@link StationConnectDto} containing from and to stations and distance between them
     * @return the {@link StationConnectDto} of newly added connect
     */
    public StationConnectDto addNewEdge(StationConnectDto newSc) {
        checkStationsExist(newSc);

        Station frSt = stationRepository.findByName(newSc.getFromStation());
        Station toSt = stationRepository.findByName(newSc.getToStation());
        graphService.addEdge(frSt, toSt, newSc.getDistance());

        StationConnect scEntity = new StationConnect();
        scEntity.setFrom(frSt);
        scEntity.setTo(toSt);
        scEntity.setDistanceInMinutes(newSc.getDistance());

        StationConnect saved = edgeRepository.save(scEntity);
        log.info("Edge {} added" + newSc.toString());
        return MapperUtils.map(saved, StationConnectDto.class);
    }

    /**
     * Removes connect between from and to stations from database and graph
     *
     * @param sc the {@link StationConnectDto} containing from and to stations
     */
    public void removeEdge(StationConnectDto sc) {


        checkStationsExist(sc);

        Station frSt = stationRepository.findByName(sc.getFromStation());
        Station toSt = stationRepository.findByName(sc.getToStation());

        checkIfTrainsPassingThrough(frSt, toSt);

        edgeRepository.deleteByFromAndTo(frSt, toSt);

        graphService.deleteEdge(frSt, toSt);
        log.info("Edge {} deleted", sc.toString());
    }

    /**
     * Checks is there any trains passing through these stations
     *
     * @param frSt the {@link Station} from
     * @param toSt the {@link Station} to
     * @throws IllegalArgumentException if there's at least one train passing through edge
     */
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

    /**
     * @return List of all edges between stations
     */
    public List<StationConnectDto> getAll() {
        return MapperUtils.mapAll(edgeRepository.findAll(), StationConnectDto.class);
    }

    /**
     * Updates existing connect between two stations and updates train times, passing through this edge
     *
     * @param newSc the {@link StationConnectDto} with new distance
     * @return updated {@link StationConnectDto}
     */
    public StationConnectDto update(StationConnectDto newSc) {
        checkStationsExist(newSc);

        Station frSt = stationRepository.findByName(newSc.getFromStation());
        Station toSt = stationRepository.findByName(newSc.getToStation());

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

    /**
     * Returns {@link StationConnect} between two stations
     *
     * @param fr {@link Station} from station
     * @param to {@link Station} to station
     * @return {@link StationConnect} entity
     */
    public StationConnect getStationConnect(Station fr, Station to) {
        return edgeRepository.findByFromAndTo(fr, to);
    }

    /**
     * Check if stations from {@link StationConnectDto} exist
     *
     * @param newSc the {@link StationConnectDto}
     */
    private void checkStationsExist(StationConnectDto newSc) {
        if (!stationRepository.existsByName(newSc.getFromStation()))
            throw new EntityNotFoundException("Station with name " + newSc.getFromStation() + " not found");
        if (!stationRepository.existsByName(newSc.getToStation()))
            throw new EntityNotFoundException("Station with name " + newSc.getToStation() + " not found");

    }


    /**
     * Get distance between stations
     *
     * @param fr {@link Station} from
     * @param to {@link Station} to
     * @return distance or null
     */
    public Integer getDistanceBetweenStations(Station fr, Station to) {
        return edgeRepository.findByFromAndTo(fr, to).getDistanceInMinutes();
    }

    @Autowired
    public void setStationRepository(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Autowired
    public void setEdgeRepository(EdgeRepository edgeRepository) {
        this.edgeRepository = edgeRepository;
    }

    @Autowired
    public void setTrainRepository(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    @Autowired
    public void setGraphService(GraphService graphService) {
        this.graphService = graphService;
    }

    @Autowired
    public void setTrainService(TrainService trainService) {
        this.trainService = trainService;
    }
}
