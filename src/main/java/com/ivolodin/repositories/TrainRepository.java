package com.ivolodin.repositories;

import com.ivolodin.entities.Train;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainRepository extends JpaRepository<Train, Integer> {
    Train findTrainByTrainName(String name);
}
