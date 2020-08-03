package com.ivolodin.dao;

import com.ivolodin.entities.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class StationDaoImpl implements StationDao {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Station getById(int id) {
        return entityManager.find(Station.class, id);
    }

    @Override
    public Station getByName(String name) {
        TypedQuery<Station> query = entityManager.createQuery("select s from Station s where s.name = :name", Station.class);
        query.setParameter("name", name);

        try {
            return query.getSingleResult();
        } catch (NoResultException ignored) {
            return null;
        }
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

    @Override
    public List getAll() {
        Query query = entityManager.createQuery("select e from Station e");
        return query.getResultList();
    }
}
