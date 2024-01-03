package com.example.demo.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuitDto {
        @NotBlank(message = "잘못된 요청입니다.")
        private String accessToken;

        @NotBlank(message = "잘못된 요청입니다.")
        private String refreshToken;

        @NotBlank(message = "잘못된 요청입니다.")
        private String email;

        @NotBlank(message = "잘못된 요청입니다.")
        private String password;
}
