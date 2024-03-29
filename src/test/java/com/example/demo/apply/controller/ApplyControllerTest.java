package com.example.demo.apply.controller;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.example.demo.apply.service.ApplyService;
import com.example.demo.entity.Apply;
import com.example.demo.auth.security.JwtAuthenticationFilter;
import com.example.demo.auth.security.SecurityConfiguration;
import com.example.demo.auth.security.TokenProvider;
import com.example.demo.type.ApplyStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(ApplyController.class)
@Import(SecurityConfiguration.class)
class ApplyControllerTest {
    @MockBean
    private ApplyService applyService;

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void apply() throws Exception {
        // given
        given(applyService.apply(anyString(), anyLong()))
                .willReturn(Apply.builder()
                        .applyStatus(ApplyStatus.PENDING)
                        .build());
        // when
        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/apply/matches/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    void cancelApply() throws Exception {
        // given
        given(applyService.cancel(anyLong()))
                .willReturn(Apply.builder()
                        .applyStatus(ApplyStatus.CANCELED)
                        .build());
        // when
        // then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/apply/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    void acceptApply() throws Exception {
        // given
        String request = "{\n"
                + "\"appliedList\": [1,2],\n"
                + "\"confirmedList\": [3,4]\n"
                + "}";

        // when
        // then
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/apply/matches/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }
}