package com.ivolodin.dao;

import com.ivolodin.entities.Train;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
public class TrainDaoImpl implements TrainDao {


    private final EntityManager entityManager;

    @Autowired
    public TrainDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Train getById(int id) {
        return entityManager.find(Train.class, id);
    }

    @Override
    public void update(Train train) {
        entityManager.getTransaction().begin();
        entityManager.merge(train);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(Train train) {
        entityManager.getTransaction().begin();
        entityManager.remove(train);
        entityManager.getTransaction().commit();
    }

    @Override
    public void addTrain(Train train) {
        entityManager.getTransaction().begin();
        entityManager.persist(train);
        entityManager.getTransaction().commit();
    }

    @Override
    public List getAll() {
        Query query = entityManager.createQuery("select e from Train e");
        return query.getResultList();
    }
}
