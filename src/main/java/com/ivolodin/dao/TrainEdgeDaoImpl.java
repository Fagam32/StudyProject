package com.ivolodin.dao;

import com.ivolodin.entities.Station;
import com.ivolodin.entities.Train;
import com.ivolodin.entities.TrainEdge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashSet;
import java.util.List;

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
                ("select train from TrainEdge edge, Train train where (edge.stationConnect.from.id = :id)");
        query.setParameter("id", fr.getId());
        return new HashSet<>(query.getResultList());
    }

    @Override
    public HashSet getTrainsPassingToThis(Station to) {
        Query query = entityManager.createQuery
                ("select train from TrainEdge edge, Train train where (edge.stationConnect.to.id = :id)");
        query.setParameter("id", to.getId());

        return new HashSet<>(query.getResultList());

    }

    @Override
    public List getTrainPath(Train train) {
        Query query = entityManager.createQuery("select edge from TrainEdge edge where edge.train = :train order by edge.id");
        query.setParameter("train", train);
        return query.getResultList();
    }

}
