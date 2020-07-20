package com.ivolodin.dao;

import com.ivolodin.entities.Station;

import java.util.List;

public interface StationDao {

    Station getById(int id);

    Station getByName(String name);

    void update(Station station);

    void delete(Station station);

    void addStation(Station station);

    List<Station> getAll();
}
