package com.ivolodin.repositories;

import com.ivolodin.model.entities.Station;
import com.ivolodin.model.entities.StationConnect;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EdgeRepository extends JpaRepository<StationConnect, Integer> {
    StationConnect findByFromAndTo(Station from, Station to);

    void deleteByFromAndTo(Station from, Station to);

}
