package com.ivolodin.service;

import com.ivolodin.dao.UserDao;
import com.ivolodin.entities.User;
import com.ivolodin.model.UserDetailsModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

    public User getUserByEmail(String email){
        return userDao.getByUsername(email);
    }

    public User getUserByUsername(String name){
        return userDao.getByUsername(name);
    }

    public void addUser(User user) {
        userDao.add(user);
    }
}
