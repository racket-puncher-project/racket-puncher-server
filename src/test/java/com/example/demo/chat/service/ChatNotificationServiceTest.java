package com.example.demo.chat.service;

import com.example.demo.chat.dto.ChatMessageResponseDto;
import com.example.demo.chat.repository.ChatMessageRepository;
import com.example.demo.chat.repository.LastReadTimeId;
import com.example.demo.chat.repository.LastReadTimeRepository;
import com.example.demo.entity.ChatMessage;
import com.example.demo.entity.SiteUser;
import com.example.demo.siteuser.repository.SiteUserRepository;
import com.example.demo.type.AgeGroup;
import com.example.demo.type.GenderType;
import com.example.demo.type.Ntrp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ChatNotificationServiceTest {

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private SiteUserRepository siteUserRepository;

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private LastReadTimeRepository lastReadTimeRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private ChatNotificationService chatNotificationService;

    @Test
    void notifyUserConnection() {
        // given
        String matchingId = "1L";
        SiteUser siteUser = makeSiteUser();
        String userEmail = siteUser.getEmail();

        given(siteUserRepository.findByEmail(userEmail))
                .willReturn(Optional.of(siteUser));
        given(lastReadTimeRepository.existsById(any(LastReadTimeId.class)))
                .willReturn(false);
        given(applicationContext.getBean(SimpMessagingTemplate.class))
                .willReturn(messagingTemplate);

        // when
        chatNotificationService.notifyUserConnection(matchingId, userEmail);

        // then
        verify(messagingTemplate, times(1)).convertAndSend(eq("/topic/" + matchingId), any(ChatMessageResponseDto.class));
        verify(chatMessageRepository, times(1)).save(any(ChatMessage.class));
    }

    @Test
    void notifyChatRoomWillCloseSuccess() {
        // given
        String matchingId = "1L";

        given(applicationContext.getBean(SimpMessagingTemplate.class))
                .willReturn(messagingTemplate);

        // when
        chatNotificationService.notifyChatRoomWillClose(matchingId);

        // then
        verify(messagingTemplate, times(1)).convertAndSend(eq("/topic/" + matchingId), any(ChatMessageResponseDto.class));
        verify(chatMessageRepository, times(1)).save(any(ChatMessage.class));
    }

    private SiteUser makeSiteUser() {
        return SiteUser.builder()
                .id(1L)
                .email("emaill@naver.com")
                .password("password")
                .nickname("nickName")
                .siteUserName("userName")
                .phoneNumber("010-1234-5678")
                .mannerScore(3)
                .gender(GenderType.FEMALE)
                .ntrp(Ntrp.BEGINNER)
                .address("address")
                .zipCode("zipCode")
                .ageGroup(AgeGroup.TWENTIES)
                .profileImg("img.png")
                .createDate(LocalDateTime.now())
                .build();
    }
}
