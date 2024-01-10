package com.example.demo.matching.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.example.demo.apply.dto.ApplyDto;
import com.example.demo.apply.repository.ApplyRepository;
import com.example.demo.common.FindEntity;
import com.example.demo.entity.Apply;
import com.example.demo.entity.Matching;
import com.example.demo.entity.SiteUser;
import com.example.demo.exception.RacketPuncherException;
import com.example.demo.matching.dto.MatchingDetailRequestDto;
import com.example.demo.matching.dto.MatchingPreviewDto;
import com.example.demo.matching.repository.MatchingRepository;
import com.example.demo.notification.service.NotificationService;
import com.example.demo.siteuser.repository.SiteUserRepository;
import com.example.demo.type.AgeGroup;
import com.example.demo.type.ApplyStatus;
import com.example.demo.type.GenderType;
import com.example.demo.type.MatchingType;
import com.example.demo.type.Ntrp;
import com.example.demo.type.RecruitStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;

@ExtendWith(MockitoExtension.class)
class MatchingServiceImplTest {
    @Mock
    private MatchingRepository matchingRepository;

    @Mock
    private SiteUserRepository siteUserRepository;

    @Mock
    private ApplyRepository applyRepository;

    @Mock
    FindEntity findEntity;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private MatchingServiceImpl matchingService;

    // 카카오에서 lat,lon을 가져오는 부분에서 에러가 나는 듯 합니다. 확인 부탁드립니다.
//    @Test
//    @DisplayName("유저 아이디에 해당하는 유저와 함께 매칭이 저장됨")
//    void create() {
//        //given
//        SiteUser siteUser = makeSiteUser();
//        MatchingDetailRequestDto matchingDetailRequestDto = makeMatchingDetailDto();
//        ApplyDto applyDto = makeApplyDto(siteUser, Matching.fromDto(matchingDetailRequestDto, siteUser));
//        given(siteUserRepository.findByEmail(("example@example.com")))
//                .willReturn(Optional.of(siteUser));
//        given(matchingRepository.save(any(Matching.class)))
//                .willReturn(Matching.fromDto(matchingDetailRequestDto, siteUser));
//        given(applyRepository.save(any(Apply.class)))
//                .willReturn(Apply.fromDto(applyDto));
//
//        //when
//        Matching savedMatching = matchingService.create(siteUser.getEmail(), matchingDetailRequestDto);
//
//        //then
//        assertThat(savedMatching.getSiteUser().getId()).isEqualTo(siteUser.getId());
//        assertThat(savedMatching.getTitle()).isEqualTo(matchingDetailRequestDto.getTitle());
//        assertThat(savedMatching.getContent()).isEqualTo(matchingDetailRequestDto.getContent());
//        assertThat(savedMatching.getLocation()).isEqualTo(matchingDetailRequestDto.getLocation());
//        assertThat(savedMatching.getLat()).isEqualTo(matchingDetailRequestDto.getLat());
//        assertThat(savedMatching.getLon()).isEqualTo(matchingDetailRequestDto.getLon());
//        assertThat(savedMatching.getLocationImg()).isEqualTo(matchingDetailRequestDto.getLocationImg());
//        assertThat(savedMatching.getDate()).isEqualTo(matchingDetailRequestDto.getDate());
//        assertThat(savedMatching.getStartTime()).isEqualTo(matchingDetailRequestDto.getStartTime());
//        assertThat(savedMatching.getEndTime()).isEqualTo(matchingDetailRequestDto.getEndTime());
//        assertThat(savedMatching.getRecruitNum()).isEqualTo(matchingDetailRequestDto.getRecruitNum());
//        assertThat(savedMatching.getCost()).isEqualTo(matchingDetailRequestDto.getCost());
//        assertThat(savedMatching.getIsReserved()).isEqualTo(matchingDetailRequestDto.getIsReserved());
//        assertThat(savedMatching.getNtrp()).isEqualTo(matchingDetailRequestDto.getNtrp());
//        assertThat(savedMatching.getAge()).isEqualTo(matchingDetailRequestDto.getAgeGroup());
//        assertThat(savedMatching.getMatchingType()).isEqualTo(matchingDetailRequestDto.getMatchingType());
//    }
//
//    @Test
//    @DisplayName("수정한 값이 제대로 저장됨")
//    void update() {
//        // given
//        SiteUser siteUser = makeSiteUser();
//        Matching matching = makeMatching(siteUser);
//        MatchingDetailRequestDto matchingDetailRequestDto = makeMatchingDetailDto();
//        given(siteUserRepository.findById(anyLong()))
//                .willReturn(Optional.of(siteUser));
//        given(matchingRepository.findById(anyLong()))
//                .willReturn(Optional.of(matching));
//        given(matchingRepository.existsByIdAndSiteUser(anyLong(), any(SiteUser.class)))
//                .willReturn(true);
//        given(matchingRepository.save(any(Matching.class)))
//                .willReturn(Matching.fromDto(matchingDetailRequestDto, siteUser));
//
//        //when
//        Matching savedMatching = matchingService.update(siteUser.getEmail(), 1L, matchingDetailRequestDto);
//
//        //then
//        assertThat(savedMatching.getSiteUser().getId()).isEqualTo(siteUser.getId());
//        assertThat(savedMatching.getTitle()).isEqualTo(matchingDetailRequestDto.getTitle());
//        assertThat(savedMatching.getContent()).isEqualTo(matchingDetailRequestDto.getContent());
//        assertThat(savedMatching.getLocation()).isEqualTo(matchingDetailRequestDto.getLocation());
//        assertThat(savedMatching.getLat()).isEqualTo(matchingDetailRequestDto.getLat());
//        assertThat(savedMatching.getLon()).isEqualTo(matchingDetailRequestDto.getLon());
//        assertThat(savedMatching.getLocationImg()).isEqualTo(matchingDetailRequestDto.getLocationImg());
//        assertThat(savedMatching.getDate()).isEqualTo(matchingDetailRequestDto.getDate());
//        assertThat(savedMatching.getStartTime()).isEqualTo(matchingDetailRequestDto.getStartTime());
//        assertThat(savedMatching.getEndTime()).isEqualTo(matchingDetailRequestDto.getEndTime());
//        assertThat(savedMatching.getRecruitNum()).isEqualTo(matchingDetailRequestDto.getRecruitNum());
//        assertThat(savedMatching.getCost()).isEqualTo(matchingDetailRequestDto.getCost());
//        assertThat(savedMatching.getIsReserved()).isEqualTo(matchingDetailRequestDto.getIsReserved());
//        assertThat(savedMatching.getNtrp()).isEqualTo(matchingDetailRequestDto.getNtrp());
//        assertThat(savedMatching.getAge()).isEqualTo(matchingDetailRequestDto.getAgeGroup());
//        assertThat(savedMatching.getMatchingType()).isEqualTo(matchingDetailRequestDto.getMatchingType());
//    }

    // 이 이후는 jpa에서 기본으로 제공하는 method로만 이루어져서 테스트 안해도 될 듯
    @Test
    void delete() {
    }

    @Test
    void getList() {
    }

    @Test
    void getDetail() {
    }

    @Test
    void getApplyContentsByOrganizerSuccess() {
        // given
        given(siteUserRepository.findByEmail("example@example.com"))
                .willReturn(Optional.ofNullable(makeSiteUser()));
        given(findEntity.findMatching(1L)).willReturn(makeMatching(makeSiteUser()));
        given(applyRepository.countByMatching_IdAndApplyStatus(1L, ApplyStatus.PENDING))
                .willReturn(Optional.of(2));
        given(applyRepository.findAllByMatching_IdAndApplyStatus(1L, ApplyStatus.PENDING))
                .willReturn(Optional.of(getApplyMember()));
        given(applyRepository.findAllByMatching_IdAndApplyStatus(1L, ApplyStatus.ACCEPTED))
                .willReturn(Optional.of(getConfirmedMember()));

        // when
        var result = matchingService.getApplyContents("example@example.com", 1L);

        // then
        assertEquals(2, result.getApplyNum());
        assertEquals(2, result.getAppliedMembers().size());
    }

    @Test
    void getApplyContentsByUserSuccess() {
        // given
        given(siteUserRepository.findByEmail("example@example.com"))
                .willReturn(Optional.ofNullable(makeSiteUser2()));
        given(findEntity.findMatching(1L)).willReturn(makeMatching(makeSiteUser()));
        given(applyRepository.countByMatching_IdAndApplyStatus(1L, ApplyStatus.PENDING))
                .willReturn(Optional.of(2));
        given(applyRepository.findAllByMatching_IdAndApplyStatus(1L, ApplyStatus.PENDING))
                .willReturn(Optional.of(getApplyMember()));
        given(applyRepository.findAllByMatching_IdAndApplyStatus(1L, ApplyStatus.ACCEPTED))
                .willReturn(Optional.of(getConfirmedMember()));

        // when
        var result = matchingService.getApplyContents("example@example.com", 1L);

        // then
        assertEquals(0, result.getApplyNum());
        assertEquals(null, result.getAppliedMembers());
    }

    @Test
    void getApplyContentsFailedByEmailNotFound() {
        // given
        given(siteUserRepository.findByEmail("example@example.com"))
                .willReturn(Optional.ofNullable(null));

        // when
        RacketPuncherException exception = assertThrows(RacketPuncherException.class,
                () -> matchingService.getApplyContents("example@example.com", 1L));

        // then
        assertEquals(exception.getMessage(), "이메일을 찾을 수 없습니다.");
    }

    private SiteUser makeSiteUser() {
        return SiteUser.builder()
                .id(1L)
                .password("password123")
                .nickname("nickname")
                .email("example@example.com")
                .phoneNumber("010-1234-5678")
                .mannerScore(10)
                .gender(GenderType.MALE)
                .ntrp(Ntrp.DEVELOPMENT)
                .address("서울")
                .zipCode("12345")
                .ageGroup(AgeGroup.TWENTIES)
                .profileImg("http://example.com/img.jpg")
                .createDate(LocalDateTime.now())
                .isPhoneVerified(true)
                .hostedMatches(new ArrayList<>())
                .applies(new ArrayList<>())
                .notifications(new ArrayList<>())
                .build();
    }

    private SiteUser makeSiteUser2() {
        return SiteUser.builder()
                .id(2L)
                .password("password123")
                .nickname("nickname")
                .email("example@example.com")
                .phoneNumber("010-1234-5678")
                .mannerScore(10)
                .gender(GenderType.MALE)
                .ntrp(Ntrp.DEVELOPMENT)
                .address("서울")
                .zipCode("12345")
                .ageGroup(AgeGroup.TWENTIES)
                .profileImg("http://example.com/img.jpg")
                .createDate(LocalDateTime.now())
                .isPhoneVerified(true)
                .hostedMatches(new ArrayList<>())
                .applies(new ArrayList<>())
                .notifications(new ArrayList<>())
                .build();
    }

    private ApplyDto makeApplyDto(SiteUser siteUser, Matching matching) {
        return ApplyDto.builder()
                .matching(matching)
                .siteUser(siteUser)
                .build();
    }

    private MatchingDetailRequestDto makeMatchingDetailDto() {
        return MatchingDetailRequestDto.builder()
                .title("제목")
                .content("본문")
                .location("장소")
                .locationImg("구장 이미지 주소")
                .date("2023-11-11")
                .startTime("10:00")
                .endTime("12:00")
                .recruitDueDate("2023-11-10")
                .recruitDueTime("23")
                .recruitNum(5)
                .cost(5000)
                .isReserved(false)
                .ntrp(Ntrp.DEVELOPMENT)
                .ageGroup(AgeGroup.TWENTIES)
                .matchingType(MatchingType.SINGLE)
                .build();
    }

    private MatchingPreviewDto makeMatchingPreviewDto() {
        return MatchingPreviewDto.builder()
                .isReserved(true)
                .matchingType(MatchingType.SINGLE)
                .ntrp(Ntrp.ADVANCE)
                .title("제목")
                .matchingStartDateTime("시작 날짜")
                .build();
    }

    private Matching makeMatching(SiteUser siteUser) {
        return Matching.builder()
                .id(1L)
                .siteUser(siteUser)
                .createTime(LocalDateTime.now())
                .age(AgeGroup.FORTIES)
                .content("내용")
                .matchingType(MatchingType.SINGLE)
                .ntrp(Ntrp.PRO)
                .isReserved(true)
                .recruitDueDateTime(LocalDateTime.now().plusDays(3))
                .recruitNum(4)
                .confirmedNum(2)
                .cost(5000)
                .location("서울특별시 중구 을지로 66")
                .lat(37.56556383681641)
                .lon(126.98540998152264)
                .recruitStatus(RecruitStatus.OPEN)
                .title("같이 테니스 치실분 구해요")
                .build();
    }

    private List<Apply> getApplyMember() {
        List<Apply> applyMembers = new ArrayList<>();

        var applyMember1 = Apply.builder()
                .id(1L)
                .siteUser(SiteUser.builder()
                        .nickname("nickname1")
                        .id(2L)
                        .build())
                .build();

        var applyMember2 = Apply.builder()
                .id(2L)
                .siteUser(SiteUser.builder()
                        .nickname("nickname2")
                        .id(3L)
                        .build())
                .build();

        applyMembers.add(applyMember1);
        applyMembers.add(applyMember2);

        return applyMembers;
    }

    private List<Apply> getConfirmedMember() {
        List<Apply> confirmedMembers = new ArrayList<>();

        var applyMember1 = Apply.builder()
                .id(3L)
                .siteUser(SiteUser.builder()
                        .nickname("nickname3")
                        .id(1L)
                        .build())
                .build();

        var applyMember2 = Apply.builder()
                .id(4L)
                .siteUser(SiteUser.builder()
                        .nickname("nickname4")
                        .id(5L)
                        .build())
                .build();

        confirmedMembers.add(applyMember1);
        confirmedMembers.add(applyMember2);

        return confirmedMembers;
    }
}
