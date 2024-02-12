package com.example.demo.auth.security;

import com.example.demo.apply.repository.ApplyRepository;
import com.example.demo.chat.service.ChatNotificationService;
import com.example.demo.entity.Apply;
import com.example.demo.entity.SiteUser;
import com.example.demo.exception.RacketPuncherException;
import com.example.demo.siteuser.repository.SiteUserRepository;
import com.example.demo.type.ApplyStatus;
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

import static com.example.demo.exception.type.ErrorCode.USER_NOT_ACCEPTED_AT_MATCHING;
import static com.example.demo.exception.type.ErrorCode.USER_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class StompAuthChannelInterceptor implements ChannelInterceptor {

    private final TokenProvider tokenProvider;
    private final ChatNotificationService chatNotificationService;
    private final SessionRegistry sessionRegistry;
    private final SiteUserRepository siteUserRepository;
    private final ApplyRepository applyRepository;

    @Override
    public Message<?> preSend(@NotNull Message<?> message, @NotNull MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        assert accessor != null;

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authToken = Objects.requireNonNull(accessor.getFirstNativeHeader("Authorization")).substring("Bearer ".length());
            Authentication auth = validateAccessToken(authToken);
            String sessionId = accessor.getSessionId();
            sessionRegistry.registerNewSession(sessionId, auth);

            String connectType = Objects.requireNonNull(accessor.getFirstNativeHeader("connectType"));
            if (connectType.equals("room")) {
                String matchingId = Objects.requireNonNull(accessor.getFirstNativeHeader("matchingId"));
                validateUserAccepted(auth.getName(), matchingId);
                chatNotificationService.notifyUserConnection(matchingId, auth.getName());
            }
        }
        return message;
    }

    private Authentication validateAccessToken(String authToken) {
        try {
            tokenProvider.validateToken(authToken);
            Authentication auth = tokenProvider.getAuthentication(authToken);
            SecurityContextHolder.getContext().setAuthentication(auth);
            return auth;
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationException("Invalid Token") {
            };
        }
    }

    private void validateUserAccepted(String email, String matchingId) {
        SiteUser siteUser = siteUserRepository.findByEmail(email)
                .orElseThrow(() -> new RacketPuncherException(USER_NOT_FOUND));
        Apply apply = applyRepository.findBySiteUser_IdAndMatching_Id(siteUser.getId(), Long.parseLong(matchingId))
                .orElseThrow(() -> new RacketPuncherException(USER_NOT_ACCEPTED_AT_MATCHING));
        if (!apply.getApplyStatus().equals(ApplyStatus.ACCEPTED)) {
            throw new RacketPuncherException(USER_NOT_ACCEPTED_AT_MATCHING);
        }
    }
}
