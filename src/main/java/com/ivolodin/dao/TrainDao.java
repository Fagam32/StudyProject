package com.ivolodin.dao;

import com.ivolodin.entities.Train;

import java.util.List;

public interface TrainDao {
    Train getById(int id);

    void update(Train train);

    void delete(Train train);

    void addTrain(Train train);

    List<Train> getAll();


}
