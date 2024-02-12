package com.example.demo.chat.controller;

import com.example.demo.auth.security.JwtAuthenticationFilter;
import com.example.demo.auth.security.SecurityConfiguration;
import com.example.demo.auth.security.TokenProvider;
import com.example.demo.chat.dto.ChatMessageResponseDto;
import com.example.demo.chat.dto.ChatRoomDto;
import com.example.demo.chat.service.ChatService;
import com.example.demo.siteuser.dto.SiteUserInfoForListDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(ChatController.class)
@Import(SecurityConfiguration.class)
class ChatControllerTest {

    @MockBean
    private ChatService chatService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void getPrevious() throws Exception {
        // given
        String matchingId = "1";
        List<ChatMessageResponseDto> chatMessageResponseDto = List.of(makeChatMessageResponseDto());

        given(chatService.getPreviousMessages(matchingId))
                .willReturn(chatMessageResponseDto);

        // when
        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/chat/previous/" + matchingId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "test@test.com")
    void getChatRoomList() throws Exception {
        // given
        List<ChatRoomDto> chatRoomDtoList = List.of(makeChatRoomDto());

        given(chatService.getChatRoomList("test@test.com"))
                .willReturn(chatRoomDtoList);

        // when
        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/list"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    private ChatMessageResponseDto makeChatMessageResponseDto() {
        return ChatMessageResponseDto.builder()
                .sentTime("time")
                .senderProfileImg("profileImg")
                .senderNickname("nickname")
                .content("content")
                .build();
    }

    private ChatRoomDto makeChatRoomDto() {
        return ChatRoomDto.builder()
                .matchingId(1L)
                .locationImg("locationImg")
                .newMessageNum(0L)
                .participants(List.of(new SiteUserInfoForListDto()))
                .title("title")
                .build();
    }
}