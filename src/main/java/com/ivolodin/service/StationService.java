package com.ivolodin.service;

import com.ivolodin.dao.StationConnectDao;
import com.ivolodin.dao.StationDao;
import com.ivolodin.entities.Station;
import com.ivolodin.entities.StationConnect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StationService {

    private final StationDao stationDao;

    private final StationConnectDao stationConnectDao;

    private final StationGraph stationGraph;

    @Autowired
    public StationService(StationDao stationDao, StationConnectDao stationConnectDao, StationGraph stationGraph) {
        this.stationDao = stationDao;
        this.stationConnectDao = stationConnectDao;
        this.stationGraph = stationGraph;
    }

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
                stationGraph.addEdge(frStat,toStat, distance);
            } else {
                edge.setDistanceInMinutes(distance);
                stationConnectDao.update(edge);
                stationGraph.updateEdge(frStat, toStat, distance);
            }
        }
    }

    public Station getStationByName(String stat){
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
}
