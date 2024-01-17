package com.example.demo.auth.service;

import static com.example.demo.exception.type.ErrorCode.*;

import com.example.demo.auth.dto.*;
import com.example.demo.entity.SiteUser;
import com.example.demo.exception.RacketPuncherException;
import com.example.demo.auth.security.TokenProvider;
import com.example.demo.notification.service.NotificationService;
import com.example.demo.siteuser.repository.SiteUserRepository;
import com.example.demo.type.AuthType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    public static final String VALID_EMAIL = "사용 가능한 이메일입니다.";
    public static final String VALID_NICKNAME = "사용 가능한 닉네임입니다.";
    public static final String SUCCESS_LOGOUT = "로그아웃 성공";
    public static final String SUCCESS_WITHDRAWAL = "탈퇴 성공";
    public static final String SUCCESS_PASSWORD_RESET = "비밀번호 초기화 성공";
    public static final String RESET_TOKEN_PREFIX = "reset:";

    private final PasswordEncoder passwordEncoder;
    private final SiteUserRepository siteUserRepository;
    private final TokenProvider tokenProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final NotificationService notificationService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.siteUserRepository.findByEmail(email)
                .orElseThrow(() -> new RacketPuncherException(EMAIL_NOT_FOUND));
    }

    public SiteUser register(SignUpDto signUpDto) {
        boolean exists = siteUserRepository.existsByEmail(signUpDto.getEmail());
        if (exists) {
            throw new RacketPuncherException(EMAIL_ALREADY_EXISTED);
        }
        if (AuthType.GENERAL.equals(signUpDto.getAuthType())) {
            signUpDto.setPassword(this.passwordEncoder.encode(signUpDto.getPassword()));
        }
        var siteUser = siteUserRepository.save(SiteUser.fromDto(signUpDto));
        return siteUser;
    }

    public SiteUser authenticate(SignInDto signInDto) {
        var user = siteUserRepository.findByEmail(signInDto.getEmail())
                .orElseThrow(() -> new RacketPuncherException(EMAIL_NOT_FOUND));

        if (!passwordEncoder.matches(signInDto.getPassword(), user.getPassword())) {
            throw new RacketPuncherException(WRONG_PASSWORD);
        }
        return user;
    }

    public AccessTokenDto tokenReissue(AccessTokenDto accessTokenDto) {
        Authentication authentication = tokenProvider.getAuthentication(accessTokenDto.getAccessToken());
        String refreshToken = redisTemplate.opsForValue().get(authentication.getName());
        if (ObjectUtils.isEmpty(refreshToken)) {
            throw new RacketPuncherException(REFRESH_TOKEN_EXPIRED);
        }
        if (refreshToken.equals(accessTokenDto.getAccessToken())) {
            throw new RacketPuncherException(INVALID_TOKEN);
        }
        var newAccessToken = tokenProvider.generateAccessToken(authentication.getName());
        redisTemplate.delete(authentication.getName());
        tokenProvider.generateAndSaveRefreshToken(authentication.getName());
        return new AccessTokenDto(newAccessToken);
    }

    public GeneralSignInResponseDto signIn(SignInDto signInDto) {
        authenticate(signInDto);

        var accessToken = tokenProvider.generateAccessToken(signInDto.getEmail());
        var refreshToken = tokenProvider.generateAndSaveRefreshToken(signInDto.getEmail());

        return GeneralSignInResponseDto.builder()
                .authType(AuthType.GENERAL)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String signOut(AccessTokenDto accessTokenDto) {
        String email = tokenProvider.getUserEmail(accessTokenDto.getAccessToken());
        if (redisTemplate.opsForValue().get(email) == null) {
            throw new RacketPuncherException(REFRESH_TOKEN_EXPIRED);
        }
        redisTemplate.delete(tokenProvider.getUserEmail(accessTokenDto.getAccessToken()));
        redisTemplate.opsForValue().set(email, accessTokenDto.getAccessToken());
        return SUCCESS_LOGOUT;
    }

    public StringResponseDto checkEmail(String email) {
        if (siteUserRepository.existsByEmail(email)) {
            throw new RacketPuncherException(EMAIL_ALREADY_EXISTED);
        }
        return new StringResponseDto(VALID_EMAIL);
    }

    public StringResponseDto checkNickname(String nickname) {
        if (siteUserRepository.existsByNickname(nickname)) {
            throw new RacketPuncherException(NICKNAME_ALREADY_EXISTED);
        }
        return new StringResponseDto(VALID_NICKNAME);
    }

    public String withdraw(String email, String password) {
        var user = siteUserRepository.findByEmail(email)
                .orElseThrow(() -> new RacketPuncherException(EMAIL_NOT_FOUND));

        if (user.getAuthType().equals(AuthType.GENERAL) && !passwordEncoder.matches(password, user.getPassword())) {
            throw new RacketPuncherException(WRONG_PASSWORD);
        }
        redisTemplate.delete(email);
        siteUserRepository.delete(user);
        return SUCCESS_WITHDRAWAL;
    }

    public FindEmailResponseDto findEmail(String phoneNumber) {
        var user = siteUserRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RacketPuncherException(REGISTRATION_INFO_NOT_FOUND));

        if (user.getAuthType().equals(AuthType.KAKAO)) {
            return FindEmailResponseDto.builder()
                    .authType(AuthType.KAKAO)
                    .email("")
                    .build();
        }

        return FindEmailResponseDto.builder()
                .authType(AuthType.GENERAL)
                .email(user.getEmail())
                .build();
    }

    public ResetTokenDto verifyUserForResetPassword(String email, String phoneNumber){
        var user = siteUserRepository.findByEmailAndPhoneNumber(email, phoneNumber)
                .orElseThrow(() -> new RacketPuncherException(REGISTRATION_INFO_NOT_FOUND));

        if (user.getAuthType().equals(AuthType.KAKAO)) {
            return ResetTokenDto.builder()
                    .authType(AuthType.KAKAO)
                    .resetToken("")
                    .build();
        }

        String resetToken = tokenProvider.generateResetToken(email);
        redisTemplate.opsForValue().set(RESET_TOKEN_PREFIX+email, resetToken);

        return ResetTokenDto.builder()
                .authType(AuthType.GENERAL)
                .resetToken(resetToken)
                .build();
    }

    @Transactional
    public StringResponseDto resetPassword(String resetToken, String newPassword){
        if (!tokenProvider.validateResetToken(resetToken)) {
            throw new RacketPuncherException(RESET_TOKEN_EXPIRED);
        }

        String email = tokenProvider.getUserEmailForResetToken(resetToken);
        String resetKey = RESET_TOKEN_PREFIX + email;

        if (!resetToken.equals(redisTemplate.opsForValue().get(resetKey))) { // ResetToken이 null 이거나 일치하지 않으면
            throw new RacketPuncherException(RESET_TOKEN_ALREADY_USED);
        }

        SiteUser siteUser = siteUserRepository.findByEmail(email)
                .orElseThrow(() -> new RacketPuncherException(USER_NOT_FOUND));

        redisTemplate.delete(resetKey);
        siteUser.setPassword(passwordEncoder.encode(newPassword));
        return new StringResponseDto(SUCCESS_PASSWORD_RESET);
    }
}