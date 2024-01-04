package com.example.demo.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoFirstSignInResponseDto {
    private Boolean registered = false;
    private String email;
    private String profileImageUrl;
    private String nickName;

    public static KakaoFirstSignInResponseDto fromKakaoUserInfo(KakaoUserInfoDto kakaoUserInfoDto){
        return KakaoFirstSignInResponseDto.builder()
                .registered(false)
                .email(kakaoUserInfoDto.getKakaoAcount().getEmail())
                .profileImageUrl(kakaoUserInfoDto.getKakaoAcount().getProfile().getProfileImageUrl())
                .nickName(kakaoUserInfoDto.getKakaoAcount().getProfile().getNickname())
                .build();
    }
}