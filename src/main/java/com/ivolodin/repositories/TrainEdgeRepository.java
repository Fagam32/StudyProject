package com.ivolodin.repositories;

import com.ivolodin.model.entities.Station;
import com.ivolodin.model.entities.Train;
import com.ivolodin.model.entities.TrainEdge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainEdgeRepository extends JpaRepository<TrainEdge, Integer> {
    @Query("select edges from TrainEdge edges where edges.train =:train order by edges.order")
    List<TrainEdge> getTrainPath(Train train);

    Boolean existsTrainEdgeByStation(Station station);

    TrainEdge getTrainEdgeByStationNameAndTrain(String stationName, Train train);
}
