package com.example.demo.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoFirstSignInResponseDto implements KakaoSignIn{
    private Boolean registered = false;
    private String email;
    private String profileImageUrl;
    private String nickname;

    public static KakaoFirstSignInResponseDto fromKakaoUserInfo(KakaoUserInfoDto kakaoUserInfoDto){
        return KakaoFirstSignInResponseDto.builder()
                .registered(false)
                .email(kakaoUserInfoDto.getKakaoAccount().getEmail())
                .profileImageUrl(kakaoUserInfoDto.getKakaoAccount().getProfile().getProfileImageUrl())
                .nickname(kakaoUserInfoDto.getKakaoAccount().getProfile().getNickname())
                .build();
    }
}