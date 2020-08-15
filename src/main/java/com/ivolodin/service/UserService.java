package com.ivolodin.service;

import com.ivolodin.dao.UserDao;
import com.ivolodin.entities.Role;
import com.ivolodin.entities.User;
import com.ivolodin.model.UserDetailsModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private final UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userDao.getByUsername(s);
        return new UserDetailsModel(user);
    }

    public User getUserByEmail(String email) {
        return userDao.getByUsername(email);
    }

    public User getUserByUsername(String name) {
        return userDao.getByUsername(name);
    }

    public boolean registerNewUser(String email, String username, String password) {
        User user = userDao.getByUsername(username);
        user = userDao.getByEmail(email);
        if (user == null) {
            user = new User();
            user.setUserName(username);
            user.setEmail(email);
            user.setPassword(password);
            user.setRoles(Collections.singleton(Role.USER));
            userDao.add(user);
            return true;
        } else return false;
    }

    public void save(User user) {
        userDao.update(user);
    }

    public List<User> getAllUsers() {
        return userDao.getAll();
    }

    public void updateUserRoles(Integer userId, boolean adminRole, boolean userRole) {
        User user = userDao.getById(userId);
        HashSet<Role> roles = new HashSet<>();

        if (adminRole) roles.add(Role.ADMIN);

        if (userRole) roles.add(Role.USER);

        if (roles.isEmpty()) roles.add(Role.USER);

        user.setRoles(roles);

        userDao.update(user);
    }
}
