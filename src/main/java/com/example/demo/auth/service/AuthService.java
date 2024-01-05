package com.example.demo.auth.service;

import static com.example.demo.exception.type.ErrorCode.*;

import com.example.demo.entity.SiteUser;
import com.example.demo.exception.RacketPuncherException;
import com.example.demo.auth.dto.AccessTokenDto;
import com.example.demo.auth.dto.GeneralSignInResponseDto;
import com.example.demo.auth.dto.QuitDto;
import com.example.demo.auth.dto.SignInDto;
import com.example.demo.auth.dto.SignUpDto;
import com.example.demo.auth.dto.StringResponseDto;
import com.example.demo.auth.security.TokenProvider;
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
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    public static final String VALID_EMAIL = "사용 가능한 이메일입니다.";
    public static final String SUCCESS_LOGOUT = "로그아웃 성공";
    public static final String VALID_NICKNAME = "사용 가능한 닉네임입니다.";
    private final PasswordEncoder passwordEncoder;
    private final SiteUserRepository siteUserRepository;
    private final TokenProvider tokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.siteUserRepository.findByEmail(email)
                .orElseThrow(() -> new RacketPuncherException(EMAIL_NOT_FOUND));
    }

    public SiteUser register(SignUpDto signUpDto) {
        boolean exists = this.siteUserRepository.existsByEmail(signUpDto.getEmail());
        if (exists) {
            throw new RacketPuncherException(EMAIL_ALREADY_EXISTED);
        }
        if(AuthType.GENERAL.equals(signUpDto.getAuthType())){
            signUpDto.setPassword(this.passwordEncoder.encode(signUpDto.getPassword()));
        }
        return this.siteUserRepository.save(SiteUser.fromDto(signUpDto));
    }

    public SiteUser withdraw(QuitDto member) {
        var user = this.siteUserRepository.findByEmail(member.getEmail())
                .orElseThrow(() -> new RacketPuncherException(EMAIL_NOT_FOUND));

        if (!this.passwordEncoder.matches(member.getPassword(), user.getPassword())) {
            throw new RacketPuncherException(WRONG_PASSWORD);
        }
        this.siteUserRepository.delete(user);
        return user;
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
}