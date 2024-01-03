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

//    @Test
//    public void testReissue() throws Exception {
//        // given
//        String accessToken = "accessToken";
//        String refreshToken = "refreshToken";
//        String newAccessToken = "newAccessToken";
//        String newRefreshToken = "newRefreshToken";
//        String username = "username";
//        AccessTokenDto accessTokenDto = new AccessTokenDto(accessToken);
//        Authentication authentication = mock(Authentication.class);
//        ValueOperations<String, String> valueOperations = mock(ValueOperations.class);
//
//        when(authentication.getName()).thenReturn(username);
//        when(tokenProvider.getAuthentication(accessToken)).thenReturn(authentication);
//        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
//        when(redisTemplate.opsForValue().get(authentication.getName())).thenReturn(refreshToken);
//        when(tokenProvider.generateAccessToken(username)).thenReturn(newAccessToken);
//        when(redisTemplate.delete(authentication.getName())).thenReturn(null);
//        when(tokenProvider.generateAndSaveRefreshToken(authentication.getName())).thenReturn(newRefreshToken);
//
//        // when
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/reissue")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(accessTokenDto)))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(print());
//    }
}