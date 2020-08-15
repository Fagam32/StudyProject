package com.ivolodin.repositories;

import com.ivolodin.entities.StationConnect;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EdgeRepository extends JpaRepository<StationConnect, Integer> {

}
