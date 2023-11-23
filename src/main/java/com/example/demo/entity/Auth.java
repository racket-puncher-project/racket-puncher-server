package com.example.demo.entity;

import com.example.demo.type.AgeGroup;
import com.example.demo.type.GenderType;
import com.example.demo.type.Ntrp;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

public class Auth {
    @Data
    public static class SignIn {
        private String email;
        private String password;
    }

    @Data
    public static class SignUp {

        private String email;
        private String password;
        private List<String> roles;
        private String nickname;
        private String phoneNumber;
        private GenderType gender;
        private Ntrp ntrp;
        private String address;
        private String zipCode;
        private AgeGroup ageGroup;
        private Timestamp createDate;
        private String profileImg;
        private String siteusername;

        public SiteUser fromUser() {
            return SiteUser.builder()
                    .email(this.email)
                    .password(this.password)
                    .roles(this.roles)
                    .nickname(this.nickname)
                    .phoneNumber(this.phoneNumber)
                    .gender(this.gender)
                    .ntrp(this.ntrp)
                    .address(this.address)
                    .zipCode(this.zipCode)
                    .ageGroup(this.ageGroup)
                    .profileImg(this.profileImg)
                    .siteusername(this.siteusername)
                    .build();
        }
    }

    @Data
    public static class Reissue {
        @NotBlank(message = "accessToken을 입력해주세요.")
        private String accessToken;

        @NotBlank(message = "refreshToken을 입력해주세요.")
        private String refreshToken;
    }

    @Data
    public static class SignOut {
        @NotBlank(message = "잘못된 요청입니다.")
        private String accessToken;

        @NotBlank(message = "잘못된 요청입니다.")
        private String refreshToken;
    }

    @Data
    public static class Quit {
        @NotBlank(message = "잘못된 요청입니다.")
        private String accessToken;

        @NotBlank(message = "잘못된 요청입니다.")
        private String refreshToken;

        @NotBlank(message = "잘못된 요청입니다.")
        private String email;

        @NotBlank(message = "잘못된 요청입니다.")
        private String password;
    }

    @Data
    public static class SignKakao {
        @NotBlank(message = "잘못된 요청입니다.")
        private String code;
        @NotBlank(message = "잘못된 요청입니다.")
        private String provider;
    }

}
