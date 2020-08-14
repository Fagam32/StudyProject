package com.ivolodin.repositories;

import com.ivolodin.entities.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, Integer> {

    Station findByName(String name);
}
