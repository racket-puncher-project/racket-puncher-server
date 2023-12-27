package com.example.demo.siteuser.dto;

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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpDto {
    private String email;
    private String password;
    private String nickname;
    private String phoneNumber;
    private GenderType gender;
    private String siteUserName;
    private Ntrp ntrp;
    private String address;
    private String zipCode;
    private String profileImg;
    private AgeGroup ageGroup;
}
