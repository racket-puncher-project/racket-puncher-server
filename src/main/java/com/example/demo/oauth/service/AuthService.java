package com.example.demo.oauth.service;

import static com.example.demo.exception.type.ErrorCode.EMAIL_ALREADY_EXISTED;
import static com.example.demo.exception.type.ErrorCode.EMAIL_NOT_FOUND;
import static com.example.demo.exception.type.ErrorCode.REFRESH_TOKEN_EXPIRED;
import static com.example.demo.exception.type.ErrorCode.WRONG_PASSWORD;

import com.example.demo.entity.SiteUser;
import com.example.demo.exception.RacketPuncherException;
import com.example.demo.oauth.dto.AccessTokenDto;
import com.example.demo.oauth.dto.QuitDto;
import com.example.demo.oauth.dto.SignInDto;
import com.example.demo.oauth.dto.SignUpDto;
import com.example.demo.oauth.security.TokenProvider;
import com.example.demo.siteuser.repository.SiteUserRepository;
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
        signUpDto.setPassword(this.passwordEncoder.encode(signUpDto.getPassword()));
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

    public AccessTokenDto getNewAccessTokenAndSaveNewRefreshToken(AccessTokenDto accessTokenDto) {
        Authentication authentication = tokenProvider.getAuthentication(accessTokenDto.getAccessToken());
        String refreshToken = redisTemplate.opsForValue().get(authentication.getName());
        if (ObjectUtils.isEmpty(refreshToken)) {
            throw new RacketPuncherException(REFRESH_TOKEN_EXPIRED);
        }
        var newAccessToken = tokenProvider.generateAccessToken(authentication.getName());
        redisTemplate.delete(authentication.getName());
        tokenProvider.generateAndSaveRefreshToken(authentication.getName());
        return new AccessTokenDto(newAccessToken);
    }

    public boolean isEmailExist(String email) {

        return siteUserRepository.existsByEmail(email);
    }

    public boolean isNicknameExist(String nickname) {
        return siteUserRepository.existsByNickname(nickname);
    }
}
