package com.example.demo.chat.service;

import com.example.demo.apply.repository.ApplyRepository;
import com.example.demo.chat.dto.ChatMessageResponseDto;
import com.example.demo.chat.dto.ChatRoomDto;
import com.example.demo.chat.dto.NewMessageArrivedDto;
import com.example.demo.chat.repository.ChatMessageId;
import com.example.demo.chat.repository.ChatMessageRepository;
import com.example.demo.chat.repository.LastReadTimeId;
import com.example.demo.chat.repository.LastReadTimeRepository;
import com.example.demo.entity.Apply;
import com.example.demo.entity.ChatMessage;
import com.example.demo.entity.LastReadTime;
import com.example.demo.entity.SiteUser;
import com.example.demo.exception.RacketPuncherException;
import com.example.demo.siteuser.dto.SiteUserInfoForListDto;
import com.example.demo.siteuser.repository.SiteUserRepository;
import com.example.demo.type.ApplyStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.exception.type.ErrorCode.USER_NOT_FOUND;
import static com.example.demo.util.dateformatter.DateFormatter.formForChatSentTime;
import static com.example.demo.util.dateformatter.DateFormatter.formForDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {
    private final ApplicationContext applicationContext;
    private final SiteUserRepository siteUserRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final LastReadTimeRepository lastReadTimeRepository;
    private final ApplyRepository applyRepository;

    public void send(String matchingId, String userEmail, String content) {
        SiteUser siteUser = siteUserRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RacketPuncherException(USER_NOT_FOUND));

        String sentTime = formForChatSentTime.format(LocalDateTime.now());

        ChatMessageResponseDto chatMessageResponseDto = ChatMessageResponseDto.builder()
                .content(content)
                .senderNickname(siteUser.getNickname())
                .senderProfileImg(siteUser.getProfileImg())
                .sentTime(sentTime)
                .build();

        ChatMessage chatMessage = ChatMessage.builder()
                .id(new ChatMessageId(matchingId, sentTime))
                .senderId(siteUser.getId().toString())
                .content(content)
                .build();

        SimpMessagingTemplate messagingTemplate = applicationContext.getBean(SimpMessagingTemplate.class);
        messagingTemplate.convertAndSend("/topic/" + matchingId, chatMessageResponseDto);
        chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessageResponseDto> getPreviousMessages(String matchingId) {
        return chatMessageRepository.findAllByMatchingId(matchingId).stream()
                .map(cm -> {
                    if (cm.getSenderId().equals("admin")) {
                        return ChatMessageResponseDto.fromEntity(cm, cm.getSenderId(), "");
                    }
                    SiteUser siteUser = siteUserRepository.findById(Long.valueOf(cm.getSenderId()))
                            .orElseThrow(() -> new RacketPuncherException(USER_NOT_FOUND));
                    return ChatMessageResponseDto.fromEntity(cm, siteUser.getNickname(), siteUser.getProfileImg());
                }).toList();
    }

    public void updateLastReadTime(String matchingId, String userEmail, String readTime) {
        SiteUser siteUser = siteUserRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RacketPuncherException(USER_NOT_FOUND));

        DateTimeFormatter originalFormatter = DateTimeFormatter.ofPattern("yyyy. MM. dd. HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(readTime, originalFormatter);
        String formattedString = dateTime.format(formForDateTime);

        LastReadTime lastReadTime = LastReadTime.builder()
                .id(new LastReadTimeId(matchingId, String.valueOf(siteUser.getId())))
                .time(formattedString)
                .build();
        lastReadTimeRepository.save(lastReadTime);
    }

    public List<ChatRoomDto> getChatRoomList(String userEmail) {
        SiteUser siteUser = siteUserRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RacketPuncherException(USER_NOT_FOUND));
        LocalDateTime now = LocalDateTime.now();

        return applyRepository.findAllBySiteUser_Email(siteUser.getEmail()).stream()
                .filter(apply -> apply.getApplyStatus() == ApplyStatus.ACCEPTED)
                .map(Apply::getMatching) // 신청한 것 중 ACCEPTED 된 매칭
                .filter(matching -> matching.getRecruitDueDateTime().isBefore(now)
                        && matching.getDate().atTime(matching.getEndTime().plusHours(24)).isAfter(LocalDateTime.from(now)))
                .distinct() // 마감 시간이 지나고, 종료 시간 이후 24시간이 지나지 않은 매칭
                .map(matching -> {
                    List<SiteUserInfoForListDto> acceptedUsers = applyRepository.findAllByMatching_IdAndApplyStatus(matching.getId(), ApplyStatus.ACCEPTED).stream()
                            .map(apply -> SiteUserInfoForListDto.fromEntity(apply.getSiteUser()))
                            .toList(); // 그 매칭에 ACCEPTED 된 사용자들
                    LastReadTime lastReadTime = lastReadTimeRepository.findById(new LastReadTimeId(String.valueOf(matching.getId()), String.valueOf(siteUser.getId())))
                            .orElse(null);

                    if (lastReadTime == null) {
                        long newMessageNum = chatMessageRepository.countByMatchingId(String.valueOf(matching.getId()));
                        return ChatRoomDto.makeChatRoomDto(matching, newMessageNum, acceptedUsers);
                    } // 본 기록이 없으면 전체 갯수 리턴

                    LocalDateTime lastReadDateTime = LocalDateTime.parse(lastReadTime.getTime(), formForDateTime);
                    List<ChatMessage> messages = chatMessageRepository.findAllByMatchingId(String.valueOf(matching.getId()));
                    long newMessageNum = messages.stream()
                            .map(message -> LocalDateTime.parse(message.getTime(), formForDateTime))
                            .filter(messageDateTime -> messageDateTime.isAfter(lastReadDateTime))
                            .count();
                    return ChatRoomDto.makeChatRoomDto(matching, newMessageNum, acceptedUsers);
                })
                .collect(Collectors.toList());
    }

    public void updateNewMessageNum(String matchingId) {
        SimpMessagingTemplate messagingTemplate = applicationContext.getBean(SimpMessagingTemplate.class);
        messagingTemplate.convertAndSend("/topic/newMessageArrived", new NewMessageArrivedDto(matchingId));
    }
}
