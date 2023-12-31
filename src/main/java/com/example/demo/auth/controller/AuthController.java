package com.example.demo.auth.controller;

import com.example.demo.auth.dto.*;
import com.example.demo.auth.service.KakaoOAuthService;
import com.example.demo.common.ResponseDto;
import com.example.demo.common.ResponseUtil;
import com.example.demo.auth.security.TokenProvider;
import com.example.demo.auth.service.AuthService;

import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
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

    @DeleteMapping("/quit")
    public ResponseEntity<?> quit(@RequestBody QuitDto request) {
        var accessToken = request.getAccessToken();
        if (!StringUtils.hasText(accessToken) || !this.tokenProvider.validateToken(accessToken)) {
            return new ResponseEntity<>("Wrong Request", HttpStatus.BAD_REQUEST);
        }
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        if (redisTemplate.opsForValue().get(authentication.getName()) != null) {
            redisTemplate.delete(authentication.getName());
        }
        Long expiration = tokenProvider.getExpiration(accessToken);
        redisTemplate.opsForValue().set(accessToken, "quit", expiration, TimeUnit.MILLISECONDS);

        var result = this.authService.withdraw(request);
        return new ResponseEntity<>("Quit Completed", HttpStatus.OK);
    }
}