package com.example.demo.siteuser.dto;

import com.example.demo.entity.SiteUser;
import com.example.demo.type.AgeGroup;
import com.example.demo.type.GenderType;
import com.example.demo.type.Ntrp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewPageInfoDto {
    private Long siteUserId;
    private String profileImg;
    private String siteUserName;
    private String nickname;
    private String address;
    private String zipCode;
    private Ntrp ntrp;
    private GenderType gender;
    private int mannerScore;
    private AgeGroup ageGroup;


    public static ReviewPageInfoDto fromEntity(SiteUser siteUser) {

        return ReviewPageInfoDto.builder()
                .siteUserId(siteUser.getId())
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