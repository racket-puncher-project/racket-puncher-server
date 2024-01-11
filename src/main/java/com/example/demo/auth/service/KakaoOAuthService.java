package com.example.demo.auth.service;

import com.example.demo.auth.dto.*;

import com.example.demo.auth.security.TokenProvider;
import com.example.demo.common.ResponseUtil;
import com.example.demo.exception.RacketPuncherException;
import com.example.demo.notification.service.NotificationService;
import com.example.demo.siteuser.repository.SiteUserRepository;
import com.example.demo.type.AuthType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static com.example.demo.exception.type.ErrorCode.EMAIL_NOT_FOUND;
import static com.example.demo.exception.type.ErrorCode.KAKAO_ACCESS_TOKEN_FAIL;
import static com.example.demo.exception.type.ErrorCode.KAKAO_USER_INFO_FAIL;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoOAuthService {
    private final RestTemplate restTemplate;
    private final TokenProvider tokenProvider;
    private final SiteUserRepository siteUserRepository;
    private final NotificationService notificationService;

    @Value("${kakao.client_id}")
    private String clientId;
    @Value("${kakao.redirect_uri}")
    private String redirectUri;
    @Value("${kakao.token_url}")
    private String tokenUrl;
    @Value("${kakao.user_info_url}")
    private String userInfoUrl;

    public KakaoSignIn processOauth(String code) {
        String kakaoAccessToken = getKakaoAccessToken(code);
        KakaoUserInfoDto kakaoUserInfoDto = getKakaoUserInfo(kakaoAccessToken);
        String email = kakaoUserInfoDto.getKakaoAccount().getEmail();
        boolean isAlreadyRegistered = siteUserRepository.existsByEmail(email);
        if (isAlreadyRegistered) {
            return kakaoSignIn(email);
        }

        return KakaoFirstSignInResponseDto.fromKakaoUserInfo(kakaoUserInfoDto);
    }

    private String getKakaoAccessToken(String code) {
        // 카카오 엑세스 토큰 요청
        ResponseEntity<KakaoTokenDto> response = restTemplate.exchange(
                buildTokenUri(code),
                HttpMethod.POST,
                null,
                KakaoTokenDto.class
        );

        // 응답 예외처리
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody().getAccessToken();
        } else {
            throw new RacketPuncherException(KAKAO_ACCESS_TOKEN_FAIL);
        }
    }

    // 카카오에게 엑세스 토큰 발급 요청하는 URI 생성
    private String buildTokenUri(String code) {
        return UriComponentsBuilder.fromHttpUrl(tokenUrl)
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("code", code)
                .toUriString();
    }

    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) {
        // 카카오 엑세스 토큰을 헤더에 담은 HttpEntity
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        // 사용자의 정보 요청
        ResponseEntity<KakaoUserInfoDto> response = restTemplate.exchange(
                userInfoUrl,
                HttpMethod.GET,
                entity,
                KakaoUserInfoDto.class
        );

        // 응답 예외처리
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new RacketPuncherException(KAKAO_USER_INFO_FAIL);
        }
    }

    private KakaoSignInResponseDto kakaoSignIn(String email) {
        var siteUserId = siteUserRepository.findByEmail(email)
                .orElseThrow(() -> new RacketPuncherException(EMAIL_NOT_FOUND)).getId();

        var accessToken = tokenProvider.generateAccessToken(email);
        var refreshToken = tokenProvider.generateAndSaveRefreshToken(email);
        return KakaoSignInResponseDto.builder()
                .registered(true)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .authType(AuthType.KAKAO)
                .build();
    }
}