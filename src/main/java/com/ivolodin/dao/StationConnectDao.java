package com.ivolodin.dao;

import com.ivolodin.entities.Station;
import com.ivolodin.entities.StationConnect;

import java.util.List;

public interface StationConnectDao {

    StationConnect getById(int id);

    StationConnect getConnect(Station frStat, Station toStat);

    void update(StationConnect edge);

    void delete(StationConnect edge);

    void addConnect(StationConnect edge);

    List<StationConnect> getAll();
}
