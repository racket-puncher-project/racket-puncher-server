package com.example.demo.siteuser.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

import com.example.demo.apply.repository.ApplyRepository;
import com.example.demo.common.FindEntity;
import com.example.demo.entity.SiteUser;
import com.example.demo.matching.repository.MatchingRepository;
import com.example.demo.notification.repository.NotificationRepository;
import com.example.demo.siteuser.dto.SiteUserInfoDto;
import com.example.demo.type.AgeGroup;
import com.example.demo.type.GenderType;
import com.example.demo.type.Ntrp;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.demo.siteuser.repository.SiteUserRepository;

@ExtendWith(MockitoExtension.class)
public class SiteUserServiceTest {
    @Mock
    private SiteUserRepository siteUserRepository;

    @Mock
    private MatchingRepository matchingRepository;

    @Mock
    private ApplyRepository applyRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private FindEntity findEntity;

    @InjectMocks
    private SiteUserServiceImpl siteUserService;

    @Test
    void getSiteUserInfoSuccess() {
        // given
        given(findEntity.findUser(1L))
                .willReturn(getSiteUser());

        // when
        var result = siteUserService.getSiteUserInfo(1L);

        // then
        assertEquals(getSiteUserInfoDto().getSiteUserName(), result.getSiteUserName());
    }

    private SiteUserInfoDto getSiteUserInfoDto() {
        return SiteUserInfoDto.builder()
                .profileImg("img.png")
                .siteUserName("userName")
                .nickname("nickName")
                .address("address")
                .zipCode("zipCode")
                .ntrp(Ntrp.BEGINNER)
                .gender(GenderType.FEMALE)
                .mannerScore(3.0)
                .ageGroup(AgeGroup.TWENTIES)
                .build();
    }

    private SiteUser getSiteUser() {
        return SiteUser.builder()
                .id(1L)
                .password("password")
                .nickname("nickName")
                .siteUserName("userName")
                .phoneNumber("010-1234-5678")
                .mannerScore(3.0)
                .gender(GenderType.FEMALE)
                .ntrp(Ntrp.BEGINNER)
                .address("address")
                .zipCode("zipCode")
                .ageGroup(AgeGroup.TWENTIES)
                .profileImg("img.png")
                .isPhoneVerified(true)
                .createDate(LocalDateTime.now())
                .build();
    }

}
