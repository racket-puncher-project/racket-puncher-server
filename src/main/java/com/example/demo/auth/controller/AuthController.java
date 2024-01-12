package com.example.demo.auth.controller;

import com.example.demo.auth.dto.*;
import com.example.demo.auth.service.KakaoOAuthService;
import com.example.demo.auth.service.PhoneAuthService;
import com.example.demo.common.ResponseDto;
import com.example.demo.common.ResponseUtil;
import com.example.demo.auth.security.TokenProvider;
import com.example.demo.auth.service.AuthService;

import java.security.Principal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RedisTemplate<String, String> redisTemplate;
    private final AuthService authService;
    private final TokenProvider tokenProvider;
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

    @GetMapping("/kakao")
    public ResponseDto<?> kakaoCallback(@RequestParam String code) {
        var result = kakaoOAuthService.processOauth(code);
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
}