package com.example.demo.oauth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.example.demo.entity.SiteUser;
import com.example.demo.exception.RacketPuncherException;
import com.example.demo.oauth.dto.AccessTokenDto;
import com.example.demo.oauth.dto.SignUpDto;
import com.example.demo.oauth.security.TokenProvider;
import com.example.demo.siteuser.repository.SiteUserRepository;
import com.example.demo.type.AgeGroup;
import com.example.demo.type.GenderType;
import com.example.demo.type.Ntrp;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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

    private AccessTokenDto getAccessTokenDto() {
        return new AccessTokenDto("accessToken");
    }
}