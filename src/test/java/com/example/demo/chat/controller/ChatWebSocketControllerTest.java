package com.example.demo.chat.controller;

import com.example.demo.auth.security.JwtAuthenticationFilter;
import com.example.demo.auth.security.SecurityConfiguration;
import com.example.demo.auth.security.TokenProvider;
import com.example.demo.chat.dto.ChatMessageRequestDto;
import com.example.demo.chat.dto.LastReadTimeUpdateDto;
import com.example.demo.chat.service.ChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.security.Principal;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ChatWebSocketController.class, SecurityConfiguration.class})
class ChatWebSocketControllerTest {

    @MockBean
    private ChatService chatService;

    @MockBean
    private SessionRegistry sessionRegistry;

    @InjectMocks
    @Autowired
    private ChatWebSocketController chatWebSocketController;

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private SimpMessageHeaderAccessor headerAccessor;

    @BeforeEach
    void setUp() {
        headerAccessor = SimpMessageHeaderAccessor.create();
    }

    @Test
    void sendMessage() throws Exception {
        // given
        String matchingId = "1";
        String sessionId = "session";
        Principal mockPrincipal = mock(Principal.class);
        SessionInformation sessionInformation = mock(SessionInformation.class);
        String userEmail = "email";
        String content = "content";

        given(mockPrincipal.getName())
                .willReturn(userEmail);
        given(sessionInformation.getPrincipal())
                .willReturn(mockPrincipal);
        when(sessionRegistry.getSessionInformation(sessionId))
                .thenReturn(sessionInformation);

        ChatMessageRequestDto chatMessageRequestDto = new ChatMessageRequestDto();
        chatMessageRequestDto.setContent(content);
        headerAccessor.setSessionId(sessionId);

        // when
        chatWebSocketController.sendMessage(matchingId, headerAccessor, chatMessageRequestDto);

        // then
        verify(chatService, times(1)).send(eq(matchingId), eq(userEmail), eq(content));
    }

    @Test
    void updateLastReadMessage() throws Exception {
        // given
        String matchingId = "1";
        String sessionId = "session123";
        Principal mockPrincipal = mock(Principal.class);
        String userEmail = "email";
        String lastReadTime = "2023-01-01T12:00:00Z";
        SessionInformation sessionInformation = mock(SessionInformation.class);

        when(mockPrincipal.getName())
                .thenReturn(userEmail);
        when(sessionInformation.getPrincipal())
                .thenReturn(mockPrincipal);
        when(sessionRegistry.getSessionInformation(sessionId))
                .thenReturn(sessionInformation);

        LastReadTimeUpdateDto lastReadTimeUpdateDto = new LastReadTimeUpdateDto();
        lastReadTimeUpdateDto.setReadTime(lastReadTime);
        headerAccessor.setSessionId(sessionId);

        // when
        chatWebSocketController.updateLastReadMessage(matchingId, headerAccessor, lastReadTimeUpdateDto);

        // then
        verify(chatService, times(1)).updateLastReadTime(eq(matchingId), eq(userEmail), eq(lastReadTime));
    }
}