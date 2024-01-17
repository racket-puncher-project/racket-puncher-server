package com.example.demo.auth.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResetPasswordDto {
    private String resetToken;
    private String newPassword;
}
