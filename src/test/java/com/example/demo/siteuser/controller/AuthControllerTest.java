package com.example.demo.siteuser.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.example.demo.entity.SiteUser;
import com.example.demo.oauth2.service.ProviderService;
import com.example.demo.siteuser.dto.SignUpDto;
import com.example.demo.siteuser.repository.SiteUserRepository;
import com.example.demo.siteuser.security.CustomAuthFailureHandler;
import com.example.demo.siteuser.security.JwtAuthenticationFilter;
import com.example.demo.siteuser.security.SecurityConfiguration;
import com.example.demo.siteuser.security.TokenProvider;
import com.example.demo.siteuser.service.MemberService;
import com.example.demo.type.AgeGroup;
import com.example.demo.type.GenderType;
import com.example.demo.type.Ntrp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(AuthController.class)
@Import(SecurityConfiguration.class)
class AuthControllerTest {
    @MockBean
    private SiteUserRepository siteUserRepository;

    @MockBean
    private RedisTemplate<String, String> redisTemplate;

    @MockBean
    private MemberService memberService;

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private CustomAuthFailureHandler customAuthFailureHandler;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private ProviderService providerService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void signUp() throws Exception {
        // given
        given(memberService.register(getSignUpDto()))
                .willReturn(SiteUser.fromDto(getSignUpDto()));

        // when
        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/sign-up"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    private SignUpDto getSignUpDto() {
        return SignUpDto.builder()
                .email("email@naver.com")
                .password("1234")
                .nickname("닉네임")
                .phoneNumber("010-1234-5678")
                .gender(GenderType.FEMALE)
                .siteUserName("홍길동")
                .ntrp(Ntrp.BEGINNER)
                .address("삼성동")
                .zipCode("12345")
                .profileImg("test.url")
                .ageGroup(AgeGroup.TWENTIES)
                .build();
    }
}