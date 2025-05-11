package com.example.Users_Demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "chathistory")
public class ChatHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "sender_id")
    private int sender;

    @Column(name = "recip_Id")
    private int recip;

    @Column(name = "msg")
    private String message;

    public ChatHistory() {

    }

    public ChatHistory(int sender, int recip, String message) {
        this.sender = sender;
        this.recip = recip;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public int getRecip() {
        return recip;
    }

    public void setRecip(int recip) {
        this.recip = recip;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
