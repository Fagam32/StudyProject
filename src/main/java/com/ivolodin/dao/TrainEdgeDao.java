package com.ivolodin.dao;

import com.ivolodin.entities.Station;
import com.ivolodin.entities.Train;
import com.ivolodin.entities.TrainEdge;

import java.util.List;
import java.util.Set;

public interface TrainEdgeDao {

    TrainEdge getById(int id);

    void update(TrainEdge edge);

    void delete(TrainEdge edge);

    void addEdge(TrainEdge edge);

    List<TrainEdge> getAll();

    Set<Train> getTrainsPassingFromThis(Station fr);

    Set<Train> getTrainsPassingToThis(Station to);

    List<TrainEdge> getTrainPath(Train train);
}
