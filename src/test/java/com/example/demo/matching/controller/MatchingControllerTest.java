package com.example.demo.matching.controller;

import com.example.demo.auth.security.JwtAuthenticationFilter;
import com.example.demo.auth.security.SecurityConfiguration;
import com.example.demo.auth.security.TokenProvider;
import com.example.demo.aws.S3Uploader;
import com.example.demo.entity.Matching;
import com.example.demo.entity.SiteUser;
import com.example.demo.matching.dto.*;
import com.example.demo.matching.service.MatchingService;
import com.example.demo.openfeign.dto.address.AddressResponseDto;
import com.example.demo.openfeign.service.address.AddressService;
import com.example.demo.type.AgeGroup;
import com.example.demo.type.MatchingType;
import com.example.demo.type.Ntrp;
import com.example.demo.type.RecruitStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MatchingController.class)
@Import(SecurityConfiguration.class)
class MatchingControllerTest {

    @MockBean
    private MatchingService matchingService;

    @MockBean
    private AddressService addressService;

    @MockBean
    private S3Uploader s3Uploader;

    @MockBean
    private TokenProvider tokenProvider;


    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private MockMvc mockMvc;
  
    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void createMatchingTest() throws Exception {
        //given
        Matching matching = getMatchingEntity();
        MatchingDetailRequestDto matchingDetailRequestDto = getMatchingDetailRequestDto();
        String content = objectMapper.writeValueAsString(matchingDetailRequestDto);

        given(matchingService.create(anyString(), eq(matchingDetailRequestDto)))
                .willReturn(matching);

        //when
        //then
        mockMvc.perform(post("/api/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk());
    }

    @Test
    public void getDetailedMatchingTest() throws Exception {
        //given
        given(matchingService.getDetail(1L))
                .willReturn(getMatchingDetailResponseDto());
        //when
        //then
        mockMvc.perform(get("/api/matches/1"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void editMatchingTest() throws Exception {
        //given
        Matching matching = getMatchingEntity();
        MatchingDetailRequestDto matchingDetailRequestDto = getMatchingDetailRequestDto();
        given(matchingService.update(anyString(), eq(1L), eq(matchingDetailRequestDto)))
                .willReturn(matching);
        String content = objectMapper.writeValueAsString(matchingDetailRequestDto);

        //when
        //then
        mockMvc.perform(patch("/api/matches/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteMatchingTest() throws Exception {
        //given
        doNothing().when(matchingService).delete(anyString(), eq(1L));

        mockMvc.perform(delete("/api/matches/1"))
                .andExpect(status().isOk());
    }

//    @Test
//    @DisplayName("매칭글 목록 조회")
//    public void getMatchingList() throws Exception {
//        //given
//        int page = 0;
//        int size = 5;
//        Pageable pageable = PageRequest.of(page, size);
//        ArrayList<MatchingPreviewDto> arrayList = new ArrayList<>();
//        MatchingPreviewDto matchingPreviewDto = makeMatchingPreviewDto();
//        arrayList.add(matchingPreviewDto);
//        Page<MatchingPreviewDto> pages = new PageImpl<>(arrayList, pageable, 1);
//
//        given(matchingService.findFilteredMatching(new FilterRequestDto(), pageable))
//                .willReturn(pages);
//
//        mockMvc.perform(get("/api/matches/list")
//                        .param("page", String.valueOf(page))
//                        .param("size", String.valueOf(size)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.response.ntrp").value(matchingPreviewDto.getNtrp().name()))
//                .andExpect(jsonPath("$.content[0].title").value(matchingPreviewDto.getTitle()))
//                .andExpect(jsonPath("$.content[0].matchingStartDateTime").value(matchingPreviewDto.getMatchingStartDateTime()))
//                .andExpect(jsonPath("$.content[0].reserved").value(matchingPreviewDto.isReserved()))
//                .andExpect(jsonPath("$.content[0].matchingType").value(matchingPreviewDto.getMatchingType().name()));
//    }

    @Test
    void getApplyContents() throws Exception {
        given(matchingService.getApplyContents(anyString(), eq(1L)))
                .willReturn(makeApplyContents());

        mockMvc.perform(get("/api/matches/1/apply"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getAddress() throws Exception {
        String keyword = "삼성동";
        given(addressService.getAddressService(keyword))
                .willReturn(List.of(getAddressResponseDto()));

        mockMvc.perform(get("/api/matches/address")
                .param("keyword", keyword))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getMatchingWithinDistance() throws Exception {
        //given
        LocationDto locationDto = getLocationDto();
        Pageable pageable = PageRequest.of(0, 5);
        String content = objectMapper.writeValueAsString(locationDto);

        given(matchingService.getMatchingWithinDistance(locationDto,3.0, pageable))
                .willReturn(new PageImpl<>(List.of(getMatchingPreviewDto()), pageable, 1));

        //when
        //then
        mockMvc.perform(post("/api/matches/list/map")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getMatchingList() throws Exception {
        //given
        FilterRequestDto filterRequestDto = getFilterRequestDto();
        Pageable pageable = PageRequest.of(0, 5);
        String content = objectMapper.writeValueAsString(filterRequestDto);

        given(matchingService.getMatchingByFilter(filterRequestDto.getFilter(), pageable))
                .willReturn(new PageImpl<>(List.of(getMatchingPreviewDto()), pageable, 1));

        //when
        //then
        mockMvc.perform(post("/api/matches/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andDo(print());
    }

    private Matching getMatchingEntity(){
        return Matching.builder()
                .createTime(LocalDateTime.now())
                .age(AgeGroup.FORTIES)
                .content("내용")
                .acceptedNum(1)
                .matchingType(MatchingType.SINGLE)
                .ntrp(Ntrp.PRO)
                .isReserved(true)
                .recruitDueDateTime(LocalDateTime.now().plusDays(3))
                .cost(5000)
                .location("서울특별시 중구 을지로 66")
                .lat(37.56556383681641)
                .lon(126.98540998152264)
                .recruitStatus(RecruitStatus.OPEN)
                .title("같이 테니스 치실분 구해요")
                .siteUser(new SiteUser())
                .build();
    }

    private MatchingDetailRequestDto getMatchingDetailRequestDto(){
        return MatchingDetailRequestDto.builder()
                .title("제목")
                .content("본문")
                .location("서울특별시 중구 을지로 66")
                .locationImg("구장 이미지 주소")
                .date("2023-11-07")
                .startTime("18:00")
                .endTime("20:00")
                .recruitNum(4)
                .cost(5000)
                .isReserved(false)
                .ntrp(Ntrp.DEVELOPMENT)
                .ageGroup(AgeGroup.SENIOR)
                .matchingType(MatchingType.MIXED_DOUBLE)
                .build();
    }

    private MatchingDetailResponseDto getMatchingDetailResponseDto(){
        return MatchingDetailResponseDto.builder()
                .title("제목")
                .content("본문")
                .location("서울특별시 중구 을지로 66")
                .lat(37.2636)
                .lon(127.0286)
                .locationImg("구장 이미지 주소")
                .date("2023-11-07")
                .startTime("18:00")
                .endTime("20:00")
                .recruitNum(4)
                .cost(5000)
                .isReserved(false)
                .ntrp(Ntrp.DEVELOPMENT)
                .ageGroup(AgeGroup.SENIOR)
                .matchingType(MatchingType.MIXED_DOUBLE)
                .build();
    }

    private MatchingPreviewDto getMatchingPreviewDto(){
        return MatchingPreviewDto.builder()
                .isReserved(true)
                .matchingType(MatchingType.SINGLE)
                .ntrp(Ntrp.ADVANCE)
                .title("제목")
                .matchingStartDateTime("2023-11-11")
                .build();
    }

    private ApplyContents makeApplyContents() {
        return ApplyContents.builder()
                .isApplied(true)
                .applyNum(1)
                .recruitNum(4)
                .acceptedNum(1)
                .appliedMembers(List.of(ApplyMember
                        .builder()
                                .applyId(2L)
                                .siteUserId(2L)
                                .nickname("닉네임2")
                        .build()))
                .acceptedMembers(List.of(ApplyMember
                        .builder()
                        .applyId(1L)
                        .siteUserId(1L)
                        .nickname("닉네임1")
                        .build()))
                .build();
    }

    private AddressResponseDto getAddressResponseDto() {
        return AddressResponseDto.builder()
                .roadAddr("서울특별시 강남구 삼성로 629 (삼성동, 삼성동센트럴아이파크)")
                .jibunAddr("서울특별시 강남구 삼성동 188 삼성동센트럴아이파크")
                .zipNo("06094")
                .build();
    }

    private LocationDto getLocationDto(){
        return LocationDto.builder()
                .lat(38.0)
                .lon(127.0)
                .build();
    }

    public FilterRequestDto getFilterRequestDto() {
        return FilterRequestDto.builder()
                .filter(FilterDto.builder()
                        .date("")
                        .ntrps(List.of())
                        .matchingTypes(List.of())
                        .ageGroups(List.of())
                        .regions(List.of())
                        .build())
                .build();
    }
}