package com.example.Users_Demo.controller;

import com.example.Users_Demo.security.SessionTracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class UserListener {

    private final SessionTracker sessionTracker;

    @Autowired
    public UserListener(SessionTracker sessionTracker) {
        this.sessionTracker = sessionTracker;
    }

    @EventListener
    public void userHasConnected(AuthenticationSuccessEvent event) {
        String username = event.getAuthentication().getName();
        sessionTracker.addOnlineUser(username);
    }

    @EventListener
    public void onLogout(LogoutSuccessEvent event) {
        String username = event.getAuthentication().getName();
        sessionTracker.removeOnlineUser(username);
    }
}
