package com.example.demo.oauth.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.example.demo.entity.SiteUser;
import com.example.demo.oauth2.service.ProviderService;
import com.example.demo.oauth.dto.AccessTokenDto;
import com.example.demo.oauth.dto.SignInDto;
import com.example.demo.oauth.dto.SignUpDto;
import com.example.demo.siteuser.repository.SiteUserRepository;
import com.example.demo.oauth.security.JwtAuthenticationFilter;
import com.example.demo.oauth.security.SecurityConfiguration;
import com.example.demo.oauth.security.TokenProvider;
import com.example.demo.oauth.service.AuthService;
import com.example.demo.type.AgeGroup;
import com.example.demo.type.GenderType;
import com.example.demo.type.Ntrp;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(AuthController.class)
@Import(SecurityConfiguration.class)
class AuthControllerTest {
    @MockBean
    private SiteUserRepository siteUserRepository;

    @MockBean
    private RedisTemplate<String, String> redisTemplate;

    @MockBean
    private AuthService authService;

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private ProviderService providerService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void signUp() throws Exception {
        // given
        given(authService.register(getSignUpDto()))
                .willReturn(SiteUser.fromDto(getSignUpDto()));

        // when
        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/sign-up"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    void signIn() throws Exception {
        // given
        given(authService.authenticate(getSignInDto()))
                .willReturn(SiteUser.fromDto(getSignUpDto()));

        // when
        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/sign-in"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    public void testReissue() throws Exception {
        // given
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        String newAccessToken = "newAccessToken";
        String newRefreshToken = "newRefreshToken";
        String username = "username";
        AccessTokenDto accessTokenDto = new AccessTokenDto(accessToken);
        Authentication authentication = mock(Authentication.class);
        ValueOperations<String, String> valueOperations = mock(ValueOperations.class);

        when(authentication.getName()).thenReturn(username);
        when(tokenProvider.getAuthentication(accessToken)).thenReturn(authentication);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(redisTemplate.opsForValue().get(authentication.getName())).thenReturn(refreshToken);
        when(tokenProvider.generateAccessToken(username)).thenReturn(newAccessToken);
        when(redisTemplate.delete(authentication.getName())).thenReturn(null);
        when(tokenProvider.generateAndSaveRefreshToken(authentication.getName())).thenReturn(newRefreshToken);

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/reissue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(accessTokenDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
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

    private SignInDto getSignInDto() {
        return new SignInDto("email@nave.com", "`1234");
    }
}