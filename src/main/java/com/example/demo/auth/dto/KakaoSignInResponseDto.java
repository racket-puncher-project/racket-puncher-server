package com.example.demo.auth.dto;

import com.example.demo.type.AuthType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoSignInResponseDto implements KakaoSignIn {
    private Boolean registered = true;
    private String accessToken;
    private String refreshToken;
    private AuthType authType = AuthType.KAKAO;
}