package com.example.demo.auth.dto;

import com.example.demo.type.AuthType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoSignInResponseDto implements KakaoSignIn {
    private final Boolean registered = true;
    private String accessToken;
    private String refreshToken;
    private final AuthType authType = AuthType.KAKAO;
}