package com.ivolodin.repositories;

import com.ivolodin.entities.Station;
import com.ivolodin.entities.StationConnect;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EdgeRepository extends JpaRepository<StationConnect, Integer> {
    StationConnect findByFromAndTo(Station from, Station to);

    void deleteByFromAndTo(Station from, Station to);
}
