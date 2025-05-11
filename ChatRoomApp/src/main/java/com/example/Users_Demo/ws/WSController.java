package com.example.Users_Demo.ws;

import com.example.Users_Demo.dao.ChatHistoryDAO;
import com.example.Users_Demo.dao.CustomUserDAO;
import com.example.Users_Demo.entity.ChatHistory;
import com.example.Users_Demo.entity.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
public class WSController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatHistoryDAO chatHistoryDAO;
    private final CustomUserDAO customUserDAO;

    @Autowired
    public WSController(SimpMessagingTemplate simpMessagingTemplate, ChatHistoryDAO chatHistoryDAO, CustomUserDAO customUserDAO) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.chatHistoryDAO = chatHistoryDAO;
        this.customUserDAO = customUserDAO;
    }

    @MessageMapping("/chat")
    public void sendToUser(@Payload ChatMessage chatMessage) {
        saveChatHistory(chatMessage);
        simpMessagingTemplate.convertAndSendToUser(chatMessage.getRecipient(), "/queue/chat", chatMessage);
    }

    private void saveChatHistory(ChatMessage chatMessage) {
        CustomUser sender = customUserDAO.findByUsername(chatMessage.getSender());
        CustomUser recipient = customUserDAO.findByUsername(chatMessage.getRecipient());
        chatHistoryDAO.saveHistory(new ChatHistory(sender.getId(), recipient.getId(), sender.getUsername() + ": " + chatMessage.getMessage()));
        System.out.println(chatHistoryDAO.findByUsers(sender, recipient));
    }
}
