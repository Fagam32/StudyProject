package com.ivolodin.repositories;

import com.ivolodin.model.entities.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StationRepository extends JpaRepository<Station, Integer> {

    Station findByName(String name);

    List<Station> findByNameContaining(String stationName);

    Boolean existsByName(String stationName);
}
