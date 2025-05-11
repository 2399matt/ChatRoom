package com.example.Users_Demo.controller;

import com.example.Users_Demo.dao.ChatHistoryDAO;
import com.example.Users_Demo.dao.CustomUserDAO;
import com.example.Users_Demo.entity.CustomUser;
import com.example.Users_Demo.security.SessionTracker;
import com.example.Users_Demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final UserService userService;
    private final SessionTracker sessionTracker;
    private final ChatHistoryDAO chatHistoryDAO;
    private final CustomUserDAO customUserDAO;

    @Autowired
    public HomeController(UserService userService, SessionTracker sessionTracker, ChatHistoryDAO chatHistoryDAO, CustomUserDAO customUserDAO) {
        this.userService = userService;
        this.sessionTracker = sessionTracker;
        this.chatHistoryDAO = chatHistoryDAO;
        this.customUserDAO = customUserDAO;
    }

    @GetMapping("/list")
    public String listUsers(Principal principal, Model model) {
        model.addAttribute("currentUser", principal.getName());
        return "homePage";
    }

    @GetMapping("/refresh")
    public String refreshUsers(Model model) {
        model.addAttribute("users", sessionTracker.getOnlineUsers());
        return "userList :: userListFrag";
    }

    @GetMapping("/message-area")
    public String showMessageArea(@RequestParam(name = "recipient") String recipient, Model model) {
        model.addAttribute("recipient", recipient);
        return "chatPage :: chatArea";
    }

    @GetMapping("/getChatHistory")
    @ResponseBody
    public List<String> getChatHistory(@RequestParam("sender") String sender, @RequestParam("recipient") String recipient) {
        if (sender != null && recipient != null) {
            CustomUser sendee = customUserDAO.findByUsername(sender);
            CustomUser recip = customUserDAO.findByUsername(recipient);
            List<String> chatHistory = chatHistoryDAO.findByUsers(sendee, recip);
            return chatHistory;
        } else {
            throw new RuntimeException("Unable to locate message history!");
        }
    }

}
