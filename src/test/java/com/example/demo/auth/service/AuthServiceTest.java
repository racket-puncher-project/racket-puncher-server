package com.example.demo.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.demo.auth.dto.*;
import com.example.demo.entity.SiteUser;
import com.example.demo.exception.RacketPuncherException;
import com.example.demo.auth.security.TokenProvider;
import com.example.demo.siteuser.repository.SiteUserRepository;
import com.example.demo.type.AgeGroup;
import com.example.demo.type.AuthType;
import com.example.demo.type.GenderType;
import com.example.demo.type.Ntrp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SiteUserRepository siteUserRepository;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @InjectMocks
    private AuthService authService;

    @Test
    void registerSuccess() {
        // given
        given(siteUserRepository.existsByEmail(getSignUpDto().getEmail()))
                .willReturn(false);

        ArgumentCaptor<SiteUser> captor = ArgumentCaptor.forClass(SiteUser.class);
        // when
        authService.register(getSignUpDto());

        // then
        verify(siteUserRepository, times(1)).save(captor.capture());
    }

    private SignUpDto getSignUpDto() {
        return SignUpDto.builder()
                .email("email@naver.com")
                .password("1234")
                .nickname("닉네임")
                .phoneNumber("010-1234-5678")
                .gender(GenderType.FEMALE)
                .siteUserName("홍길동")
                .ntrp(Ntrp.BEGINNER)
                .address("삼성동")
                .zipCode("12345")
                .profileImg("test.url")
                .ageGroup(AgeGroup.TWENTIES)
                .authType(AuthType.GENERAL)
                .build();
    }

    @Test
    void registerFailedByAlreadyExistedEmail() {
        // given
        given(siteUserRepository.existsByEmail(getSignUpDto().getEmail()))
                .willReturn(true);

        // when
        RacketPuncherException exception = assertThrows(RacketPuncherException.class,
                () -> authService.register(getSignUpDto()));

        // then
        assertEquals(exception.getMessage(), "이미 사용 중인 이메일입니다.");
    }

    @Test
    public void tokenReissueSuccess() {
        // given
        Authentication authentication = mock(Authentication.class);
        ValueOperations<String, String> valueOperations = mock(ValueOperations.class);

        given(authentication.getName()).willReturn("username");
        given(tokenProvider.getAuthentication("accessToken")).willReturn(authentication);
        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(redisTemplate.opsForValue().get(authentication.getName())).willReturn("refreshToken");
        given(tokenProvider.generateAccessToken("username")).willReturn("newAccessToken");
        given(redisTemplate.delete(authentication.getName())).willReturn(null);
        given(tokenProvider.generateAndSaveRefreshToken(authentication.getName())).willReturn("newRefreshToken");

        // when
        AccessTokenDto newAccessToken = authService.tokenReissue(new AccessTokenDto("accessToken"));

        // then
        assertEquals("newAccessToken", newAccessToken.getAccessToken());
    }

    @Test
    public void signOutSuccess() {
        // given
        ValueOperations<String, String> valueOperations = mock(ValueOperations.class);

        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(tokenProvider.getUserEmail(getAccessTokenDto().getAccessToken())).willReturn("email");
        given(redisTemplate.opsForValue().get("email")).willReturn("refreshToken");
        given(redisTemplate.delete(tokenProvider.getUserEmail(getAccessTokenDto().getAccessToken())))
                .willReturn(null);
        // when
        String result = authService.signOut(getAccessTokenDto());

        // then
        assertEquals("로그아웃 성공", result);
    }

    @Test
    public void signOutFailedByRefreshTokenExpired() {
        // given
        ValueOperations<String, String> valueOperations = mock(ValueOperations.class);

        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(tokenProvider.getUserEmail(getAccessTokenDto().getAccessToken())).willReturn("email");
        given(redisTemplate.opsForValue().get("email")).willReturn(null);

        // when
        RacketPuncherException exception = assertThrows(RacketPuncherException.class,
                () -> authService.signOut(getAccessTokenDto()));

        // then
        assertEquals(exception.getMessage(), "리프레시 토큰이 만료되었습니다.");
    }

    @Test
    void checkEmailSuccess() {
        // given
        given(siteUserRepository.existsByEmail("email"))
                .willReturn(false);

        // when
        var result = authService.checkEmail("email");

        // then
        assertEquals("사용 가능한 이메일입니다.", result.getMessage());
    }

    @Test
    public void checkEmailFailed() {
        // given
        given(siteUserRepository.existsByEmail("email"))
                .willReturn(true);
        // when
        RacketPuncherException exception = assertThrows(RacketPuncherException.class,
                () -> authService.checkEmail("email"));

        // then
        assertEquals(exception.getMessage(), "이미 사용 중인 이메일입니다.");
    }

    @Test
    void checkNicknameSuccess() {
        // given
        given(siteUserRepository.existsByNickname("nickname"))
                .willReturn(false);

        // when
        var result = authService.checkNickname("nickname");

        // then
        assertEquals("사용 가능한 닉네임입니다.", result.getMessage());
    }

    @Test
    public void checkNicknameFailed() {
        // given
        given(siteUserRepository.existsByNickname("nickname"))
                .willReturn(true);
        // when
        RacketPuncherException exception = assertThrows(RacketPuncherException.class,
                () -> authService.checkNickname("nickname"));

        // then
        assertEquals(exception.getMessage(), "이미 사용 중인 닉네임입니다.");
    }

    @Test
    public void withdrawGeneralUserSuccess() {
        // given
        SiteUser generalSiteUser = getGeneralSiteUser();
        ValueOperations<String, String> valueOperations = mock(ValueOperations.class);

        given(siteUserRepository.findByEmail(generalSiteUser.getEmail()))
                .willReturn(Optional.of(generalSiteUser));
        given(passwordEncoder.matches("password", generalSiteUser.getPassword()))
                .willReturn(true);
        given(redisTemplate.delete(generalSiteUser.getEmail()))
                .willReturn(null);

        // when
        String result = authService.withdraw(generalSiteUser.getEmail(), "password");

        // then
        assertEquals(result, "탈퇴 성공");
    }

    @Test
    public void withdrawKakaoUserSuccess() {
        // given
        SiteUser kakaoSiteUser = getKakaoSiteUser();
        ValueOperations<String, String> valueOperations = mock(ValueOperations.class);

        given(siteUserRepository.findByEmail(kakaoSiteUser.getEmail()))
                .willReturn(Optional.of(kakaoSiteUser));
        given(redisTemplate.delete(kakaoSiteUser.getEmail()))
                .willReturn(null);

        // when
        String result = authService.withdraw(kakaoSiteUser.getEmail(), "");

        // then
        assertEquals(result, "탈퇴 성공");
    }

    @Test
    public void withdrawGeneralUserFailedByWrongPassword() {
        // given
        SiteUser generalSiteUser = getGeneralSiteUser();

        given(siteUserRepository.findByEmail(generalSiteUser.getEmail()))
                .willReturn(Optional.of(generalSiteUser));
        given(passwordEncoder.matches("wrongPassword", generalSiteUser.getPassword()))
                .willReturn(false);

        // when
        RacketPuncherException exception = assertThrows(RacketPuncherException.class,
                () -> authService.withdraw(generalSiteUser.getEmail(), "wrongPassword"));

        // then
        assertEquals("비밀번호가 일치하지 않습니다.", exception.getMessage());
    }

    @Test
    public void findEmailSuccessForGeneralUser() {
        // given
        SiteUser generalSiteUser = getGeneralSiteUser();

        given(siteUserRepository.findByPhoneNumber(generalSiteUser.getPhoneNumber()))
                .willReturn(Optional.of(generalSiteUser));

        // when
        FindEmailResponseDto result = authService.findEmail(generalSiteUser.getPhoneNumber());

        // then
        assertEquals(AuthType.GENERAL, result.getAuthType());
        assertEquals(generalSiteUser.getEmail(), result.getEmail());
    }

    @Test
    public void findEmailSuccessForKakaoUser() {
        // given
        SiteUser kakaoSiteUser = getKakaoSiteUser();

        given(siteUserRepository.findByPhoneNumber(kakaoSiteUser.getPhoneNumber()))
                .willReturn(Optional.of(kakaoSiteUser));

        // when
        FindEmailResponseDto result = authService.findEmail(kakaoSiteUser.getPhoneNumber());

        // then
        assertEquals(AuthType.KAKAO, result.getAuthType());
        assertEquals("", result.getEmail());
    }

    @Test
    public void findEmailFailByUserNotFound() {
        // given
        String wrongPhoneNumber = "wrong phone number";
        given(siteUserRepository.findByPhoneNumber(wrongPhoneNumber))
                .willReturn(Optional.empty());

        // when
        RacketPuncherException exception = assertThrows(RacketPuncherException.class,
                () -> authService.findEmail(wrongPhoneNumber));
        // then
        assertEquals(exception.getMessage(), "가입 정보가 없습니다.");
    }

    @Test
    public void verifyUserForResetPasswordSuccessForGeneralUser() {
        // given
        SiteUser generalSiteUser = getGeneralSiteUser();
        given(siteUserRepository.findByEmailAndPhoneNumber(generalSiteUser.getEmail(), generalSiteUser.getPhoneNumber()))
                .willReturn(Optional.of(generalSiteUser));
        given(tokenProvider.generateAccessToken(generalSiteUser.getEmail()))
                .willReturn("ResetToken");

        // when
        ResetTokenDto result = authService.verifyUserForResetPassword(generalSiteUser.getEmail(), generalSiteUser.getPhoneNumber());

        // then
        assertEquals(AuthType.GENERAL, result.getAuthType());
        assertEquals("ResetToken", result.getResetToken());
    }

    @Test
    public void verifyUserForResetPasswordFailByUserNotFound() {
        // given
        given(siteUserRepository.findByEmailAndPhoneNumber("WrongEmail", "WrongPhoneNumber"))
                .willReturn(Optional.empty());

        // when
        RacketPuncherException exception = assertThrows(RacketPuncherException.class,
                () -> authService.verifyUserForResetPassword("WrongEmail", "WrongPhoneNumber"));

        // then
        assertEquals(exception.getMessage(), "가입 정보가 없습니다.");
    }

    @Test
    public void verifyUserForResetPasswordSuccessForKakaoUser() {
        // given
        SiteUser kakaoSiteUser = getKakaoSiteUser();
        given(siteUserRepository.findByEmailAndPhoneNumber(kakaoSiteUser.getEmail(), kakaoSiteUser.getPhoneNumber()))
                .willReturn(Optional.of(kakaoSiteUser));

        // when
        ResetTokenDto result = authService.verifyUserForResetPassword(kakaoSiteUser.getEmail(), kakaoSiteUser.getPhoneNumber());

        // then
        assertEquals(AuthType.KAKAO, result.getAuthType());
        assertEquals("", result.getResetToken());
    }

    @Test
    public void resetPassword() {
        // given
        String resetToken = "ResetToken";
        SiteUser siteUser = getGeneralSiteUser();
        given(tokenProvider.validateResetToken(resetToken))
                .willReturn(true);
        given(tokenProvider.getUserEmail(resetToken))
                .willReturn(siteUser.getEmail());
        given(siteUserRepository.findByEmail(siteUser.getEmail()))
                .willReturn(Optional.of(siteUser));

        // when
        StringResponseDto result = authService.resetPassword(resetToken, "NewPassword");

        // then
        assertEquals(result.getMessage(), "비밀번호 초기화 성공");
    }

    private AccessTokenDto getAccessTokenDto() {
        return new AccessTokenDto("accessToken");
    }

    private SiteUser getGeneralSiteUser() {
        return SiteUser.builder()
                .id(1L)
                .email("email@naver.com")
                .password(passwordEncoder.encode("password"))
                .nickname("nickName")
                .siteUserName("userName")
                .phoneNumber("010-1234-5678")
                .mannerScore(3)
                .gender(GenderType.FEMALE)
                .ntrp(Ntrp.BEGINNER)
                .address("address")
                .zipCode("zipCode")
                .ageGroup(AgeGroup.TWENTIES)
                .profileImg("img.png")
                .createDate(LocalDateTime.now())
                .authType(AuthType.GENERAL)
                .build();
    }

    private SiteUser getKakaoSiteUser() {
        return SiteUser.builder()
                .id(1L)
                .email("email@naver.com")
                .password("")
                .nickname("nickName")
                .siteUserName("userName")
                .phoneNumber("010-1234-5678")
                .mannerScore(3)
                .gender(GenderType.FEMALE)
                .ntrp(Ntrp.BEGINNER)
                .address("address")
                .zipCode("zipCode")
                .ageGroup(AgeGroup.TWENTIES)
                .profileImg("img.png")
                .createDate(LocalDateTime.now())
                .authType(AuthType.KAKAO)
                .build();
    }
}