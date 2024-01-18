package com.example.demo.siteuser.dto;

import com.example.demo.entity.SiteUser;
import lombok.*;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SiteUserInfoForListDto {
    private Long id;
    private String profileImg;
    private String nickname;

    public static SiteUserInfoForListDto fromEntity(SiteUser siteUser) {
        return SiteUserInfoForListDto.builder()
                .id(siteUser.getId())
                .profileImg(Optional.ofNullable(siteUser.getProfileImg()).orElse(""))
                .nickname(siteUser.getNickname())
                .build();
    }
}