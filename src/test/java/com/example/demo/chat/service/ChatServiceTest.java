package com.example.demo.chat.service;

import com.example.demo.apply.repository.ApplyRepository;
import com.example.demo.chat.dto.ChatMessageResponseDto;
import com.example.demo.chat.dto.ChatRoomDto;
import com.example.demo.chat.repository.ChatMessageId;
import com.example.demo.chat.repository.ChatMessageRepository;
import com.example.demo.chat.repository.LastReadTimeRepository;
import com.example.demo.entity.*;
import com.example.demo.siteuser.repository.SiteUserRepository;
import com.example.demo.type.AgeGroup;
import com.example.demo.type.ApplyStatus;
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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    private final DateTimeFormatter formForDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private SiteUserRepository siteUserRepository;

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private LastReadTimeRepository lastReadTimeRepository;

    @Mock
    private ApplyRepository applyRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private ChatService chatService;

    @Test
    void send() {
        // given
        String matchingId = "matchingId";
        SiteUser siteUser = makeSiteUser();
        String userEmail = "emaill@naver.com";
        String content = "content";

        given(applicationContext.getBean(SimpMessagingTemplate.class)).willReturn(messagingTemplate);
        given(siteUserRepository.findByEmail(userEmail)).willReturn(Optional.of(siteUser));

        // when
        chatService.send(matchingId, userEmail, content);

        // then
        verify(siteUserRepository, times(1)).findByEmail(userEmail);
        verify(messagingTemplate, times(1)).convertAndSend((String) eq("/topic/" + matchingId), (Object) any());
        verify(chatMessageRepository, times(1)).save(any(ChatMessage.class));
    }

    @Test
    void getPreviousMessages() {
        // given
        String matchingId = "matchingId";
        List<ChatMessage> chatMessages = List.of(makeChatMessage());
        SiteUser siteUser = makeSiteUser();

        given(chatMessageRepository.findAllByMatchingId(matchingId)).willReturn(chatMessages);
        given(siteUserRepository.findById(siteUser.getId())).willReturn(Optional.of(siteUser));

        // when
        List<ChatMessageResponseDto> result = chatService.getPreviousMessages(matchingId);

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(chatMessages.size());
    }

    @Test
    void updateLastReadTime() {
        // given
        String matchingId = "1";
        String userEmail = "user@example.com";
        String readTime = "2024. 03. 01. 12:00:00";
        SiteUser siteUser = new SiteUser();
        siteUser.setId(1L);
        siteUser.setEmail(userEmail);

        given(siteUserRepository.findByEmail(userEmail)).willReturn(Optional.of(siteUser));

        // when
        chatService.updateLastReadTime(matchingId, userEmail, readTime);

        // then
        verify(lastReadTimeRepository, times(1)).save(any(LastReadTime.class));
    }

    @Test
    void getChatRoomList() {
        // given
        SiteUser siteUser = makeSiteUser();
        String userEmail = siteUser.getEmail();

        LocalDateTime now = LocalDateTime.now();
        Apply myApply = makeAcceptedApply1();
        Apply otherApply = makeAcceptedApply2();
        List<Apply> acceptedApplies = List.of(myApply);

        given(siteUserRepository.findByEmail(userEmail)).willReturn(Optional.of(siteUser));
        given(applyRepository.findAllBySiteUser_Email(userEmail)).willReturn(acceptedApplies);
        given(applyRepository.findAllByMatching_IdAndApplyStatus(myApply.getMatching().getId(), ApplyStatus.ACCEPTED)).willReturn(List.of(otherApply));
        given(lastReadTimeRepository.findById(any())).willReturn(Optional.empty());
        given(chatMessageRepository.countByMatchingId(any())).willReturn(0);

        // when
        List<ChatRoomDto> result = chatService.getChatRoomList(userEmail);

        // then
        assertThat(result).isNotEmpty();
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

    private ChatMessage makeChatMessage() {
        return ChatMessage.builder()
                .id(new ChatMessageId("1", "sentTime"))
                .senderId("1")
                .content("content")
                .build();
    }

    private Apply makeAcceptedApply1() {
        return Apply.builder()
                .id(1L)
                .matching(Matching.builder()
                        .id(1L)
                        .recruitDueDateTime(LocalDateTime.now().minusHours(1))
                        .endTime(LocalTime.now().plusHours(1))
                        .build())
                .siteUser(SiteUser.builder()
                        .id(1L)
                        .email("emaill@naver.com")
                        .nickname("nickName1")
                        .profileImg("img.png")
                        .build())
                .applyStatus(ApplyStatus.ACCEPTED)
                .build();
    }

    private Apply makeAcceptedApply2() {
        return Apply.builder()
                .id(1L)
                .matching(Matching.builder()
                        .id(1L)
                        .recruitDueDateTime(LocalDateTime.now().minusHours(1))
                        .endTime(LocalTime.now().plusHours(1))
                        .build())
                .siteUser(SiteUser.builder()
                        .id(1L)
                        .email("email2@naver.com")
                        .nickname("nickName2")
                        .profileImg("img.png")
                        .build())
                .applyStatus(ApplyStatus.ACCEPTED)
                .build();
    }
}
