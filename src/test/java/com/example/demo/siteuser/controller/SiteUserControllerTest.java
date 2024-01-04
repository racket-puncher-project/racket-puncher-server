package com.example.demo.siteuser.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.example.demo.auth.security.JwtAuthenticationFilter;
import com.example.demo.auth.security.SecurityConfiguration;
import com.example.demo.auth.security.TokenProvider;
import com.example.demo.aws.S3Uploader;
import com.example.demo.siteuser.dto.SiteUserInfoDto;
import com.example.demo.siteuser.service.SiteUserService;
import com.example.demo.type.AgeGroup;
import com.example.demo.type.GenderType;
import com.example.demo.type.Ntrp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(SiteUserController.class)
@Import(SecurityConfiguration.class)
public class SiteUserControllerTest {
    @MockBean
    private SiteUserService siteUserService;

    @MockBean
    private S3Uploader s3Uploader;

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getSiteUserInfo() throws Exception {
        // given
        given(siteUserService.getSiteUserInfo(1L))
                .willReturn(getSiteUserInfoDto());

        // when
        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/profile/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    private SiteUserInfoDto getSiteUserInfoDto() {
        return SiteUserInfoDto.builder()
                .profileImg("img.png")
                .siteUserName("userName")
                .nickname("nickName")
                .address("address")
                .zipCode("zipCode")
                .ntrp(Ntrp.BEGINNER)
                .gender(GenderType.FEMALE)
                .mannerScore(3)
                .ageGroup(AgeGroup.TWENTIES)
                .build();
    }

}
