package com.example.demo.siteuser.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignOutDto {
    @NotBlank(message = "잘못된 요청입니다.")
    private String accessToken;

    @NotBlank(message = "잘못된 요청입니다.")
    private String refreshToken;
}
