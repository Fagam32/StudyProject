package com.ivolodin.dao;

import com.ivolodin.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private final EntityManager entityManager;

    @Override
    public User getById(int id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User getByUsername(String name) {
        Query query = entityManager.createQuery("select user from User user where user.userName = :name");
        query.setParameter("name", name);
        return (User) query.getSingleResult();
    }

    @Override
    public void update(User user) {
        entityManager.getTransaction().begin();
        entityManager.merge(user);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(User user) {
        entityManager.getTransaction().begin();
        entityManager.remove(user);
        entityManager.getTransaction().commit();
    }

    @Override
    public void add(User user) {
        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.getTransaction().commit();
    }

    @Override
    public List getAll() {
        Query query = entityManager.createQuery("select user from User user");
        return query.getResultList();
    }
}
