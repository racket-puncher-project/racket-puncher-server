package com.example.demo.siteuser.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import com.example.demo.apply.repository.ApplyRepository;
import com.example.demo.common.FindEntity;
import com.example.demo.entity.Apply;
import com.example.demo.entity.Matching;
import com.example.demo.entity.Notification;
import com.example.demo.entity.SiteUser;
import com.example.demo.exception.RacketPuncherException;
import com.example.demo.matching.repository.MatchingRepository;
import com.example.demo.notification.repository.NotificationRepository;
import com.example.demo.siteuser.dto.NotificationDto;
import com.example.demo.siteuser.dto.SiteUserInfoDto;
import com.example.demo.siteuser.dto.UpdateSiteUserInfoDto;
import com.example.demo.type.AgeGroup;
import com.example.demo.type.ApplyStatus;
import com.example.demo.type.GenderType;
import com.example.demo.type.Ntrp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.demo.siteuser.repository.SiteUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @Mock
    private PasswordEncoder passwordEncoder;

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

    @Test
    void getMyInfoSuccess() {
        // given
        given(siteUserRepository.findByEmail("email@naver.com"))
                .willReturn(Optional.ofNullable(getSiteUser()));

        // when
        var result = siteUserService.getMyInfo("email@naver.com");

        // then
        assertEquals("email@naver.com", result.getEmail());
    }

    @Test
    void getMyInfoFailedByEmailNotFound() {
        // given
        given(siteUserRepository.findByEmail("email@naver.com"))
                .willReturn(Optional.ofNullable(null));

        // when
        RacketPuncherException exception = assertThrows(RacketPuncherException.class,
                () -> siteUserService.getMyInfo("email@naver.com"));

        // then
        assertEquals(exception.getMessage(), "이메일을 찾을 수 없습니다.");
    }

    @Test
    void updateSiteUserInfoSuccess() {
        // given
        given(siteUserRepository.findByEmail("email@naver.com"))
                .willReturn(Optional.ofNullable(getSiteUser()));

        // when
        var result = siteUserService.updateSiteUserInfo("email@naver.com", getUpdateSiteUserInfoDto());

        // then
        assertEquals("update.png", result.getProfileImg());
    }

    @Test
    void updateKakaoUserInfoSuccess() {
        // given
        given(siteUserRepository.findByEmail("email@naver.com"))
                .willReturn(Optional.ofNullable(getSiteUser()));

        // when
        var result = siteUserService.updateSiteUserInfo("email@naver.com", getUpdateKakaoUserInfoDto());

        // then
        assertEquals("update.png", result.getProfileImg());
    }

    @Test
    void updateSiteUserInfoFailedByEmailNotFound() {
        // given
        given(siteUserRepository.findByEmail("email@naver.com"))
                .willReturn(Optional.ofNullable(null));

        // when
        RacketPuncherException exception = assertThrows(RacketPuncherException.class,
                () -> siteUserService.updateSiteUserInfo("email@naver.com"
                        , getUpdateSiteUserInfoDto()));

        // then
        assertEquals(exception.getMessage(), "이메일을 찾을 수 없습니다.");
    }

    @Test
    void updateSiteUserInfoFailedByWrongPassword() {
        // given
        given(siteUserRepository.findByEmail("email@naver.com"))
                .willReturn(Optional.ofNullable(getSiteUser()));

        // when
        RacketPuncherException exception = assertThrows(RacketPuncherException.class,
                () -> siteUserService.updateSiteUserInfo("email@naver.com"
                        , getWorngUpdateSiteUserInfoDto()));

        // then
        assertEquals(exception.getMessage(), "비밀번호가 일치하지 않습니다.");
    }

    @Test
    void getNotificationsSuccess() {
        // given
        given(siteUserRepository.findByEmail("email@naver.com"))
                .willReturn(Optional.ofNullable(getSiteUser()));
        given(notificationRepository.findAllBySiteUser_Email("email@naver.com"))
                .willReturn(Optional.ofNullable(getNotifications()));

        // when
        var result = siteUserService.getNotifications("email@naver.com");

        // then
        assertEquals(2, result.size());
    }

    @Test
    void getNotificationsFailedByEmailNotFound() {
        // given
        given(siteUserRepository.findByEmail("email@naver.com"))
                .willReturn(Optional.ofNullable(null));

        // when
        RacketPuncherException exception = assertThrows(RacketPuncherException.class,
                () -> siteUserService.getNotifications("email@naver.com"));

        // then
        assertEquals(exception.getMessage(), "이메일을 찾을 수 없습니다.");
    }

    @Test
    void getReviewPageInfoSuccess() {
        // given
        given(siteUserRepository.findByEmail("email@naver.com"))
                .willReturn(Optional.ofNullable(getSiteUser()));
        given(applyRepository.findAllByMatching_IdAndApplyStatus(1L, ApplyStatus.ACCEPTED))
                .willReturn(Optional.ofNullable(getApplies()));

        // when
        var result = siteUserService.getReviewPageInfo("email@naver.com", 1L);

        // then
        assertEquals(2, result.size());
    }

    @Test
    void getReviewPageInfoFailedByEmailNotFound() {
        // given
        given(siteUserRepository.findByEmail("email@naver.com"))
                .willReturn(Optional.ofNullable(null));

        // when
        RacketPuncherException exception = assertThrows(RacketPuncherException.class,
                () -> siteUserService.getReviewPageInfo("email@naver.com", 1L));

        // then
        assertEquals(exception.getMessage(), "이메일을 찾을 수 없습니다.");
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
                .email("email@naver.com")
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

    private UpdateSiteUserInfoDto getUpdateSiteUserInfoDto() {
        return UpdateSiteUserInfoDto.builder()
                .profileImg("update.png")
                .nickname("nickName")
                .password("2222")
                .checkPassword("2222")
                .phoneNumber("010-1234-5678")
                .address("address")
                .zipCode("zipCode")
                .ntrp(Ntrp.BEGINNER)
                .gender(GenderType.FEMALE)
                .ageGroup(AgeGroup.TWENTIES)
                .build();
    }

    private UpdateSiteUserInfoDto getUpdateKakaoUserInfoDto() {
        return UpdateSiteUserInfoDto.builder()
                .profileImg("update.png")
                .nickname("nickName")
                .phoneNumber("010-1234-5678")
                .address("address")
                .zipCode("zipCode")
                .ntrp(Ntrp.BEGINNER)
                .gender(GenderType.FEMALE)
                .ageGroup(AgeGroup.TWENTIES)
                .build();
    }

    private UpdateSiteUserInfoDto getWorngUpdateSiteUserInfoDto() {
        return UpdateSiteUserInfoDto.builder()
                .profileImg("update.png")
                .nickname("nickName")
                .password("2222")
                .checkPassword("3333")
                .phoneNumber("010-1234-5678")
                .address("address")
                .zipCode("zipCode")
                .ntrp(Ntrp.BEGINNER)
                .gender(GenderType.FEMALE)
                .ageGroup(AgeGroup.TWENTIES)
                .build();
    }

    private List<Notification> getNotifications() {
        List<Notification> notifications = new ArrayList<>();

        Notification notification1 = Notification
                .builder()
                .matching(Matching.builder()
                        .id(1L)
                        .title("title1")
                        .build())
                .content("content1")
                .createTime(LocalDateTime.now())
                .build();

        Notification notification2 = Notification
                .builder()
                .matching(Matching.builder()
                        .id(2L)
                        .title("title2")
                        .build())
                .content("content2")
                .createTime(LocalDateTime.now())
                .build();

        notifications.add(notification1);
        notifications.add(notification2);

        return notifications;
    }

    private List<Apply> getApplies() {
        List<Apply> applies = new ArrayList<>();

        Apply apply1 = Apply
                .builder()
                .matching(Matching.builder()
                        .id(1L)
                        .title("title1")
                        .build())
                .siteUser(SiteUser.builder()
                        .id(1L)
                        .siteUserName("주최자")
                        .profileImg("img.png")
                        .nickname("nickName1")
                        .address("address1")
                        .zipCode("zipCode1")
                        .ntrp(Ntrp.BEGINNER)
                        .gender(GenderType.FEMALE)
                        .mannerScore(3.0)
                        .ageGroup(AgeGroup.TWENTIES)
                        .build())
                .applyStatus(ApplyStatus.ACCEPTED)
                .createTime(LocalDateTime.now())
                .build();

        Apply apply2 = Apply
                .builder()
                .matching(Matching.builder()
                        .id(1L)
                        .title("title1")
                        .build())
                .siteUser(SiteUser.builder()
                        .id(2L)
                        .siteUserName("참여자")
                        .profileImg("img.png")
                        .nickname("nickName2")
                        .address("address2")
                        .zipCode("zipCode2")
                        .ntrp(Ntrp.BEGINNER)
                        .gender(GenderType.FEMALE)
                        .mannerScore(3.0)
                        .ageGroup(AgeGroup.TWENTIES)
                        .build())
                .applyStatus(ApplyStatus.ACCEPTED)
                .createTime(LocalDateTime.now())
                .build();

        applies.add(apply1);
        applies.add(apply2);

        return applies;
    }
}
