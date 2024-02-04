package com.example.demo.matching.service;

import com.example.demo.apply.dto.ApplyDto;
import com.example.demo.apply.repository.ApplyRepository;
import com.example.demo.common.FindEntity;
import com.example.demo.entity.Apply;
import com.example.demo.entity.Matching;
import com.example.demo.entity.SiteUser;
import com.example.demo.exception.RacketPuncherException;
import com.example.demo.matching.dto.*;
import com.example.demo.matching.filter.Region;
import com.example.demo.matching.repository.MatchingRepository;
import com.example.demo.notification.service.NotificationService;
import com.example.demo.openfeign.feignclient.LatAndLonApiFeignClient;
import com.example.demo.siteuser.repository.SiteUserRepository;
import com.example.demo.type.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

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

    @Mock
    private LatAndLonApiFeignClient latAndLonApiFeignClient;

    @InjectMocks
    private MatchingServiceImpl matchingService;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(matchingService, "apiKey", "kakaoClientId");
    }

    @Test
    void createSuccess() {
        //given
        SiteUser siteUser = getSiteUser();
        MatchingDetailRequestDto matchingDetailRequestDto = getMatchingDetailDto();
        ApplyDto applyDto = getApplyDto(siteUser, Matching.fromDto(matchingDetailRequestDto, siteUser));
        LatAndLonResponseDto latAndLonResponseDto = getLatAndLonResponseDto();

        given(siteUserRepository.findByEmail(siteUser.getEmail()))
                .willReturn(Optional.of(siteUser));
        given(latAndLonApiFeignClient.getLatAndLon(matchingDetailRequestDto.getLocation(), "KakaoAK kakaoClientId"))
                .willReturn(latAndLonResponseDto);
        given(matchingRepository.save(any(Matching.class)))
                .willReturn(Matching.fromDto(matchingDetailRequestDto, siteUser));
        given(applyRepository.save(any(Apply.class)))
                .willReturn(Apply.fromDto(applyDto));

        //when
        Matching savedMatching = matchingService.create(siteUser.getEmail(), matchingDetailRequestDto);

        //then
        assertThat(savedMatching.getSiteUser().getId()).isEqualTo(siteUser.getId());
        assertThat(savedMatching.getTitle()).isEqualTo(matchingDetailRequestDto.getTitle());
        assertThat(savedMatching.getLocation()).isEqualTo(matchingDetailRequestDto.getLocation());
    }

    @Test
    void createFailByWrongAddress() {
        //given
        SiteUser siteUser = getSiteUser();
        MatchingDetailRequestDto matchingDetailRequestDto = getMatchingDetailDto();
        LatAndLonResponseDto latAndLonResponseDto = getLatAndLonResponseDto();
        latAndLonResponseDto.setDocuments(new ArrayList<>());

        given(siteUserRepository.findByEmail(siteUser.getEmail()))
                .willReturn(Optional.of(siteUser));
        given(latAndLonApiFeignClient.getLatAndLon(matchingDetailRequestDto.getLocation(), "KakaoAK kakaoClientId"))
                .willReturn(latAndLonResponseDto);

        //when
        RacketPuncherException exception = assertThrows(RacketPuncherException.class,
                () -> matchingService.create(siteUser.getEmail(), matchingDetailRequestDto));

        //then
        assertThat(exception.getMessage()).isEqualTo("위경도를 찾을 수 없는 주소입니다.");
    }

    @Test
    void update() {
        // given
        SiteUser siteUser = getSiteUser();
        Matching matching = getMatchingEntity(siteUser);
        MatchingDetailRequestDto matchingDetailRequestDto = getMatchingDetailDto();

        given(siteUserRepository.findByEmail(siteUser.getEmail()))
                .willReturn(Optional.of(siteUser));
        given(findEntity.findMatching(matching.getId()))
                .willReturn(matching);
        given(applyRepository.findAllByMatching_IdAndApplyStatus(matching.getId(), ApplyStatus.ACCEPTED))
                .willReturn(getApplyMember());
        given(applyRepository.findAllByMatching_Id(matching.getId()))
                .willReturn(new ArrayList<>());
        given(matchingRepository.existsByIdAndSiteUser(anyLong(), any(SiteUser.class)))
                .willReturn(true);

        //when
        Matching savedMatching = matchingService.update(siteUser.getEmail(), 1L, matchingDetailRequestDto);

        //then
        assertThat(savedMatching.getSiteUser().getId()).isEqualTo(siteUser.getId());
        assertThat(savedMatching.getTitle()).isEqualTo(matchingDetailRequestDto.getTitle());
        assertThat(savedMatching.getContent()).isEqualTo(matchingDetailRequestDto.getContent());
        assertThat(savedMatching.getLocation()).isEqualTo(matchingDetailRequestDto.getLocation());
    }

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
                .willReturn(Optional.ofNullable(getSiteUser()));
        given(findEntity.findMatching(1L)).willReturn(getMatchingEntity(getSiteUser()));
        given(applyRepository.countByMatching_IdAndApplyStatus(1L, ApplyStatus.PENDING))
                .willReturn(Optional.of(2));
        given(applyRepository.findAllByMatching_IdAndApplyStatus(1L, ApplyStatus.PENDING))
                .willReturn(getApplyMember());
        given(applyRepository.findAllByMatching_IdAndApplyStatus(1L, ApplyStatus.ACCEPTED))
                .willReturn(getConfirmedMember());

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
                .willReturn(Optional.ofNullable(getSiteUser2()));
        given(findEntity.findMatching(1L)).willReturn(getMatchingEntity(getSiteUser()));
        given(applyRepository.countByMatching_IdAndApplyStatus(1L, ApplyStatus.PENDING))
                .willReturn(Optional.of(2));
        given(applyRepository.findAllByMatching_IdAndApplyStatus(1L, ApplyStatus.PENDING))
                .willReturn(getApplyMember());
        given(applyRepository.findAllByMatching_IdAndApplyStatus(1L, ApplyStatus.ACCEPTED))
                .willReturn(getConfirmedMember());

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

    @Test
    void getMatchingWithinDistance() {
        // given
        LocationDto locationDto = getLocationDto();
        Pageable pageable = PageRequest.of(0, 5);
        Matching matching = getMatchingEntity(getSiteUser());

        given(matchingRepository.findAllWithinBoundary(eq(locationDto), any(LocationDto.class), any(LocationDto.class), eq(pageable)))
                .willReturn(new PageImpl<>(List.of(matching), pageable, 1));

        // when
        var result = matchingService.getMatchingWithinDistance(locationDto, 3.0, pageable);

        // then
        assertEquals(matching.getId(), result.get().findFirst().orElseThrow().getId());
    }

    @Test
    void getMatchingByEmptyFilter() {
        // given
        FilterDto filterDto = getEmptyFilterDto();
        Pageable pageable = PageRequest.of(0, 5);
        Matching matching = getMatchingEntity(getSiteUser());

        given(matchingRepository.findByRecruitStatusAndRecruitDueDateTimeAfter(eq(RecruitStatus.OPEN), any(LocalDateTime.class), eq(pageable)))
                .willReturn(new PageImpl<>(List.of(matching), pageable, 1));

        // when
        var result = matchingService.getMatchingByFilter(filterDto, pageable);

        // then
        assertEquals(matching.getId(), result.get().findFirst().orElseThrow().getId());
    }

    @Test
    void getMatchingByFilter() {
        // given
        FilterDto filterDto = getFilterDto();
        Pageable pageable = PageRequest.of(0, 5);
        Matching matching = getMatchingEntity(getSiteUser());

        given(matchingRepository.findAllByFilter(filterDto, pageable))
                .willReturn(new PageImpl<>(List.of(matching), pageable, 1));

        // when
        var result = matchingService.getMatchingByFilter(filterDto, pageable);

        // then
        assertEquals(matching.getId(), result.get().findFirst().orElseThrow().getId());
    }

    private SiteUser getSiteUser() {
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
                .hostedMatches(new ArrayList<>())
                .applies(new ArrayList<>())
                .notifications(new ArrayList<>())
                .build();
    }

    private SiteUser getSiteUser2() {
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
                .hostedMatches(new ArrayList<>())
                .applies(new ArrayList<>())
                .notifications(new ArrayList<>())
                .build();
    }

    private ApplyDto getApplyDto(SiteUser siteUser, Matching matching) {
        return ApplyDto.builder()
                .matching(matching)
                .siteUser(siteUser)
                .build();
    }

    private MatchingDetailRequestDto getMatchingDetailDto() {
        return MatchingDetailRequestDto.builder()
                .title("제목")
                .content("본문")
                .location("주소")
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

    private MatchingPreviewDto getMatchingPreviewDto() {
        return MatchingPreviewDto.builder()
                .isReserved(true)
                .matchingType(MatchingType.SINGLE)
                .ntrp(Ntrp.ADVANCE)
                .title("제목")
                .matchingStartDateTime("시작 날짜")
                .build();
    }

    private Matching getMatchingEntity(SiteUser siteUser) {
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
                .acceptedNum(2)
                .cost(5000)
                .location("서울시 강남구")
                .lat(37.56556383681641)
                .lon(126.98540998152264)
                .recruitStatus(RecruitStatus.OPEN)
                .title("같이 테니스 치실분 구해요")
                .date(LocalDate.now())
                .startTime(LocalTime.now())
                .build();
    }

    private List<Apply> getApplyMember() {
        List<Apply> applyMembers = new ArrayList<>();

        var applyMember1 = Apply.builder()
                .id(1L)
                .siteUser(SiteUser.builder()
                        .nickname("nickname1")
                        .id(2L)
                        .siteUserName("siteUsername1")
                        .email("email1")
                        .mannerScore(1)
                        .gender(GenderType.FEMALE)
                        .ntrp(Ntrp.BEGINNER)
                        .ageGroup(AgeGroup.TWENTIES)
                        .address("address1")
                        .zipCode("zipCode1")
                        .profileImg("profile1")
                        .build())
                .build();

        var applyMember2 = Apply.builder()
                .id(2L)
                .siteUser(SiteUser.builder()
                        .nickname("nickname2")
                        .id(3L)
                        .siteUserName("siteUsername2")
                        .email("email2")
                        .mannerScore(1)
                        .gender(GenderType.FEMALE)
                        .ntrp(Ntrp.BEGINNER)
                        .ageGroup(AgeGroup.TWENTIES)
                        .address("address2")
                        .zipCode("zipCode2")
                        .profileImg("profile2")
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
                        .siteUserName("siteUsername3")
                        .email("email3")
                        .mannerScore(1)
                        .gender(GenderType.FEMALE)
                        .ntrp(Ntrp.BEGINNER)
                        .ageGroup(AgeGroup.TWENTIES)
                        .address("address3")
                        .zipCode("zipCode3")
                        .profileImg("profile3")
                        .build())
                .build();

        var applyMember2 = Apply.builder()
                .id(4L)
                .siteUser(SiteUser.builder()
                        .nickname("nickname4")
                        .id(5L)
                        .siteUserName("siteUsername4")
                        .email("email4")
                        .mannerScore(1)
                        .gender(GenderType.FEMALE)
                        .ntrp(Ntrp.BEGINNER)
                        .ageGroup(AgeGroup.TWENTIES)
                        .address("address4")
                        .zipCode("zipCode4")
                        .profileImg("profile4")
                        .build())
                .build();

        confirmedMembers.add(applyMember1);
        confirmedMembers.add(applyMember2);

        return confirmedMembers;
    }

    private LatAndLonResponseDto getLatAndLonResponseDto(){
        List<DocumentForAddressDto> documentList = Arrays.asList(
                DocumentForAddressDto.builder()
                        .x("37.8")
                        .y("127.5")
                        .build());

        return LatAndLonResponseDto.builder()
                .documents(documentList)
                .build();
    }

    private LocationDto getLocationDto(){
        return LocationDto.builder()
                .lat(38.0)
                .lon(127.0)
                .build();
    }

    public FilterDto getEmptyFilterDto() {
        return FilterDto.builder()
                .date("")
                .ntrps(List.of())
                .matchingTypes(List.of())
                .ageGroups(List.of())
                .regions(List.of())
                .build();
    }

    public FilterDto getFilterDto() {
        return FilterDto.builder()
                .date("")
                .ntrps(List.of(Ntrp.PRO))
                .matchingTypes(List.of(MatchingType.SINGLE))
                .ageGroups(List.of(AgeGroup.FORTIES))
                .regions(List.of(Region.GANGNAM))
                .build();
    }
}