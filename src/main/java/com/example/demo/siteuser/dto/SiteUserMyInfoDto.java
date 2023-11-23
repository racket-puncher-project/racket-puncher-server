package com.example.demo.siteuser.dto;

import com.example.demo.entity.SiteUser;
import com.example.demo.type.AgeGroup;
import com.example.demo.type.GenderType;
import com.example.demo.type.Ntrp;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SiteUserMyInfoDto {
    private Long id;
    private String password;
    private String siteusername;
    private String nickname;
    private String email;
    private String phoneNumber;
    private Integer mannerScore;
    private Integer penaltyScore;
    private GenderType gender;
    private Ntrp ntrp;
    private String address;
    private String zipCode;
    private AgeGroup ageGroup;
    private String profileImg;
    private String createDate;
    private Boolean isPhoneVerified;

    public static SiteUserMyInfoDto fromEntity(SiteUser siteUser) {
        int roundedMannerScore = (int) Math.round(siteUser.getMannerScore());

        return SiteUserMyInfoDto.builder()
                .id(siteUser.getId())
                .siteusername(siteUser.getSiteusername())
                .password(siteUser.getPassword())
                .nickname(siteUser.getNickname())
                .email(siteUser.getEmail())
                .phoneNumber(siteUser.getPhoneNumber())
                .mannerScore(roundedMannerScore)
                .penaltyScore(siteUser.getPenaltyScore())
                .gender(siteUser.getGender())
                .ntrp(siteUser.getNtrp())
                .address(siteUser.getAddress())
                .zipCode(siteUser.getZipCode())
                .ageGroup(siteUser.getAgeGroup())
                .profileImg(siteUser.getProfileImg())
                .createDate(siteUser.getCreateDate().toString())
                .isPhoneVerified(siteUser.getIsPhoneVerified())
                .build();
    }
}