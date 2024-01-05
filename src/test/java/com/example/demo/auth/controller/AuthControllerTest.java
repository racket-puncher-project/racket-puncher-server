package com.example.demo.auth.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.example.demo.entity.SiteUser;
import com.example.demo.auth.dto.AccessTokenDto;
import com.example.demo.auth.dto.EmailRequestDto;
import com.example.demo.auth.dto.NicknameRequestDto;
import com.example.demo.auth.dto.SignInDto;
import com.example.demo.auth.dto.SignUpDto;
import com.example.demo.auth.dto.StringResponseDto;
import com.example.demo.auth.security.JwtAuthenticationFilter;
import com.example.demo.auth.security.SecurityConfiguration;
import com.example.demo.auth.security.TokenProvider;
import com.example.demo.auth.service.AuthService;
import com.example.demo.type.AgeGroup;
import com.example.demo.type.GenderType;
import com.example.demo.type.Ntrp;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(AuthController.class)
@Import(SecurityConfiguration.class)
class AuthControllerTest {

    @MockBean
    private RedisTemplate<String, String> redisTemplate;

    @MockBean
    private AuthService authService;

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void signUp() throws Exception {
        // given
        given(authService.register(getSignUpDto()))
                .willReturn(SiteUser.fromDto(getSignUpDto()));

        // when
        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/sign-up"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    void signIn() throws Exception {
        // given
        given(authService.authenticate(getSignInDto()))
                .willReturn(SiteUser.fromDto(getSignUpDto()));

        // when
        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/sign-in"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    public void reissue() throws Exception {
        // given
       given(authService.tokenReissue(getAccessTokenDto()))
               .willReturn(new AccessTokenDto("newAccessToken"));

        // when
        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/reissue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(getAccessTokenDto())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    public void signOut() throws Exception {
        // given
        given(authService.signOut(getAccessTokenDto()))
                .willReturn("로그아웃 성공");

        // when
        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/sign-out")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(getAccessTokenDto())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    public void checkEmail() throws Exception {
        // given
        given(authService.checkEmail(getEmailRequestDto().getEmail()))
                .willReturn(new StringResponseDto("사용 가능한 이메일입니다."));

        // when
        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/check-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(getEmailRequestDto())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    public void checkNickname() throws Exception {
        // given
        given(authService.checkNickname(getNicknameRequestDto().getNickname()))
                .willReturn(new StringResponseDto("사용 가능한 닉네임입니다."));

        // when
        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/check-nickname")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(getNicknameRequestDto())))
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

    private SignInDto getSignInDto() {
        return new SignInDto("email@nave.com", "`1234");
    }

    private AccessTokenDto getAccessTokenDto() {
        return new AccessTokenDto("accessToken");
    }
    private EmailRequestDto getEmailRequestDto() {
        return new EmailRequestDto("email");
    }

    private NicknameRequestDto getNicknameRequestDto() {
        return new NicknameRequestDto("nickname");
    }
}