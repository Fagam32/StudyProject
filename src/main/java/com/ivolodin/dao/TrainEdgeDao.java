package com.ivolodin.dao;

import com.ivolodin.entities.TrainEdge;

import java.util.List;

public interface TrainEdgeDao {

    TrainEdge getById(int id);

    void update(TrainEdge edge);

    void delete(TrainEdge edge);

    void addEdge(TrainEdge edge);

    List<TrainEdge> getAll();
}
