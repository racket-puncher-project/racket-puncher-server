package com.example.demo.siteuser.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.demo.apply.repository.ApplyRepository;
import com.example.demo.common.FindEntity;
import com.example.demo.entity.Apply;
import com.example.demo.entity.Matching;
import com.example.demo.entity.Notification;
import com.example.demo.entity.Review;
import com.example.demo.entity.SiteUser;
import com.example.demo.exception.RacketPuncherException;
import com.example.demo.matching.repository.MatchingRepository;
import com.example.demo.notification.repository.NotificationRepository;
import com.example.demo.siteuser.dto.InputReviewDto;
import com.example.demo.siteuser.dto.SiteUserInfoDto;
import com.example.demo.siteuser.dto.UpdateSiteUserInfoDto;
import com.example.demo.siteuser.repository.ReviewRepository;
import com.example.demo.type.AgeGroup;
import com.example.demo.type.ApplyStatus;
import com.example.demo.type.GenderType;
import com.example.demo.type.MatchingType;
import com.example.demo.type.NegativeReviewType;
import com.example.demo.type.Ntrp;
import com.example.demo.type.PositiveReviewType;
import com.example.demo.type.RecruitStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
    private ReviewRepository reviewRepository;

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
                .willReturn(Optional.empty());

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
                .willReturn(Optional.empty());

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
                .willReturn(getNotifications());

        // when
        var result = siteUserService.getNotifications("email@naver.com");

        // then
        assertEquals(2, result.size());
    }

    @Test
    void getNotificationsFailedByEmailNotFound() {
        // given
        given(siteUserRepository.findByEmail("email@naver.com"))
                .willReturn(Optional.empty());

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
                .willReturn(getApplies());

        // when
        var result = siteUserService.getReviewPageInfo("email@naver.com", 1L);

        // then
        assertEquals(2, result.size());
    }

    @Test
    void getReviewPageInfoFailedByEmailNotFound() {
        // given
        given(siteUserRepository.findByEmail("email@naver.com"))
                .willReturn(Optional.empty());

        // when
        RacketPuncherException exception = assertThrows(RacketPuncherException.class,
                () -> siteUserService.getReviewPageInfo("email@naver.com", 1L));

        // then
        assertEquals(exception.getMessage(), "이메일을 찾을 수 없습니다.");
    }

    @Test
    void reviewSuccess() {
        // given
        given(siteUserRepository.findByEmail("email@naver.com"))
                .willReturn(Optional.ofNullable(getSiteUser()));
        given(findEntity.findMatching(1L))
                .willReturn(getMatching());
        given(findEntity.findUser(2L))
                .willReturn(getObjectUser1());
        given(findEntity.findUser(3L))
                .willReturn(getObjectUser2());

        // when
        ArgumentCaptor<Review> captor = ArgumentCaptor.forClass(Review.class);
        siteUserService.review("email@naver.com", 1L, getInputReviewDtoList());
        // then
        verify(reviewRepository, times(2)).save(captor.capture());
    }

    @Test
    void reviewFailedByEmailNotFound() {
        // given
        given(siteUserRepository.findByEmail("email@naver.com"))
                .willReturn(Optional.empty());

        // when
        RacketPuncherException exception = assertThrows(RacketPuncherException.class,
                () -> siteUserService.review("email@naver.com", 1L, getInputReviewDtoList()));

        // then
        assertEquals(exception.getMessage(), "이메일을 찾을 수 없습니다.");
    }

    @Test
    void getMatchingHostedBySiteUser() {
        // given
        SiteUser siteUser = getSiteUser();
        Matching matching = getMatching();
        Apply apply = getApply();
        given(siteUserRepository.findByEmail(siteUser.getEmail()))
                .willReturn(Optional.of(siteUser));
        given(matchingRepository.findAllBySiteUser_Email(siteUser.getEmail()))
                .willReturn(List.of(matching));
        given(applyRepository.findBySiteUser_IdAndMatching_Id(siteUser.getId(), matching.getId()))
                .willReturn(Optional.of(apply));
        given(applyRepository.findAllByMatching_Id(matching.getId()))
                .willReturn(List.of(apply));

        // when
        var result = siteUserService.getMatchingHostedBySiteUser(siteUser.getEmail());

        // then
        assertNotNull(result);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getId(), matching.getId());
    }

    @Test
    void getMatchingAppliedBySiteUser() {
        // given
        SiteUser siteUser = getSiteUser();
        Matching matching = getMatching();
        Apply apply = getApply();
        given(siteUserRepository.findByEmail(siteUser.getEmail()))
                .willReturn(Optional.of(siteUser));
        given(applyRepository.findAllBySiteUser_Email(siteUser.getEmail()))
                .willReturn(List.of(apply));
        given(applyRepository.findAllByMatching_Id(matching.getId()))
                .willReturn(List.of(apply));

        // when
        var result = siteUserService.getMatchingAppliedBySiteUser(siteUser.getEmail());

        // then
        assertNotNull(result);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getId(), matching.getId());
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
                .mannerScore(3)
                .ageGroup(AgeGroup.TWENTIES)
                .build();
    }

    private Matching getMatching() {
        return Matching.builder()
                .id(1L)
                .title("title")
                .acceptedNum(4)
                .date(LocalDate.now())
                .recruitDueDateTime(LocalDateTime.now())
                .matchingType(MatchingType.DOUBLE)
                .siteUser(getSiteUser())
                .recruitStatus(RecruitStatus.CONFIRMED)
                .content("content")
                .location("서울시 테니스구")
                .cost(40000)
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
                .mannerScore(3)
                .gender(GenderType.FEMALE)
                .ntrp(Ntrp.BEGINNER)
                .address("address")
                .zipCode("zipCode")
                .ageGroup(AgeGroup.TWENTIES)
                .profileImg("img.png")
                .createDate(LocalDateTime.now())
                .build();
    }

    private SiteUser getObjectUser1() {
        return SiteUser.builder()
                .id(2L)
                .email("email2@naver.com")
                .password("password")
                .nickname("nickName2")
                .siteUserName("userName3")
                .phoneNumber("010-1234-5678")
                .mannerScore(3)
                .gender(GenderType.FEMALE)
                .ntrp(Ntrp.BEGINNER)
                .address("address")
                .zipCode("zipCode")
                .ageGroup(AgeGroup.TWENTIES)
                .profileImg("img.png")
                .createDate(LocalDateTime.now())
                .build();
    }

    private SiteUser getObjectUser2() {
        return SiteUser.builder()
                .id(1L)
                .email("email3@naver.com")
                .password("password")
                .nickname("nickName3")
                .siteUserName("userName3")
                .phoneNumber("010-1234-5678")
                .mannerScore(3)
                .gender(GenderType.FEMALE)
                .ntrp(Ntrp.BEGINNER)
                .address("address")
                .zipCode("zipCode")
                .ageGroup(AgeGroup.TWENTIES)
                .profileImg("img.png")
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
                        .mannerScore(3)
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
                        .mannerScore(3)
                        .ageGroup(AgeGroup.TWENTIES)
                        .build())
                .applyStatus(ApplyStatus.ACCEPTED)
                .createTime(LocalDateTime.now())
                .build();

        applies.add(apply1);
        applies.add(apply2);

        return applies;
    }

    private List<InputReviewDto> getInputReviewDtoList() {
        List<InputReviewDto> inputReviewDtoList = new ArrayList<>();

        InputReviewDto inputReviewDto1 = InputReviewDto
                .builder()
                .objectUserId(2L)
                .positiveReviews(List.of(PositiveReviewType.PROACTIVE))
                .negativeReviews(List.of(NegativeReviewType.AGGRESSIVE))
                .build();

        InputReviewDto inputReviewDto2 = InputReviewDto
                .builder()
                .objectUserId(3L)
                .positiveReviews(List.of(PositiveReviewType.PROACTIVE))
                .negativeReviews(List.of(NegativeReviewType.AGGRESSIVE))
                .build();

        inputReviewDtoList.add(inputReviewDto1);
        inputReviewDtoList.add(inputReviewDto2);

        return inputReviewDtoList;
    }

    private Apply getApply() {
        return Apply.builder()
                .matching(Matching.builder()
                        .id(1L)
                        .title("title1")
                        .location("서울시 테니스구")
                        .date(LocalDate.now())
                        .siteUser(SiteUser.builder()
                                .id(2L)
                                .build())
                        .build())
                .siteUser(SiteUser.builder()
                        .id(1L)
                        .profileImg("img.png")
                        .nickname("nickName1")
                        .build())
                .applyStatus(ApplyStatus.ACCEPTED)
                .createTime(LocalDateTime.now())
                .build();
    }
}
