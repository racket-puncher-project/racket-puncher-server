package com.example.demo.chat.controller;


import com.example.demo.chat.dto.ChatMessageResponseDto;
import com.example.demo.chat.dto.ChatRoomDto;
import com.example.demo.chat.service.ChatService;
import com.example.demo.common.ResponseDto;
import com.example.demo.common.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/previous/{matchingId}")
    public ResponseDto<List<ChatMessageResponseDto>> getPreviousMessages(@PathVariable String matchingId) {
        var result = chatService.getPreviousMessages(matchingId);
        return ResponseUtil.SUCCESS(result);
    }

    @GetMapping("/list")
    public ResponseDto<List<ChatRoomDto>> getChatRoomList(Principal principal) {
        var result = chatService.getChatRoomList(principal.getName());
        return ResponseUtil.SUCCESS(result);
    }
}
