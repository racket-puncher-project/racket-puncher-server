package com.example.demo.chat.dto;

import com.example.demo.entity.ChatMessage;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponseDto {
    private String content;
    private String senderNickname;
    private String senderProfileImg;
    private String sentTime;

    public static ChatMessageResponseDto fromEntity(ChatMessage chatMessage, String senderNickname, String senderProfileImg){
        return ChatMessageResponseDto.builder()
                .content(chatMessage.getContent())
                .senderNickname(senderNickname)
                .senderProfileImg(senderProfileImg)
                .sentTime(chatMessage.getTime())
                .build();
    }
}