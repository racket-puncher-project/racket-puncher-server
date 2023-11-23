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
public class SiteUserInfoDto{
    private String profileImg;
    private String nickname;
    private String address;
    private String zipCode;
    private Ntrp ntrp;
    private GenderType gender;
    private Integer mannerScore;
    private Integer penaltyScore;
    private AgeGroup ageGroup;


    public static SiteUserInfoDto fromEntity(SiteUser siteUser) {
        int roundedMannerScore = (int) Math.round(siteUser.getMannerScore());

        return SiteUserInfoDto.builder()
                .profileImg(siteUser.getProfileImg())
                .nickname(siteUser.getNickname())
                .address(siteUser.getAddress())
                .zipCode(siteUser.getZipCode())
                .ntrp(siteUser.getNtrp())
                .gender(siteUser.getGender())
                .mannerScore(roundedMannerScore)
                .penaltyScore(siteUser.getPenaltyScore())
                .ageGroup(siteUser.getAgeGroup())
                .build();
    }
}