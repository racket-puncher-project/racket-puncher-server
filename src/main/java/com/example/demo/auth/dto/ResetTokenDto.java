package com.example.demo.auth.dto;

import com.example.demo.type.AuthType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResetTokenDto {
    private AuthType authType;
    private String resetToken;
}