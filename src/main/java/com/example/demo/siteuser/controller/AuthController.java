package com.example.demo.siteuser.controller;

import com.example.demo.common.ResponseDto;
import com.example.demo.common.ResponseUtil;
import com.example.demo.exception.impl.NicknameUnavailableException;
import com.example.demo.oauth2.dto.AccessToken;
import com.example.demo.oauth2.dto.ProfileDto;
import com.example.demo.oauth2.service.ProviderService;
import com.example.demo.siteuser.dto.EmailRequestDto;
import com.example.demo.siteuser.dto.NicknameRequestDto;
import com.example.demo.siteuser.dto.QuitDto;
import com.example.demo.siteuser.dto.ReissueDto;
import com.example.demo.siteuser.dto.SignInDto;
import com.example.demo.siteuser.dto.SignKakao;
import com.example.demo.siteuser.dto.SignOutDto;
import com.example.demo.siteuser.dto.SignUpDto;
import com.example.demo.siteuser.dto.LoginResponseDto;
import com.example.demo.siteuser.repository.SiteUserRepository;
import com.example.demo.siteuser.security.TokenProvider;
import com.example.demo.siteuser.service.MemberService;
import java.util.concurrent.TimeUnit;
import javax.naming.CommunicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SiteUserRepository siteUserRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final MemberService memberService;
    private final TokenProvider tokenProvider;
    private final ProviderService providerService;

    @PostMapping("/sign-up")
    public void signUp(@RequestBody SignUpDto signUpDto) {
            memberService.register(signUpDto);
    }

    @PostMapping("/sign-in")
    public ResponseDto<LoginResponseDto> signIn(@RequestBody SignInDto signInDto) {
        memberService.authenticate(signInDto);
        var token = tokenProvider.generateAccessToken(signInDto.getEmail());
        var refreshToken = tokenProvider.generateRefreshToken(signInDto.getEmail());
        var result = LoginResponseDto.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
        return ResponseUtil.SUCCESS(result);
    }

    @PostMapping("/sign-in/kakao")
    public ResponseEntity<?> signInKakao(@RequestBody SignKakao request) {
        // 카카오용 API

        try {
            AccessToken accessToken = providerService.getAccessToken(request.getCode(), request.getProvider());
            ProfileDto profile = providerService.getProfile(accessToken.getAccess_token(), request.getProvider());
            var member = siteUserRepository.findByNickname(profile.getNickname());
            if (member.isPresent()) {
                var refreshToken = this.tokenProvider.generateRefreshToken(member.get().getEmail());
                var acsToken = this.tokenProvider.generateAccessToken(member.get().getEmail());
                LoginResponseDto loginResponseDto = new LoginResponseDto();
                loginResponseDto.setAccessToken(acsToken);
                loginResponseDto.setRefreshToken(refreshToken);
                return ResponseEntity.ok(loginResponseDto);
            } else {
                LoginResponseDto loginResponseDto = new LoginResponseDto();
                return ResponseEntity.ok(loginResponseDto);
            }
        } catch (CommunicationException e) {
            return new ResponseEntity<>("Wrong Request", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestBody ReissueDto reissue) {
        Authentication authentication = tokenProvider.getAuthentication(reissue.getAccessToken());
        String refreshToken = redisTemplate.opsForValue().get(authentication.getName());
        if (refreshToken == null || ObjectUtils.isEmpty(refreshToken)) {
            return new ResponseEntity<>("Wrong Request", HttpStatus.BAD_REQUEST);
        }
        if (!refreshToken.equals(reissue.getRefreshToken())) {
            return new ResponseEntity<>("Refresh Token Information Does Not Match.", HttpStatus.BAD_REQUEST);
        }
        var member = siteUserRepository.findByEmail(authentication.getName());
        var token = tokenProvider.generateAccessToken(authentication.getName());
        //var rftoken = tokenProvider.generateRefreshToken(authentication.getName());
        //String[] tokenList = {token, rftoken};
        //return new ResponseEntity<>("The token information has been updated.", HttpStatus.OK);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/sign-out")
    public ResponseEntity<?> signOut(@RequestBody SignOutDto signOut) {
        var accessToken = signOut.getAccessToken();
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

        var result = this.memberService.withdraw(request);
        return new ResponseEntity<>("Quit Completed", HttpStatus.OK);
    }

    @PostMapping(path = "/check-email")
    public ResponseEntity<ResponseDto<String>> checkEmailExistence(@RequestBody EmailRequestDto emailRequestDto) {
        boolean exists = memberService.isEmailExist(emailRequestDto.getEmail());
        String message = exists ? "사용 불가능한 이메일 입니다." : "사용 가능한 이메일 입니다.";
        return ResponseEntity.ok(ResponseUtil.SUCCESS(message));
    }

    @PostMapping(path = "/check-nickname")
    public ResponseEntity<ResponseDto<String>> checkNicknameExistence(
            @RequestBody NicknameRequestDto nicknameRequestDto) {
        boolean exists = memberService.isNicknameExist(nicknameRequestDto.getNickname());

        if (exists) {
            throw new NicknameUnavailableException();
        } else {
            return ResponseEntity.ok(ResponseUtil.SUCCESS("사용 가능한 닉네임 입니다."));
        }
    }
}
