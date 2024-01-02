package com.example.demo.siteuser.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.demo.entity.SiteUser;
import com.example.demo.exception.RacketPuncherException;
import com.example.demo.siteuser.dto.SignUpDto;
import com.example.demo.siteuser.repository.SiteUserRepository;
import com.example.demo.type.AgeGroup;
import com.example.demo.type.GenderType;
import com.example.demo.type.Ntrp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SiteUserRepository siteUserRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    void registerSuccess() {
        // given
        given(siteUserRepository.existsByEmail(getSignUpDto().getEmail()))
                .willReturn(false);

        ArgumentCaptor<SiteUser> captor = ArgumentCaptor.forClass(SiteUser.class);
        // when
        memberService.register(getSignUpDto());

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
                () -> memberService.register(getSignUpDto()));

        // then
        assertEquals(exception.getMessage(), "이미 사용 중인 이메일입니다.");
    }

}