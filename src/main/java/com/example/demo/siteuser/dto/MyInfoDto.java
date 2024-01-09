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
public class MyInfoDto {
    private long id;
    private String siteUserName;
    private String nickname;
    private String email;
    private String phoneNumber;
    private int mannerScore;
    private GenderType gender;
    private Ntrp ntrp;
    private String address;
    private String zipCode;
    private AgeGroup ageGroup;
    private String profileImg;

    public static MyInfoDto fromEntity(SiteUser siteUser) {

        return MyInfoDto.builder()
                .id(siteUser.getId())
                .siteUserName(siteUser.getSiteUserName())
                .nickname(siteUser.getNickname())
                .email(siteUser.getEmail())
                .phoneNumber(siteUser.getPhoneNumber())
                .mannerScore(siteUser.getMannerScore())
                .gender(siteUser.getGender())
                .ntrp(siteUser.getNtrp())
                .address(siteUser.getAddress())
                .zipCode(siteUser.getZipCode())
                .ageGroup(siteUser.getAgeGroup())
                .profileImg(siteUser.getProfileImg())
                .build();
    }
}