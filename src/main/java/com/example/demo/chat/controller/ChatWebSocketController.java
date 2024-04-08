package com.example.demo.chat.controller;

import com.example.demo.chat.dto.ChatMessageRequestDto;
import com.example.demo.chat.service.ChatService;
import com.example.demo.exception.RacketPuncherException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;

import java.security.Principal;

import static com.example.demo.exception.type.ErrorCode.INVALID_SESSION;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final SessionRegistry sessionRegistry;
    private final ChatService chatService;

    @MessageMapping("/chat/{matchingId}")
    public void sendMessage(@DestinationVariable String matchingId, SimpMessageHeaderAccessor headerAccessor, ChatMessageRequestDto chatMessageRequestDto) {
        String sessionId = headerAccessor.getSessionId();
        SessionInformation sessionInfo = sessionRegistry.getSessionInformation(sessionId);
        if (sessionInfo == null){
            throw new RacketPuncherException(INVALID_SESSION);
        }
        Principal principal = (Principal) sessionInfo.getPrincipal();
        chatService.send(matchingId, principal.getName(), chatMessageRequestDto.getContent());
        chatService.updateNewMessageNum(matchingId);
    }

    @MessageMapping("/readMessage/{matchingId}")
    public void updateLastReadMessage(@DestinationVariable String matchingId, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        SessionInformation sessionInfo = sessionRegistry.getSessionInformation(sessionId);
        if (sessionInfo == null){
            throw new RacketPuncherException(INVALID_SESSION);
        }
        Principal principal = (Principal) sessionInfo.getPrincipal();
        chatService.updateLastReadTime(matchingId, principal.getName());
    }
}