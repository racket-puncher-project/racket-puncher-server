package com.example.demo.siteuser.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDto {
    private String accessToken;
    private String refreshToken;
}
