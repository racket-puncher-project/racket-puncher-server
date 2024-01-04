package com.example.demo.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoUserInfoDto {
    @JsonProperty("kakao_account")
    private KakaoAcount kakaoAcount;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class KakaoAcount {
        private Profile profile;
        private String email;

        @Getter
        @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public class Profile {
            private String nickname;
            @JsonProperty("profile_image_url")
            private String profileImageUrl;
        }
    }
}