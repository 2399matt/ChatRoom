package com.example.Users_Demo.security;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionTracker {

    private final Set<String> onlineUsers = ConcurrentHashMap.newKeySet();

    public void addOnlineUser(String username) {
        onlineUsers.add(username);
    }

    public void removeOnlineUser(String username) {
        onlineUsers.remove(username);
    }

    public Set<String> getOnlineUsers() {
        return Collections.unmodifiableSet(onlineUsers);
    }


}
