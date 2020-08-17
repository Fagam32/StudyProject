package com.ivolodin.dao;

import com.ivolodin.entities.Station;
import com.ivolodin.entities.StationConnect;
import com.ivolodin.entities.Train;
import com.ivolodin.entities.TrainEdge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class TrainEdgeDaoImpl implements TrainEdgeDao {

    @Autowired
    private EntityManager entityManager;

    @Override
    public TrainEdge getById(int id) {
        entityManager.getTransaction().begin();
        return entityManager.find(TrainEdge.class, id);
    }

    @Override
    public void update(TrainEdge edge) {
        entityManager.getTransaction().begin();
        entityManager.merge(edge);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(TrainEdge edge) {
        entityManager.getTransaction().begin();
        entityManager.remove(edge);
        entityManager.getTransaction().commit();
    }

    @Override
    public void addEdge(TrainEdge edge) {
        entityManager.getTransaction().begin();
        entityManager.persist(edge);
        entityManager.getTransaction().commit();
    }

    @Override
    public List getAll() {
        return entityManager.createQuery("select edge from TrainEdge edge").getResultList();
    }

    @Override
    public HashSet getTrainsPassingFromThis(Station fr) {
        Query query = entityManager.createQuery
                ("select train from TrainEdge edge, Train train" +
                        " where edge.stationConnect.from = :station" +
                        " and edge.train = train");
        query.setParameter("station", fr);
        return new HashSet<>(query.getResultList());
    }

    @Override
    public HashSet getTrainsPassingToThis(Station to) {
        Query query = entityManager.createQuery
                ("select train from TrainEdge edge, Train train " +
                        "where edge.stationConnect.to = :station " +
                        "and edge.train = train");
        query.setParameter("station", to);
        return new HashSet(query.getResultList());
    }

    @Override
    public List getTrainPath(Train train) {
        Query query = entityManager.createQuery("select edge from TrainEdge edge where edge.train = :train order by edge.id");
        query.setParameter("train", train);
        return query.getResultList();
    }

    @Override
    public List<Train> getTrainsPassingThroughThis(Station station) {
        List trains = entityManager.createQuery("select train from Train train, TrainEdge edge" +
                " where edge.stationConnect.from = :station or edge.stationConnect.to = :station")
                .setParameter("station", station)
                .getResultList();
        trains = new ArrayList<>(new HashSet<Train>(trains));
        return trains;
    }

    @Override
    public TrainEdge getEdgeByToStationAndTrain(Train train, Station station) {
        Query query = entityManager.createQuery("select edge from TrainEdge edge " +
                "where edge.train = :train and edge.stationConnect.to = :station")
                .setParameter("train", train)
                .setParameter("station", station);
        return (TrainEdge) query.getSingleResult();
    }

    @Override
    public Set<Integer> getTrainIdsByStationConnect(StationConnect sc) {
        return new HashSet<Integer>(entityManager.createQuery("select train.id from Train train, TrainEdge edge " +
                "where edge.stationConnect = :sc", Integer.class)
                .setParameter("sc", sc).getResultList());
    }

    @Override
    public Set<Train> getTrainsOnPath(Station fr, Station to) {
        Query query = entityManager.createQuery("select train from Train train, TrainEdge edge" +
                " where (edge.stationConnect.from =:frStation or edge.stationConnect.to = :toStation) and edge.train = train", Train.class);
        query.setParameter("frStation", fr);
        query.setParameter("toStation", to);
        return new HashSet<Train>(query.getResultList());
    }

}
