package com.ivolodin.dao;

import com.ivolodin.entities.User;

import java.util.List;

public interface UserDao {
    User getById(int id);

    User getByUsername(String name);

    User getByEmail(String email);

    void update(User user);

    void delete(User user);

    void add(User user);

    List<User> getAll();
}
