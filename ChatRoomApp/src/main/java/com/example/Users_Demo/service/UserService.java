package com.example.Users_Demo.service;

import com.example.Users_Demo.dao.CustomUserDAO;
import com.example.Users_Demo.entity.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class UserService {

    private final CustomUserDAO customUserDAO;

    @Autowired
    public UserService(CustomUserDAO customUserDAO) {
        this.customUserDAO = customUserDAO;
    }

    @Transactional
    public void saveUser(CustomUser user) {
        customUserDAO.save(user);
    }

    @Transactional
    public void deleteUser(CustomUser user) {
        customUserDAO.delete(user);
    }

    public CustomUser findByUsername(String username) {
        return customUserDAO.findByUsername(username);
    }

    public CustomUser findById(int id) {
        Optional<CustomUser> user = customUserDAO.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new RuntimeException("User not found!");
        }
    }

    @Transactional
    public CustomUser updateUser(CustomUser user) {
        return customUserDAO.save(user);
    }
}
