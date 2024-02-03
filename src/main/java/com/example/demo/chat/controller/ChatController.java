package com.example.demo.chat.controller;

import com.example.demo.chat.dto.ChatMessageRequest;
import com.example.demo.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SessionRegistry sessionRegistry;
    private final ChatService chatService;

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public void sendMessage(ChatMessageRequest chatMessageRequest, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        SessionInformation sessionInfo = sessionRegistry.getSessionInformation(sessionId);
        if (sessionInfo == null){
            throw new IllegalArgumentException("Invalid session information");
        }
        Principal principal = (Principal) sessionInfo.getPrincipal();
        chatService.send(principal.getName(), chatMessageRequest.getContent());
    }
}