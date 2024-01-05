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
    private String siteUserName;
    private String nickname;
    private String address;
    private String zipCode;
    private Ntrp ntrp;
    private GenderType gender;
    private double mannerScore;
    private AgeGroup ageGroup;


    public static SiteUserInfoDto fromEntity(SiteUser siteUser) {

        return SiteUserInfoDto.builder()
                .profileImg(siteUser.getProfileImg())
                .siteUserName(siteUser.getSiteUserName())
                .nickname(siteUser.getNickname())
                .address(siteUser.getAddress())
                .zipCode(siteUser.getZipCode())
                .ntrp(siteUser.getNtrp())
                .gender(siteUser.getGender())
                .mannerScore(siteUser.getMannerScore())
                .ageGroup(siteUser.getAgeGroup())
                .build();
    }
}