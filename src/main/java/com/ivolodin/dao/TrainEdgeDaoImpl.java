package com.ivolodin.dao;

import com.ivolodin.entities.TrainEdge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
@Repository
public class TrainEdgeDaoImpl implements TrainEdgeDao {

    private final EntityManager entityManager;

    @Autowired
    public TrainEdgeDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public TrainEdge getById(int id) {
        entityManager.getTransaction().begin();
        return entityManager.find(TrainEdge.class, id);
    }

    @Override
    public void update(TrainEdge edge) {
        entityManager.getTransaction().begin();
        entityManager.refresh(edge);
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
}
