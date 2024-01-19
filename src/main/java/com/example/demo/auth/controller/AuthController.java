package com.example.demo.auth.controller;

import com.example.demo.auth.dto.AccessTokenDto;
import com.example.demo.auth.dto.AuthCodeRequestDto;
import com.example.demo.auth.dto.EmailRequestDto;
import com.example.demo.auth.dto.FindEmailResponseDto;
import com.example.demo.auth.dto.GeneralSignInResponseDto;
import com.example.demo.auth.dto.KakaoCodeDto;
import com.example.demo.auth.dto.NicknameRequestDto;
import com.example.demo.auth.dto.PasswordRequestDto;
import com.example.demo.auth.dto.PhoneNumberRequestDto;
import com.example.demo.auth.dto.ResetPasswordDto;
import com.example.demo.auth.dto.ResetTokenDto;
import com.example.demo.auth.dto.SignInDto;
import com.example.demo.auth.dto.SignUpDto;
import com.example.demo.auth.dto.StringResponseDto;
import com.example.demo.auth.dto.UserInfoForPasswordDto;
import com.example.demo.auth.service.AuthService;
import com.example.demo.auth.service.KakaoOAuthService;
import com.example.demo.auth.service.PhoneAuthService;
import com.example.demo.common.ResponseDto;
import com.example.demo.common.ResponseUtil;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final KakaoOAuthService kakaoOAuthService;
    private final PhoneAuthService phoneAuthService;

    @PostMapping("/sign-up")
    public void signUp(@RequestBody SignUpDto signUpDto) {
        authService.register(signUpDto);
    }

    @PostMapping("/sign-in")
    public ResponseDto<GeneralSignInResponseDto> signIn(@RequestBody SignInDto signInDto) {
        var result = authService.signIn(signInDto);
        return ResponseUtil.SUCCESS(result);
    }

    @PostMapping("/kakao")
    public ResponseDto<?> kakaoCallback(@RequestBody KakaoCodeDto kakaoCodeDto) {
        var result = kakaoOAuthService.processOauth(kakaoCodeDto.getCode());
        return ResponseUtil.SUCCESS(result);
    }

    @PostMapping("/reissue")
    public ResponseDto<AccessTokenDto> reissue(@RequestBody AccessTokenDto accessTokenDto) {
        var newAccessToken = authService.tokenReissue(accessTokenDto);
        return ResponseUtil.SUCCESS(newAccessToken);
    }

    @PostMapping("/sign-out")
    public void signOut(@RequestBody AccessTokenDto accessTokenDto) {
        authService.signOut(accessTokenDto);
    }

    @PostMapping("/check-email")
    public ResponseDto<StringResponseDto> checkEmail(@RequestBody EmailRequestDto emailRequestDto) {
        var result = authService.checkEmail(emailRequestDto.getEmail());
        return ResponseUtil.SUCCESS(result);
    }

    @PostMapping("/check-nickname")
    public ResponseDto<StringResponseDto> checkNickname(@RequestBody NicknameRequestDto nicknameRequestDto) {
        var result = authService.checkNickname(nicknameRequestDto.getNickname());
        return ResponseUtil.SUCCESS(result);
    }

    @PostMapping("/phone/send-code")
    public ResponseDto<StringResponseDto> sendCode(@RequestBody PhoneNumberRequestDto phoneNumberRequestDto) {
        var result = phoneAuthService.sendCode(phoneNumberRequestDto.getPhoneNumber());
        return ResponseUtil.SUCCESS(result);
    }

    @PostMapping("/phone/verify-code")
    public ResponseDto<StringResponseDto> verifyCode(@RequestBody AuthCodeRequestDto authCodeRequestDto) {
        var result = phoneAuthService.verifyCode(authCodeRequestDto.getPhoneNumber(), authCodeRequestDto.getAuthCode());
        return ResponseUtil.SUCCESS(result);
    }

    @DeleteMapping("/withdraw")
    public void quit(Principal principal, @RequestBody PasswordRequestDto passwordRequestDto) {
        var email = principal.getName();
        authService.withdraw(email, passwordRequestDto.getPassword());
    }

    @PostMapping("/find-id")
    public ResponseDto<FindEmailResponseDto> findId(@RequestBody PhoneNumberRequestDto phoneNumberRequestDto) {
        var result = authService.findEmail(phoneNumberRequestDto.getPhoneNumber());
        return ResponseUtil.SUCCESS(result);
    }

    @PostMapping("/password/verify-user")
    public ResponseDto<ResetTokenDto> verifyUserForResetPassword(@RequestBody UserInfoForPasswordDto userInfoForPasswordDto) {
        var result = authService.verifyUserForResetPassword(userInfoForPasswordDto.getEmail(), userInfoForPasswordDto.getPhoneNumber());
        return ResponseUtil.SUCCESS(result);
    }

    @PatchMapping("/password/reset")
    public ResponseDto<StringResponseDto> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        var result = authService.resetPassword(resetPasswordDto.getResetToken(), resetPasswordDto.getNewPassword());
        return ResponseUtil.SUCCESS(result);
    }
}