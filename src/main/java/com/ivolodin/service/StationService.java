package com.ivolodin.service;

import com.ivolodin.dao.StationConnectDao;
import com.ivolodin.dao.StationDao;
import com.ivolodin.dao.TrainEdgeDao;
import com.ivolodin.entities.Station;
import com.ivolodin.entities.StationConnect;
import com.ivolodin.entities.Train;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class StationService {

    @Autowired
    private final StationDao stationDao;

    @Autowired
    private final StationConnectDao stationConnectDao;

    @Autowired
    private final TrainEdgeDao trainEdgeDao;

    @Autowired
    private final StationGraph stationGraph;

    public void addStation(String stationName) {
        Station station = new Station(stationName);
        stationDao.addStation(station);
        stationGraph.addVertex(station);
    }

    public void addEdge(String frStatNm, String toStatNm, long distance) {
        if (distance <= 0)
            return;

        Station frStat = stationDao.getByName(frStatNm);
        Station toStat = stationDao.getByName(toStatNm);

        if (frStat != null && toStat != null) {
            StationConnect edge = stationConnectDao.getConnect(frStat, toStat);
            if (edge == null) {
                stationConnectDao.addConnect(new StationConnect(frStat, toStat, distance));
                stationGraph.addEdge(frStat, toStat, distance);
            } else {
                edge.setDistanceInMinutes(distance);
                stationConnectDao.update(edge);
                stationGraph.updateEdge(frStat, toStat, distance);
            }
        }
    }

    public Station getStationByName(String stat) {
        return stationDao.getByName(stat);
    }

    public List<StationConnect> getAllEdges() {
        return stationConnectDao.getAll();
    }

    public List<Station> getAllStations() {
        return stationDao.getAll();
    }

    public StationConnect getEdge(Station station1, Station station2) {
        return stationConnectDao.getConnect(station1, station2);
    }

    public Set<Train> getTrainsPassingFromStation(Station fr) {
        return trainEdgeDao.getTrainsPassingFromThis(fr);
    }

    public Set<Train> getTrainsPassingToStation(Station to) {
        return trainEdgeDao.getTrainsPassingToThis(to);
    }
}
