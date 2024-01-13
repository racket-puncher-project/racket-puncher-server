package com.example.demo.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.example.demo.auth.dto.*;
import com.example.demo.auth.service.KakaoOAuthService;
import com.example.demo.auth.service.PhoneAuthService;
import com.example.demo.entity.SiteUser;
import com.example.demo.auth.security.JwtAuthenticationFilter;
import com.example.demo.auth.security.SecurityConfiguration;
import com.example.demo.auth.security.TokenProvider;
import com.example.demo.auth.service.AuthService;
import com.example.demo.type.AgeGroup;
import com.example.demo.type.AuthType;
import com.example.demo.type.GenderType;
import com.example.demo.type.Ntrp;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
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
    private KakaoOAuthService kakaoOAuthService;

    @MockBean
    private PhoneAuthService phoneAuthService;

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
    void kakaoSignUp() throws Exception {
        // given
        given(authService.register(getKakaoSignUpDto()))
                .willReturn(SiteUser.fromDto(getKakaoSignUpDto()));

        // when
        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/sign-up"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    void kakaoFirstSignIn() throws Exception {
        // given
        KakaoFirstSignInResponseDto kakaoFirstSignInResponseDto = getKakaoFirstSignInResponseDto();
        String kakaoOauthTestCode = "kakaoOauthTestCode";
        given(kakaoOAuthService.processOauth(kakaoOauthTestCode))
                .willReturn(kakaoFirstSignInResponseDto);

        // when
        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/auth/kakao")
                        .param("code", kakaoOauthTestCode))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    void kakaoSignIn() throws Exception {
        // given
        KakaoSignInResponseDto kakaoSignInResponseDto = getKakaoSigninResponseDto();
        String kakaoOauthTestCode = "kakaoOauthTestCode";
        given(kakaoOAuthService.processOauth(kakaoOauthTestCode))
                .willReturn(kakaoSignInResponseDto);
        // when
        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/auth/kakao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(getKakaoCodeDto())))
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

    @Test
    public void withdraw() throws Exception {
        // given
        given(authService.withdraw("email", getPasswordRequestDto().getPassword()))
                .willReturn("탈퇴 성공");

        // when
        // then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/auth/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(getPasswordRequestDto())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    public void sendCode() throws Exception {
        // given
        given(phoneAuthService.sendCode("01012345678"))
                .willReturn(new StringResponseDto("sms 문자가 전송되었습니다."));
        // when
        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/phone/send-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(getPhoneNumberRequestDto())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    public void verifyCode() throws Exception {
        // given
        given(phoneAuthService.verifyCode("01012345678", "12345"))
                .willReturn(new StringResponseDto("휴대폰 번호가 성공적으로 인증되었습니다."));
        // when
        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/phone/verify-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(getAuthCodeRequestDto())))
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
                .authType(AuthType.GENERAL)
                .build();
    }

    private SignUpDto getKakaoSignUpDto() {
        return SignUpDto.builder()
                .email("email@naver.com")
                .nickname("닉네임")
                .password("")
                .phoneNumber("010-1234-5678")
                .gender(GenderType.FEMALE)
                .siteUserName("홍길동")
                .ntrp(Ntrp.BEGINNER)
                .address("삼성동")
                .zipCode("12345")
                .profileImg("test.url")
                .ageGroup(AgeGroup.TWENTIES)
                .authType(AuthType.KAKAO)
                .build();
    }

    private SignInDto getSignInDto() {
        return new SignInDto("email@nave.com", "`1234");
    }

    private KakaoFirstSignInResponseDto getKakaoFirstSignInResponseDto(){
        return KakaoFirstSignInResponseDto.builder()
                .registered(false)
                .email("email@test.com")
                .profileImageUrl("test.png")
                .nickname("nickname")
                .build();
    }

    private KakaoSignInResponseDto getKakaoSigninResponseDto(){
        return KakaoSignInResponseDto.builder()
                .registered(true)
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();
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

    private PasswordRequestDto getPasswordRequestDto() {
        return new PasswordRequestDto("password");
    }

    private PhoneNumberRequestDto getPhoneNumberRequestDto(){
        return new PhoneNumberRequestDto("01012345678");
    }

    private AuthCodeRequestDto getAuthCodeRequestDto(){
        return new AuthCodeRequestDto("01012345678", "12345");
    }

    private KakaoCodeDto getKakaoCodeDto(){
        return new KakaoCodeDto("kakaoCode");
    }
}