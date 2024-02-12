package com.example.demo.auth.dto;

import com.example.demo.type.AuthType;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneralSignInResponseDto {
    private String accessToken;
    private String refreshToken;
    private final AuthType authType = AuthType.GENERAL;
}