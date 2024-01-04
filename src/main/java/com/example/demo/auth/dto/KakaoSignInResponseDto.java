package com.example.demo.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoSignInResponseDto {
    private Boolean registered = true;
    private String accessToken;
    private String refreshToken;
}