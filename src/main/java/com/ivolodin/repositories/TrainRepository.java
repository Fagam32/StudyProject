package com.ivolodin.repositories;

import com.ivolodin.entities.Station;
import com.ivolodin.entities.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface TrainRepository extends JpaRepository<Train, Integer> {
    Train findTrainByTrainName(String name);

    @Query("select train from Train train, TrainEdge edge where " +
            "edge.train = train " +
            "and edge.station.name = :station " +
            "and ( cast(edge.arrival as date) = :date or cast(edge.departure as date) = :date)")
    List<Train> findTrainsByStationAndDate(String station, Date date);

    @Query("select train from Train train, TrainEdge edge where  " +
            "edge.train = train " +
            "and edge.station.name = :station " +
            "and cast(edge.departure as date) = :date")
    List<Train> findTrainsDepartingFromStation(String station, Date date);

    @Query("select train from Train train, TrainEdge edge where edge.train = train and edge.station = :station")
    List<Train> findTrainsPassingThroughStation(Station station);

}
