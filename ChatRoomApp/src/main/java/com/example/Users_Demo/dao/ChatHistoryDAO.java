package com.example.Users_Demo.dao;

import com.example.Users_Demo.entity.ChatHistory;
import com.example.Users_Demo.entity.CustomUser;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class ChatHistoryDAO {

    private final EntityManager entityManager;

    @Autowired
    public ChatHistoryDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void saveHistory(ChatHistory chatHistory) {
        entityManager.persist(chatHistory);
    }

    public List<String> findByUsers(CustomUser sender, CustomUser recipient) {
        return entityManager.createQuery("SELECT c.message FROM ChatHistory c WHERE c.sender=:senderid AND c.recip=:recipid" +
                        " OR c.sender=:recipid AND c.recip=:senderid", String.class)
                .setParameter("senderid", sender.getId())
                .setParameter("recipid", recipient.getId())
                .getResultList();

    }
}
