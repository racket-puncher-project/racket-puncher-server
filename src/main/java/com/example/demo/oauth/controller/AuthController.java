package com.example.demo.oauth.controller;

import static com.example.demo.exception.type.ErrorCode.INVALID_NICKNAME;

import com.example.demo.common.ResponseDto;
import com.example.demo.common.ResponseUtil;
import com.example.demo.exception.RacketPuncherException;
import com.example.demo.oauth.dto.AccessTokenDto;
import com.example.demo.oauth.dto.EmailRequestDto;
import com.example.demo.oauth.dto.LoginResponseDto;
import com.example.demo.oauth.dto.NicknameRequestDto;
import com.example.demo.oauth.dto.QuitDto;
import com.example.demo.oauth.dto.SignInDto;
import com.example.demo.oauth.dto.SignUpDto;
import com.example.demo.oauth.security.TokenProvider;
import com.example.demo.oauth.service.AuthService;
import com.example.demo.siteuser.repository.SiteUserRepository;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

;

@Slf4j
@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RedisTemplate<String, String> redisTemplate;
    private final AuthService authService;
    private final TokenProvider tokenProvider;
//    private final ProviderService providerService;

    @PostMapping("/sign-up")
    public void signUp(@RequestBody SignUpDto signUpDto) {
            authService.register(signUpDto);
    }

    @PostMapping("/sign-in")
    public ResponseDto<LoginResponseDto> signIn(@RequestBody SignInDto signInDto) {
        authService.authenticate(signInDto);
        var token = tokenProvider.generateAccessToken(signInDto.getEmail());
        var refreshToken = tokenProvider.generateAndSaveRefreshToken(signInDto.getEmail());
        var result = LoginResponseDto.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
        return ResponseUtil.SUCCESS(result);
    }

//    @PostMapping("/sign-in/kakao")
//    public ResponseEntity<?> signInKakao(@RequestBody SignKakao request) {
//        // 카카오용 API
//
//        try {
////            AccessToken accessToken = providerService.getAccessToken(request.getCode(), request.getProvider());
////            ProfileDto profile = providerService.getProfile(accessToken.getAccess_token(), request.getProvider());
////            var member = siteUserRepository.findByNickname(profile.getNickname());
//            if (member.isPresent()) {
//                var refreshToken = this.tokenProvider.generateAndSaveRefreshToken(member.get().getEmail());
//                var acsToken = this.tokenProvider.generateAccessToken(member.get().getEmail());
//                LoginResponseDto loginResponseDto = LoginResponseDto.builder()
//                        .accessToken(acsToken)
//                        .refreshToken(refreshToken)
//                        .build();
//                return ResponseEntity.ok(loginResponseDto);
//            } else {
//                LoginResponseDto loginResponseDto = new LoginResponseDto();
//                return ResponseEntity.ok(loginResponseDto);
//            }
//        } catch (CommunicationException e) {
//            return new ResponseEntity<>("Wrong Request", HttpStatus.BAD_REQUEST);
//        }
//    }

    @PostMapping("/reissue")
    public ResponseDto<AccessTokenDto> reissue(@RequestBody AccessTokenDto accessTokenDto) {
        var newAccessToken = authService.getNewAccessTokenAndSaveNewRefreshToken(accessTokenDto);
        return ResponseUtil.SUCCESS(newAccessToken);
    }

    @PostMapping("/sign-out")
    public ResponseEntity<?> signOut(@RequestBody AccessTokenDto accessTokenDto) {
        var accessToken = accessTokenDto.getAccessToken();
        if (!StringUtils.hasText(accessToken) || !this.tokenProvider.validateToken(accessToken)) {
            return new ResponseEntity<>("Wrong Request", HttpStatus.BAD_REQUEST);
        }
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        if (redisTemplate.opsForValue().get(authentication.getName()) != null) {
            redisTemplate.delete(authentication.getName());
        }
        Long expiration = tokenProvider.getExpiration(accessToken);
        redisTemplate.opsForValue().set(accessToken, "logout", expiration, TimeUnit.MILLISECONDS);

        return new ResponseEntity<>("LogOut Completed", HttpStatus.OK);
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

    @PostMapping(path = "/check-email")
    public ResponseEntity<ResponseDto<String>> checkEmailExistence(@RequestBody EmailRequestDto emailRequestDto) {
        boolean exists = authService.isEmailExist(emailRequestDto.getEmail());
        String message = exists ? "사용 불가능한 이메일 입니다." : "사용 가능한 이메일 입니다.";
        return ResponseEntity.ok(ResponseUtil.SUCCESS(message));
    }

    @PostMapping(path = "/check-nickname")
    public ResponseEntity<ResponseDto<String>> checkNicknameExistence(
            @RequestBody NicknameRequestDto nicknameRequestDto) {
        boolean exists = authService.isNicknameExist(nicknameRequestDto.getNickname());

        if (exists) {
            throw new RacketPuncherException(INVALID_NICKNAME);
        } else {
            return ResponseEntity.ok(ResponseUtil.SUCCESS("사용 가능한 닉네임 입니다."));
        }
    }
}
