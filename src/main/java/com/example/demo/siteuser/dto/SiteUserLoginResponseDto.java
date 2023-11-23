package com.example.demo.siteuser.dto;

import com.example.demo.entity.SiteUser;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SiteUserLoginResponseDto {
    private Long id;
    private String email;
    private String nickname;
    private String profileImage;
    private String accessToken;
    private String refreshToken;
    private String redirectUrl;
}
