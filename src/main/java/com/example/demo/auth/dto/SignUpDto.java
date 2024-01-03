package com.example.demo.auth.dto;

import com.example.demo.type.AgeGroup;
import com.example.demo.type.GenderType;
import com.example.demo.type.Ntrp;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "이메일을 입력해 주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    private String password;

    @NotBlank(message = "닉네임을 입력해 주세요.")
    private String nickname;

    @NotBlank(message = "전화번호를 입력해 주세요.")
    private String phoneNumber;

    @NotBlank(message = "성별을 입력해 주세요.")
    private GenderType gender;

    @NotBlank(message = "이름을 입력해 주세요.")
    private String siteUserName;

    @NotBlank(message = "ntrp를 입력해 주세요.")
    private Ntrp ntrp;

    @NotBlank(message = "주소를 입력해 주세요.")
    private String address;

    @NotBlank(message = "우편번호를 입력해 주세요.")
    private String zipCode;

    private String profileImg;

    @NotBlank(message = "연령대를 입력해 주세요.")
    private AgeGroup ageGroup;
}
