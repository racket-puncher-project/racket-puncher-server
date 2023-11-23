package com.example.demo.oauth2;

import com.example.demo.oauth2.dto.OAuthRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;

@Component
@RequiredArgsConstructor
public class OAuthRequestFactory {
    private final KakaoInfo kakaoInfo;

    public OAuthRequest getRequest(String code, String provider) {
        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        if (!provider.equals("kakao")) {
            return null;
        }
        map.add("grant_type", "authorization_code");
        map.add("client_id", kakaoInfo.getKakaoClientId());
        map.add("redirect_uri", kakaoInfo.getKakaoRedirect());
        map.add("client_secret", kakaoInfo.getKakacoClientSecret());
        map.add("code", code);

        return new OAuthRequest(kakaoInfo.getKakaoTokenUrl(), map);
    }

    public String getProfileUrl(String provider) {
        if (!provider.equals("kakao")) {
            return null;
        }
        return kakaoInfo.getKakaoProfileUrl();
    }

    @Getter
    @Component
    static class KakaoInfo {
        @Value("${spring.security.oauth2.client.registration.kakao.client_id}")
        String kakaoClientId;

        @Value("${spring.security.oauth2.client.registration.kakao.redirect_uri}")
        String kakaoRedirect;

        @Value("${spring.security.oauth2.client.registration.kakao.client_secret}")
        String kakacoClientSecret;

        @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
        private String kakaoTokenUrl;

        @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
        private String kakaoProfileUrl;
    }
}
