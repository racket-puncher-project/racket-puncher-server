package com.example.demo.chat.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponse {
    private String content;
    private String senderNickname;
    private String senderProfileImg;
    private String sentTime;
}