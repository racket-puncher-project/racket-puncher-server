package com.example.demo.auth.dto;

import com.example.demo.type.AuthType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FindEmailResponseDto {
    private AuthType authType;
    private String email;
}