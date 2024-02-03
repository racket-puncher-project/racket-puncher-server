package com.example.demo.siteuser.dto;

import com.example.demo.entity.SiteUser;
import com.example.demo.type.AgeGroup;
import com.example.demo.type.GenderType;
import com.example.demo.type.Ntrp;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatorInfoDto {
    private long id;
    private String siteUserName;
    private String nickname;
    private String email;
    private int mannerScore;
    private GenderType gender;
    private Ntrp ntrp;
    private AgeGroup ageGroup;
    private String profileImg;

    public static CreatorInfoDto fromEntity(SiteUser siteUser) {
        return CreatorInfoDto.builder()
                .id(siteUser.getId())
                .siteUserName(siteUser.getSiteUserName())
                .nickname(siteUser.getNickname())
                .email(siteUser.getEmail())
                .mannerScore(siteUser.getMannerScore())
                .gender(siteUser.getGender())
                .ntrp(siteUser.getNtrp())
                .ageGroup(siteUser.getAgeGroup())
                .profileImg(siteUser.getProfileImg())
                .build();
    }
}
