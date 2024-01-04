package com.example.demo.auth.service;

import com.example.demo.auth.dto.KakaoSignInResponseDto;
import com.example.demo.auth.dto.KakaoTokenDto;
import com.example.demo.auth.dto.KakaoUserInfoDto;
import com.example.demo.auth.security.TokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class KakaoOAuthServiceTest {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    UriComponentsBuilder uriComponentsBuilder;

    @InjectMocks
    private KakaoOAuthService kakaoOAuthService;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(kakaoOAuthService, "tokenUrl", "http://test.com");
        ReflectionTestUtils.setField(kakaoOAuthService, "clientId", "testClientId");
        ReflectionTestUtils.setField(kakaoOAuthService, "redirectUri", "http://redirect.com");
        ReflectionTestUtils.setField(kakaoOAuthService, "userInfoUrl", "http://test.com");
    }

    @Test
    void getUserInfo() {
        // given
        String code = "kakaoOauthCode";
        KakaoTokenDto kakaoTokenDto = getKakaoTokenDto();
        KakaoUserInfoDto kakaoUserInfoDto = getKakaoUserInfoDto();

        given(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(KakaoTokenDto.class)))
                .willReturn(new ResponseEntity<>(kakaoTokenDto, HttpStatus.OK));
        given(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(KakaoUserInfoDto.class)))
                .willReturn(new ResponseEntity<>(kakaoUserInfoDto, HttpStatus.OK));

        // when
        KakaoUserInfoDto userInfo = kakaoOAuthService.getUserInfo(code);

        // then
        KakaoUserInfoDto.KakaoAccount expectedKakaoAccount = kakaoUserInfoDto.getKakaoAccount();
        KakaoUserInfoDto.KakaoAccount actualKakaoAccount = userInfo.getKakaoAccount();
        assertEquals(expectedKakaoAccount.getEmail(), actualKakaoAccount.getEmail());
        assertEquals(expectedKakaoAccount.getProfile().getNickname(), actualKakaoAccount.getProfile().getNickname());
        assertEquals(expectedKakaoAccount.getProfile().getProfileImageUrl(), actualKakaoAccount.getProfile().getProfileImageUrl());
    }

    @Test
    void kakaoSignInSuccess() {
        // given
        String email = "email@naver.com";
        String accessToken = "access_token";
        String refreshToken = "refresh_token";

        given(tokenProvider.generateAccessToken(email))
                .willReturn(accessToken);
        given(tokenProvider.generateAndSaveRefreshToken(email))
                .willReturn(refreshToken);

        // when
        KakaoSignInResponseDto responseDto = kakaoOAuthService.kakaoSignIn(email);

        // then
        assertTrue(responseDto.getRegistered());
        assertEquals(accessToken, responseDto.getAccessToken());
        assertEquals(refreshToken, responseDto.getRefreshToken());
    }

    private KakaoTokenDto getKakaoTokenDto(){
        return new KakaoTokenDto("access_token", "refresh_token");
    }

    private KakaoUserInfoDto getKakaoUserInfoDto() {
        KakaoUserInfoDto kakaoUserInfoDto = new KakaoUserInfoDto(null);
        KakaoUserInfoDto.KakaoAccount kakaoAccount = kakaoUserInfoDto.new KakaoAccount(null, "test@example.com");
        KakaoUserInfoDto.KakaoAccount.Profile profile = kakaoAccount.new Profile("nickname", "profileImageUrl");
        kakaoAccount.setProfile(profile);
        kakaoUserInfoDto.setKakaoAccount(kakaoAccount);
        return kakaoUserInfoDto;
    }
}