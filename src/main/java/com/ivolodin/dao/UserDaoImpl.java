package com.ivolodin.dao;

import com.ivolodin.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
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
        TypedQuery<User> query = entityManager.createQuery("select u from User u where u.userName = :name", User.class);
        query.setParameter("name", name);

        try {
            return query.getSingleResult();
        } catch (NoResultException ignored) {
            return null;
        }
    }

    @Override
    public User getByEmail(String email) {
        TypedQuery<User> query = entityManager.createQuery("select u from User u where u.email = :email", User.class);
        query.setParameter("email", email);
        try {
            return query.getSingleResult();
        } catch (NoResultException ignored) {
            return null;
        }
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
