package com.example.demo.oauth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignKakao {
    @NotBlank(message = "잘못된 요청입니다.")
    private String code;
    @NotBlank(message = "잘못된 요청입니다.")
    private String provider;
}
