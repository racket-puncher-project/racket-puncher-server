package com.example.demo.matching.dto;

import com.example.demo.entity.Apply;
import com.example.demo.type.AgeGroup;
import com.example.demo.type.ApplyStatus;
import com.example.demo.type.GenderType;
import com.example.demo.type.Ntrp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplyMember {
    private long applyId;
    private long siteUserId;
    private String nickname;
    private String siteUsername;
    private String email;
    private int mannerScore;
    private GenderType genderType;
    private Ntrp ntrp;
    private String address;
    private String zipCode;
    private AgeGroup ageGroup;
    private String profileImg;

    public static ApplyMember from(Apply apply) {
        var siteUser = apply.getSiteUser();
        return ApplyMember.builder()
                .applyId(apply.getId())
                .siteUserId(siteUser.getId())
                .nickname(siteUser.getNickname())
                .siteUsername(siteUser.getUsername())
                .email(siteUser.getEmail())
                .mannerScore(siteUser.getMannerScore())
                .genderType(siteUser.getGender())
                .ntrp(siteUser.getNtrp())
                .address(siteUser.getAddress())
                .zipCode(siteUser.getZipCode())
                .ageGroup(siteUser.getAgeGroup())
                .profileImg(siteUser.getProfileImg())
                .build();
    }
}
