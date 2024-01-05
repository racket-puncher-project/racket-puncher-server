package com.example.demo.siteuser.dto;

import com.example.demo.type.AgeGroup;
import com.example.demo.type.GenderType;
import com.example.demo.type.Ntrp;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateSiteUserInfoDto {
    private String nickname;
    private String password;
    private String checkPassword;
    private String phoneNumber;
    private String address;
    private String zipCode;
    private Ntrp ntrp;
    private GenderType gender;
    private AgeGroup ageGroup;
    private String profileImg;
}