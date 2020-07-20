package com.ivolodin.dao;

import com.ivolodin.entities.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

@Service
public class StationDaoImpl implements StationDao {

    private final EntityManager entityManager;

    @Autowired
    public StationDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Station getById(int id) {
        return entityManager.find(Station.class, id);
    }

    @Override
    public Station getByName(String name) {
        TypedQuery<Station> query = entityManager.createQuery("select s from Station s where s.name = :name", Station.class);
        query.setParameter("name", name);
        return query.getSingleResult();
    }

    @Override
    public void update(Station station) {
        entityManager.getTransaction().begin();
        entityManager.refresh(station);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(Station station) {
        entityManager.getTransaction().begin();
        entityManager.remove(station);
        entityManager.getTransaction().commit();
    }

    @Override
    public void addStation(Station station) {
        entityManager.getTransaction().begin();
        entityManager.persist(station);
        entityManager.getTransaction().commit();
    }
}
