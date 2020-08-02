package com.ivolodin.dao;

import com.ivolodin.entities.Ticket;
import com.ivolodin.entities.Train;
import com.ivolodin.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class TicketDaoImpl implements TicketDao {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Ticket getById(int id) {
        return entityManager.find(Ticket.class, id);
    }

    @Override
    public void update(Ticket ticket) {
        entityManager.getTransaction().begin();
        entityManager.merge(ticket);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(Ticket ticket) {
        entityManager.getTransaction().begin();
        entityManager.remove(ticket);
        entityManager.getTransaction().commit();
    }

    @Override
    public void add(Ticket ticket) {
        entityManager.getTransaction().begin();
        entityManager.persist(ticket);
        entityManager.getTransaction().commit();
    }

    @Override
    public List getAll() {
        return entityManager.createQuery("select ticket from Ticket ticket").getResultList();
    }

    @Override
    public Set<Ticket> getAllForTrain(Train train) {
        Query query = entityManager.createQuery("select ticket from Ticket ticket where ticket.train = :train");
        query.setParameter("train", train);
        return new HashSet<>(query.getResultList());
    }

    @Override
    public List<Ticket> getTicketsFromUser(User user) {
        Query query = entityManager.createQuery("select ticket from Ticket ticket where ticket.user = :user");
        query.setParameter("user", user);
        return query.getResultList();
    }
}
