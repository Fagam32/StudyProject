package com.ivolodin.service;

import com.ivolodin.dao.StationDao;
import com.ivolodin.entities.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StationService {

    private StationDao stationDao;

    @Autowired
    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public void addStation(String stationName) {
        stationDao.addStation(new Station(stationName));
    }
}
