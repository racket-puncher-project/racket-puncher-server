package com.example.demo.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoFirstSignInResponseDto implements KakaoSignIn{
    private final Boolean registered = false;
    private String email;
    private String profileImageUrl;
    private String nickname;

    public static KakaoFirstSignInResponseDto fromKakaoUserInfo(KakaoUserInfoDto kakaoUserInfoDto){
        return KakaoFirstSignInResponseDto.builder()
                .email(kakaoUserInfoDto.getKakaoAccount().getEmail())
                .profileImageUrl(kakaoUserInfoDto.getKakaoAccount().getProfile().getProfileImageUrl())
                .nickname(kakaoUserInfoDto.getKakaoAccount().getProfile().getNickname())
                .build();
    }
}