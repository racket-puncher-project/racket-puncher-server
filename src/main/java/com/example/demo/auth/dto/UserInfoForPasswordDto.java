package com.example.demo.auth.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoForPasswordDto {
    private String email;
    private String phoneNumber;
}