package com.example.demo.chat.service;

import com.example.demo.chat.dto.ChatMessageResponseDto;
import com.example.demo.chat.repository.ChatMessageId;
import com.example.demo.chat.repository.ChatMessageRepository;
import com.example.demo.chat.repository.LastReadTimeId;
import com.example.demo.chat.repository.LastReadTimeRepository;
import com.example.demo.entity.ChatMessage;
import com.example.demo.entity.SiteUser;
import com.example.demo.exception.RacketPuncherException;
import com.example.demo.siteuser.repository.SiteUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.example.demo.exception.type.ErrorCode.USER_NOT_FOUND;
import static com.example.demo.util.dateformatter.DateFormatter.formForDateTime;

@Service
@RequiredArgsConstructor
public class ChatNotificationService {
    private final ApplicationContext applicationContext;
    private final SiteUserRepository siteUserRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final LastReadTimeRepository lastReadTimeRepository;

    public void notifyUserConnection(String matchingId, String userEmail) {
        SiteUser siteUser = siteUserRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RacketPuncherException(USER_NOT_FOUND));
        boolean visited = lastReadTimeRepository.existsById(new LastReadTimeId(matchingId, String.valueOf(siteUser.getId())));

        if (!visited) {
            String notification = String.format("%s님이 입장했습니다.", siteUser.getNickname());
            sendNotificationChat(matchingId, notification);
        }
    }

    public void notifyChatRoomWillClose(String matchingId) {
        String notification = "매칭이 종료되었으므로, 24시간 후 채팅방이 비활성화됩니다.";
        sendNotificationChat(matchingId, notification);
    }

    private void sendNotificationChat(String matchingId, String notification){
        String sentTime = formForDateTime.format(LocalDateTime.now());

        ChatMessageResponseDto chatMessageResponseDto = ChatMessageResponseDto.builder()
                .content(notification)
                .senderNickname("admin")
                .sentTime(sentTime)
                .build();

        ChatMessage chatMessage = ChatMessage.builder()
                .id(new ChatMessageId(matchingId, sentTime))
                .senderId("admin")
                .content(notification)
                .build();

        SimpMessagingTemplate messagingTemplate = applicationContext.getBean(SimpMessagingTemplate.class);
        messagingTemplate.convertAndSend("/topic/" + matchingId, chatMessageResponseDto);
        chatMessageRepository.save(chatMessage);
    }
}
