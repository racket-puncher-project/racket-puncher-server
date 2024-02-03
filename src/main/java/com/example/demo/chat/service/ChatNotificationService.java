package com.example.demo.chat.service;

import com.example.demo.chat.dto.ChatMessageResponse;
import com.example.demo.entity.SiteUser;
import com.example.demo.exception.RacketPuncherException;
import com.example.demo.siteuser.repository.SiteUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.example.demo.exception.type.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ChatNotificationService {
    private final ApplicationContext applicationContext;
    private final SiteUserRepository siteUserRepository;

    public void notifyUserConnection(String userEmail) {
        SiteUser siteUser = siteUserRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RacketPuncherException(USER_NOT_FOUND));

        String connectMessage = String.format("%s가 입장했습니다. (%s)",
                siteUser.getNickname(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm").format(LocalDateTime.now()));
        SimpMessagingTemplate messagingTemplate = applicationContext.getBean(SimpMessagingTemplate.class);
        ChatMessageResponse chatMessageResponse = ChatMessageResponse.builder()
                .content(connectMessage)
                .senderNickname("admin")
                .build();

        messagingTemplate.convertAndSend("/topic/messages", chatMessageResponse);
    }
}
