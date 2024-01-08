package com.example.demo.auth.service;

import com.example.demo.auth.dto.*;
import com.example.demo.auth.security.TokenProvider;
import com.example.demo.siteuser.repository.SiteUserRepository;
import com.example.demo.type.AuthType;
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
    private SiteUserRepository siteUserRepository;

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
    void kakaoFirstSignIn() {
        // given
        String kakaoOauthTestCode = "kakaoOauthTestCode";
        KakaoTokenDto kakaoTokenDto = getKakaoTokenDto();
        KakaoUserInfoDto kakaoUserInfoDto = getKakaoUserInfoDto();

        given(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(KakaoTokenDto.class)))
                .willReturn(new ResponseEntity<>(kakaoTokenDto, HttpStatus.OK));
        given(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(KakaoUserInfoDto.class)))
                .willReturn(new ResponseEntity<>(kakaoUserInfoDto, HttpStatus.OK));
        given(siteUserRepository.existsByEmail(any()))
                .willReturn(false);

        // when
        KakaoFirstSignInResponseDto actualKakaoUser = (KakaoFirstSignInResponseDto) kakaoOAuthService.processOauth(kakaoOauthTestCode);

        // then
        assertFalse(actualKakaoUser.getRegistered());
        assertEquals(actualKakaoUser.getEmail(), kakaoUserInfoDto.getKakaoAccount().getEmail());
        assertEquals(actualKakaoUser.getNickname(), kakaoUserInfoDto.getKakaoAccount().getProfile().getNickname());
        assertEquals(actualKakaoUser.getProfileImageUrl(), kakaoUserInfoDto.getKakaoAccount().getProfile().getProfileImageUrl());
    }

    @Test
    void kakaoSignIn() {
        // given
        String kakaoOauthTestCode = "kakaoOauthTestCode";
        String testEmail = "testEmail";
        String testAccessToken = "testAccessToken";
        String testRefreshToken = "testRefreshToken";
        KakaoTokenDto kakaoTokenDto = getKakaoTokenDto();
        KakaoUserInfoDto kakaoUserInfoDto = getKakaoUserInfoDto();

        given(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(KakaoTokenDto.class)))
                .willReturn(new ResponseEntity<>(kakaoTokenDto, HttpStatus.OK));
        given(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(KakaoUserInfoDto.class)))
                .willReturn(new ResponseEntity<>(kakaoUserInfoDto, HttpStatus.OK));
        given(siteUserRepository.existsByEmail(testEmail))
                .willReturn(true);
        given(tokenProvider.generateAccessToken(testEmail))
                .willReturn(testAccessToken);
        given(tokenProvider.generateAndSaveRefreshToken(testEmail))
                .willReturn(testRefreshToken);

        // when
        KakaoSignInResponseDto actualKakaoUser = (KakaoSignInResponseDto) kakaoOAuthService.processOauth(kakaoOauthTestCode);

        // then
        assertTrue(actualKakaoUser.getRegistered());
        assertEquals(actualKakaoUser.getAccessToken(), testAccessToken);
        assertEquals(actualKakaoUser.getRefreshToken(), testRefreshToken);
        assertEquals(actualKakaoUser.getAuthType(), AuthType.KAKAO);
    }

    private KakaoTokenDto getKakaoTokenDto(){
        return new KakaoTokenDto("accessToken", "refreshToken");
    }

    private KakaoUserInfoDto getKakaoUserInfoDto() {
        KakaoProfile kakaoProfile = new KakaoProfile("nickname", "profileImageUrl");
        KakaoAccount kakaoAccount = new KakaoAccount(kakaoProfile, "testEmail");
        return new KakaoUserInfoDto(kakaoAccount);
    }
}