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
public class SiteUserModifyDto{
    private String nickname;
    private String password;
    private String email;
    private String phoneNumber;
    private String address;
    private String zipCode;
    private Ntrp ntrp;
    private GenderType gender;
    private AgeGroup ageGroup;
    private String profileImg;

    public static SiteUserModifyDto fromEntity(SiteUser siteUser) {
        return SiteUserModifyDto.builder()
                .password(siteUser.getPassword())
                .profileImg(siteUser.getProfileImg())
                .email(siteUser.getEmail())
                .nickname(siteUser.getNickname())
                .address(siteUser.getAddress())
                .zipCode(siteUser.getZipCode())
                .ntrp(siteUser.getNtrp())
                .gender(siteUser.getGender())
                .ageGroup(siteUser.getAgeGroup())
                .build();
    }
}