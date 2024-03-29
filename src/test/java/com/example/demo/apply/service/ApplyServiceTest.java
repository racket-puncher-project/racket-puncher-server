package com.example.demo.apply.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.demo.apply.repository.ApplyRepository;
import com.example.demo.common.FindEntity;
import com.example.demo.entity.Apply;
import com.example.demo.entity.Matching;
import com.example.demo.entity.SiteUser;
import com.example.demo.exception.RacketPuncherException;
import com.example.demo.notification.service.NotificationService;
import com.example.demo.siteuser.repository.SiteUserRepository;
import com.example.demo.type.AgeGroup;
import com.example.demo.type.ApplyStatus;
import com.example.demo.type.GenderType;
import com.example.demo.type.MatchingType;
import com.example.demo.type.Ntrp;
import com.example.demo.type.RecruitStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.Commit;

@ExtendWith(MockitoExtension.class)
class ApplyServiceTest {

    @Mock
    private ApplyRepository applyRepository;

    @Mock
    private FindEntity findEntity;

    @Mock
    private NotificationService notificationService;

    @Mock
    private SiteUserRepository siteUserRepository;

    @InjectMocks
    private ApplyServiceImpl applyService;

    @Test
    void applySuccess() {
        //given
        SiteUser siteUser = getSiteUser();

        Matching matching = getMatching(siteUser);

        given(siteUserRepository.findByEmail("emial@gmail.com"))
                .willReturn(Optional.ofNullable(siteUser));

        given(findEntity.findMatching(1L))
                .willReturn(matching);

        ArgumentCaptor<Apply> captor = ArgumentCaptor.forClass(Apply.class);

        // when
        applyService.apply("emial@gmail.com", 1L);

        // then
        verify(applyRepository, times(1)).save(captor.capture()); // save 메서드가 한 번 실행되는 지 검증
    }

    @Test
    void applyFailByDuplication() {
        //given
        SiteUser siteUser = getSiteUser(); // 1L

        SiteUser siteUserForApply = getSiteUserForApply(); // 2L

        Matching matching = getMatching(siteUser);

        Apply apply = getApply(matching, siteUserForApply);

        given(siteUserRepository.findByEmail("emial2@gmail.com"))
                .willReturn(Optional.ofNullable(siteUserForApply));

        given(findEntity.findMatching(1L))
                .willReturn(matching);

        given(applyRepository.existsBySiteUser_IdAndMatching_Id(2L, 1L))
                .willReturn(true);

        given(applyRepository.findBySiteUser_IdAndMatching_Id(2L, 1L))
                .willReturn(Optional.of(apply));

        // when
        RacketPuncherException exception = assertThrows(RacketPuncherException.class,
                () -> applyService.apply("emial2@gmail.com", 1L));

        // then
        assertEquals(exception.getMessage(), "이미 참가 신청한 경기입니다.");
    }

    @Test
    void applyFailByRecruitClosed() {
        //given
        SiteUser siteUser = getSiteUser();

        SiteUser siteUserForApply = getSiteUserForApply();

        Matching matching = getClosedMatching(siteUser);

        given(siteUserRepository.findByEmail("emial2@gmail.com"))
                .willReturn(Optional.ofNullable(siteUserForApply));

        given(findEntity.findMatching(1L))
                .willReturn(matching);

        // when
        RacketPuncherException exception = assertThrows(RacketPuncherException.class,
                () -> applyService.apply("emial2@gmail.com", 1L));

        // then
        assertEquals(exception.getMessage(), "이미 매칭이 확정된 경기입니다.");
    }

    @Test
    @Commit
    void applyCancelSuccess() {
        //given
        SiteUser siteUser = getSiteUser();

        SiteUser siteUserForApply = getSiteUserForApply();

        Matching matching = getMatching(siteUser);

        Apply apply = getApply(matching, siteUserForApply);

        given(findEntity.findApply(1L))
                .willReturn(apply);

        // when
        applyService.cancel(1L);

        // then
        assertEquals(ApplyStatus.CANCELED, apply.getApplyStatus());
    }

    @Test
    void applyCancelFailByClosedMatching() {
        // given
        SiteUser siteUser = getSiteUser();

        SiteUser siteUserForApply = getSiteUserForApply();

        Matching matching = getClosedMatching(siteUser);

        Apply apply = getApply(matching, siteUserForApply);

        given(findEntity.findApply(1L))
                .willReturn(apply);
        // when
        RacketPuncherException exception = assertThrows(RacketPuncherException.class,
                () -> applyService.cancel(1L));

        // then
        assertEquals(exception.getMessage(), "이미 매칭이 확정된 경기입니다.");
    }

    @Test
    void applyCancelFailByYourOwnPosting() {
        // given
        SiteUser siteUser = getSiteUser();

        Matching matching = getClosedMatching(siteUser);

        Apply apply = getApply(matching, siteUser);

        apply.changeApplyStatus(ApplyStatus.ACCEPTED);


        given(findEntity.findApply(1L))
                .willReturn(apply);

        // when
        RacketPuncherException exception = assertThrows(RacketPuncherException.class,
                () -> applyService.cancel(1L));

        // then
        assertEquals("본인이 주최한 경기는 참가 신청 취소를 할 수 없습니다.", exception.getMessage());
    }

    @Test
    void applyCancelFailedByCancelDuplication() {
        // given
        SiteUser siteUser = getSiteUser();

        SiteUser siteUserForApply = getSiteUserForApply();

        Matching matching = getClosedMatching(siteUser);

        Apply apply = getCancelApply(matching, siteUserForApply);

        given(findEntity.findApply(1L))
                .willReturn(apply);
        // when
        RacketPuncherException exception = assertThrows(RacketPuncherException.class,
                () -> applyService.cancel(1L));

        // then
        assertEquals(exception.getMessage(), "해당 매칭에 대한 참가 신청은 이미 취소되었습니다.");
    }

    @Test
    void applyAcceptSuccess() {
        //given
        given(findEntity.findMatching(1L))
                .willReturn(Matching.builder()
                        .id(1L)
                        .siteUser(getSiteUser())
                        .recruitStatus(RecruitStatus.OPEN)
                        .recruitNum(4)
                        .date(LocalDate.now())
                        .build());

        List<Long> appliedList = new ArrayList<>();
        appliedList.add(1L);

        List<Long> confirmedList = new ArrayList<>();
        confirmedList.add(2L);

        given(findEntity.findApply(1L))
                .willReturn(Apply.builder()
                        .id(1L)
                        .build());

        given(findEntity.findApply(2L))
                .willReturn(Apply.builder()
                        .id(2L)
                        .build());

        // when
        applyService.accept(getSiteUser().getEmail(), appliedList, confirmedList, 1L);

        // then
        verify(findEntity, times(3)).findApply(anyLong());
    }

    @Test
    void applyAcceptFailedByOverRecruitNumber() {
        //given
        given(findEntity.findMatching(1L))
                .willReturn(Matching.builder()
                        .id(1L)
                        .siteUser(getSiteUser())
                        .recruitStatus(RecruitStatus.OPEN)
                        .recruitNum(2)
                        .date(LocalDate.now())
                        .build());

        List<Long> appliedList = new ArrayList<>();
        appliedList.add(1L);

        List<Long> confirmedList = new ArrayList<>();
        confirmedList.add(1L);
        confirmedList.add(3L);
        confirmedList.add(4L);
        confirmedList.add(5L);

        // when
        RacketPuncherException exception = assertThrows(RacketPuncherException.class,
                () -> applyService.accept(getSiteUser().getEmail(), appliedList, confirmedList, 1L));

        // then
        assertEquals("모집 인원보다 많은 인원을 수락할 수 없습니다.", exception.getMessage());
    }

    @Test
    void applyAcceptFailedByPermissionDenied() {
        //given
        given(findEntity.findMatching(1L))
                .willReturn(Matching.builder()
                        .id(1L)
                        .siteUser(getSiteUser())
                        .recruitStatus(RecruitStatus.OPEN)
                        .recruitNum(4)
                        .date(LocalDate.now())
                        .build());

        List<Long> appliedList = new ArrayList<>();
        appliedList.add(1L);

        List<Long> confirmedList = new ArrayList<>();
        confirmedList.add(1L);
        confirmedList.add(3L);
        confirmedList.add(4L);
        confirmedList.add(5L);

        // when
        RacketPuncherException exception = assertThrows(RacketPuncherException.class,
                () -> applyService.accept("email@email.com", appliedList, confirmedList, 1L));

        // then
        assertEquals("참가 신청을 수락할 권한이 없습니다.", exception.getMessage());
    }

    private static Apply getApply(Matching matching, SiteUser siteUser) {
        return Apply.builder()
                .id(1L)
                .matching(matching)
                .siteUser(siteUser)
                .applyStatus(ApplyStatus.PENDING)
                .build();
    }

    private Apply getCancelApply(Matching matching, SiteUser siteUser) {
        return Apply.builder()
                .id(1L)
                .matching(matching)
                .siteUser(siteUser)
                .applyStatus(ApplyStatus.CANCELED)
                .build();
    }

    private SiteUser getSiteUser() {
        return SiteUser.builder()
                .id(1L)
                .password("1234")
                .nickname("nickname")
                .email("emial@gmail.com")
                .phoneNumber("010-1234-5678")
                .gender(GenderType.FEMALE)
                .ntrp(Ntrp.ADVANCE)
                .address("안양시")
                .zipCode("12345")
                .ageGroup(AgeGroup.TWENTIES)
                .createDate(LocalDateTime.now())
                .build();
    }

    private SiteUser getSiteUserForApply() {
        return SiteUser.builder()
                .id(2L)
                .password("1234")
                .nickname("nickname")
                .email("emial2@gmail.com")
                .phoneNumber("010-1234-5678")
                .gender(GenderType.FEMALE)
                .ntrp(Ntrp.ADVANCE)
                .address("안양시")
                .zipCode("12345")
                .ageGroup(AgeGroup.TWENTIES)
                .createDate(LocalDateTime.now())
                .build();
    }

    private Matching getMatching(SiteUser siteUser) {
        return Matching.builder()
                .id(1L)
                .siteUser(siteUser)
                .title("title")
                .content("content")
                .location("location")
                .date(LocalDate.of(2023, 11, 15))
                .startTime(LocalTime.of(10, 00))
                .endTime(LocalTime.of(12, 00))
                .recruitNum(4)
                .cost(1000)
                .isReserved(true)
                .ntrp(Ntrp.ADVANCE)
                .age(AgeGroup.FORTIES)
                .recruitStatus(RecruitStatus.OPEN)
                .createTime(LocalDateTime.now())
                .matchingType(MatchingType.DOUBLE)
                .acceptedNum(2)
                .build();
    }

    private static Matching getClosedMatching(SiteUser siteUser) {
        return Matching.builder()
                .id(1L)
                .siteUser(siteUser)
                .title("title")
                .content("content")
                .location("location")
                .date(LocalDate.of(2023, 11, 15))
                .startTime(LocalTime.of(10, 00))
                .endTime(LocalTime.of(12, 00))
                .recruitNum(4)
                .cost(1000)
                .isReserved(true)
                .ntrp(Ntrp.ADVANCE)
                .age(AgeGroup.FORTIES)
                .recruitStatus(RecruitStatus.CONFIRMED)
                .createTime(LocalDateTime.now())
                .matchingType(MatchingType.DOUBLE)
                .build();
    }
}