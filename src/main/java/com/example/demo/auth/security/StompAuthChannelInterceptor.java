package com.example.demo.auth.security;

import com.example.demo.chat.service.ChatNotificationService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class StompAuthChannelInterceptor implements ChannelInterceptor {

    private final TokenProvider tokenProvider;
    private final ChatNotificationService chatNotificationService;
    private final SessionRegistry sessionRegistry;

    @Override
    public Message<?> preSend(@NotNull Message<?> message, @NotNull MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        assert accessor != null;

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authToken = Objects.requireNonNull(accessor.getFirstNativeHeader("Authorization")).substring("Bearer ".length());
            Authentication auth = validateAccessToken(authToken);
            chatNotificationService.notifyUserConnection(auth.getName()); //TODO : 최초 한번만 입장해야 알림 가도록 수정해야함
            String sessionId = accessor.getSessionId();
            sessionRegistry.registerNewSession(sessionId, auth); // 웹소켓 세션 id와 함께 인증 정보 저장
        }
        return message;
    }

    private Authentication validateAccessToken(String authToken){
        try {
            tokenProvider.validateToken(authToken);
            Authentication auth = tokenProvider.getAuthentication(authToken);
            SecurityContextHolder.getContext().setAuthentication(auth);
            return auth;
        } catch (Exception e){
            e.printStackTrace();
            throw new AuthenticationException("Invalid Token"){};
        }
    }
}
