package com.example.Users_Demo.dao;

import com.example.Users_Demo.entity.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomUserDAO extends JpaRepository<CustomUser, Integer> {

    @Query("SELECT c FROM CustomUser c WHERE c.username=:username")
    CustomUser findByUsername(String username);
}
