package com.ivolodin.dao;

import com.ivolodin.entities.Station;
import com.ivolodin.entities.StationConnect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class StationConnectDaoImpl implements StationConnectDao {

    private final EntityManager entityManager;

    @Autowired
    public StationConnectDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public StationConnect getById(int id) {
        return entityManager.find(StationConnect.class, id);
    }

    @Override
    public void update(StationConnect edge) {
        entityManager.getTransaction().begin();
        entityManager.merge(edge);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(StationConnect edge) {
        entityManager.getTransaction().begin();
        entityManager.remove(edge);
        entityManager.getTransaction().commit();
    }

    @Override
    public void addConnect(StationConnect edge) {
        entityManager.getTransaction().begin();
        entityManager.persist(edge);
        entityManager.getTransaction().commit();
    }

    @Override
    public StationConnect getConnect(Station frStat, Station toStat) {
        TypedQuery<StationConnect> query = entityManager.createQuery(
                "select edge from StationConnect as edge, Station station" +
                        " where (edge.from.id = :frStat and edge.to.id = :toStat)", StationConnect.class);
        query.setParameter("frStat", frStat.getId());
        query.setParameter("toStat", toStat.getId());
        StationConnect edge = null;
        try {
            edge = query.getSingleResult();
        } catch (NoResultException ignored) {
        }
        return edge;
    }

    @Override
    public List getAll() {
        Query query = entityManager.createQuery("select edge from StationConnect edge");
        return query.getResultList();
    }
}
