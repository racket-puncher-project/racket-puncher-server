package com.example.demo.siteuser.controller;

import com.example.demo.common.ResponseDto;
import com.example.demo.common.ResponseUtil;
import com.example.demo.aws.S3Uploader;
import com.example.demo.entity.Auth;
import com.example.demo.entity.SiteUser;
import com.example.demo.exception.impl.NicknameUnavailableException;
import com.example.demo.oauth2.dto.AccessToken;
import com.example.demo.oauth2.dto.ProfileDto;
import com.example.demo.oauth2.service.ProviderService;
import com.example.demo.siteuser.dto.SiteUserLoginResponseDto;
import com.example.demo.siteuser.repository.SiteUserRepository;
import com.example.demo.siteuser.dto.EmailRequestDto;
import com.example.demo.siteuser.dto.NicknameRequestDto;
import com.example.demo.siteuser.security.TokenProvider;
//import com.example.demo.siteuser.service.EmailService;
import com.example.demo.siteuser.service.MemberService;
import com.example.demo.type.Authority;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Response;
import org.springframework.data.redis.core.ReactiveSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.CommunicationException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SiteUserRepository siteUserRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final MemberService memberService;
    private final TokenProvider tokenProvider;
    private final S3Uploader s3Uploader;
    private final ProviderService providerService;
    // private final EmailService emailService; // 이메일

    @PostMapping("/signup")
    public ResponseEntity<ResponseDto<String>> signup(@RequestBody Auth.SignUp request) {
        try {
            memberService.register(request);
            // 이메일
            // SiteUser siteUser = memberService.register(request);
            // emailService.sendEmail(siteUser.getId(), siteUser.getEmail());

            return ResponseEntity.ok(ResponseUtil.SUCCESS("회원 가입 성공"));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(ResponseUtil.SUCCESS("회원 가입 실패"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody Auth.SignIn request) {
        // 로그인용 API
        var member = this.memberService.authenticate(request);
        //List<String> role = new ArrayList<>();
        //role.add(member.getRoles().toString());
        var token = this.tokenProvider.generateAccessToken(member.getEmail(), member.getRoles());
        var refreshToken = this.tokenProvider.generateRefreshToken(member.getEmail());
        var id = member.getId();
        SiteUserLoginResponseDto siteUserLoginResponseDto = new SiteUserLoginResponseDto();
        siteUserLoginResponseDto.setId(id);
        siteUserLoginResponseDto.setAccessToken(token);
        siteUserLoginResponseDto.setRefreshToken(refreshToken);
        return ResponseEntity.ok(siteUserLoginResponseDto);
    }

    @PostMapping("/signin/kakao")
    public ResponseEntity<?> signinKakao(@RequestBody Auth.SignKakao request) {
        // 카카오용 API

        try {
            AccessToken accessToken = providerService.getAccessToken(request.getCode(), request.getProvider());
            //List<String> role = new ArrayList<>();
            //role.add(member.getRoles().toString());
            ProfileDto profile = providerService.getProfile(accessToken.getAccess_token(), request.getProvider());
            var member = siteUserRepository.findByNickname(profile.getNickname());
            if (member.isPresent()) {
                var refreshToken = this.tokenProvider.generateRefreshToken(member.get().getEmail());
                var acsToken = this.tokenProvider.generateAccessToken(member.get().getEmail(), member.get().getRoles());
                SiteUserLoginResponseDto siteUserLoginResponseDto = new SiteUserLoginResponseDto();
                siteUserLoginResponseDto.setId(member.get().getId());
                siteUserLoginResponseDto.setEmail(member.get().getEmail());
                siteUserLoginResponseDto.setAccessToken(acsToken);
                siteUserLoginResponseDto.setRefreshToken(refreshToken);
                return ResponseEntity.ok(siteUserLoginResponseDto);
            } else {
                SiteUserLoginResponseDto siteUserLoginResponseDto = new SiteUserLoginResponseDto();
                siteUserLoginResponseDto.setNickname(profile.getNickname());
                siteUserLoginResponseDto.setProfileImage(profile.getProfileImage());
                siteUserLoginResponseDto.setRedirectUrl("redirect:/register");
                return ResponseEntity.ok(siteUserLoginResponseDto);
            }
        } catch (CommunicationException e) {
            return new ResponseEntity<>("Wrong Request", HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestBody Auth.Reissue reissue) {
        Authentication authentication = tokenProvider.getAuthentication(reissue.getAccessToken());
        String refreshToken = redisTemplate.opsForValue().get(authentication.getName());
        if (refreshToken == null || ObjectUtils.isEmpty(refreshToken)) {
            return new ResponseEntity<>("Wrong Request", HttpStatus.BAD_REQUEST);
        }
        if (!refreshToken.equals(reissue.getRefreshToken())) {
            return new ResponseEntity<>("Refresh Token Information Does Not Match.", HttpStatus.BAD_REQUEST);
        }
        var member = siteUserRepository.findByEmail(authentication.getName());
        var token = tokenProvider.generateAccessToken(authentication.getName(), member.get().getRoles());
        //var rftoken = tokenProvider.generateRefreshToken(authentication.getName());
        //String[] tokenList = {token, rftoken};
        //return new ResponseEntity<>("The token information has been updated.", HttpStatus.OK);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signout(@RequestBody Auth.SignOut signOut) {
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
    public ResponseEntity<?> quit(@RequestBody Auth.Quit request) {
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
    public ResponseEntity<ResponseDto<String>> checkNicknameExistence(@RequestBody NicknameRequestDto nicknameRequestDto) {
        boolean exists = memberService.isNicknameExist(nicknameRequestDto.getNickname());

        if (exists) {
            throw new NicknameUnavailableException();
        } else {
            return ResponseEntity.ok(ResponseUtil.SUCCESS("사용 가능한 닉네임 입니다."));
        }
    }

/*
    @GetMapping("/email-verify")
    public void viewConfirmEmail(@RequestParam String token) {
        emailService.verifyEmail(token);
    }*/

}
