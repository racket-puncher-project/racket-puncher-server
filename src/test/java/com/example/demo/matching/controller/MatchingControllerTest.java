package com.example.demo.matching.controller;

import com.example.demo.entity.Matching;
import com.example.demo.entity.SiteUser;
import com.example.demo.matching.dto.*;
import com.example.demo.openfeign.dto.address.AddressResponseDto;
import com.example.demo.openfeign.service.address.AddressService;
import com.example.demo.matching.service.MatchingService;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.aws.S3Uploader;
import com.example.demo.siteuser.security.CustomAuthFailureHandler;
import com.example.demo.siteuser.security.JwtAuthenticationFilter;
import com.example.demo.siteuser.security.SecurityConfiguration;
import com.example.demo.siteuser.security.TokenProvider;
import com.example.demo.type.AgeGroup;
import com.example.demo.type.MatchingType;
import com.example.demo.type.Ntrp;
import com.example.demo.type.RecruitStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;

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
    private CustomAuthFailureHandler customAuthFailureHandler;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private MockMvc mockMvc;
  
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void successConfirmMatching() throws Exception {
        // given
        given(matchingService.getApplyContents(anyString(), anyLong()))
                .willReturn(ApplyContents.builder().build());

        // when
        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/matches/1/apply"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("매칭글 등록 - 구장 이미지 없는 경우")
    public void createMatchingTest() throws Exception {
        //given
        Matching matching = makeMatching();
        MatchingDetailRequestDto matchingDetailRequestDto = makeMatchingDetailDto();
        given(matchingService.create(anyString(), matchingDetailRequestDto))
                .willReturn(matching);
        String content = objectMapper.writeValueAsString(matchingDetailRequestDto);

        //when
        //then
        mockMvc.perform(post("/api/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("매칭글 조회")
    public void getDetailedMatchingTest() throws Exception {
        //given
        MatchingDetailResponseDto matchingDetailResponseDto = makeMatchingDetailDto();
        given(matchingService.getDetail(1L))
                .willReturn(matchingDetailRequestDto);
        //when
        //then
        MvcResult mvcResult = mockMvc.perform(get("/api/matches/1"))
                .andExpect(status().isOk())
                .andReturn();
        //TODO:
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("매칭글 수정 - 구장 이미지 바꾸지 않은 경우")
    public void editMatchingTest() throws Exception {
        //given
        Matching matching = makeMatching();
        MatchingDetailRequestDto matchingDetailRequestDto = makeMatchingDetailDto();
        given(matchingService.update(anyString(), 1L, matchingDetailRequestDto))
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
    @WithMockUser(roles = "USER")
    @DisplayName("매칭글 삭제")
    public void deleteMatchingTest() throws Exception {
        //given
        doNothing().when(matchingService).delete(anyString(), 1L);

        mockMvc.perform(delete("/api/matches/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("매칭글 목록 조회")
    public void getMatchingList() throws Exception {
        //given
        int page = 0;
        int size = 5;
        Pageable pageable = PageRequest.of(page, size);
        ArrayList<MatchingPreviewDto> arrayList = new ArrayList<>();
        MatchingPreviewDto matchingPreviewDto = makeMatchingPreviewDto();
        arrayList.add(matchingPreviewDto);
        Page<MatchingPreviewDto> pages = new PageImpl<>(arrayList, pageable, 1);

        given(matchingService.findFilteredMatching(new FilterRequestDto(), pageable))
                .willReturn(pages);

        mockMvc.perform(get("/api/matches/list")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.ntrp").value(matchingPreviewDto.getNtrp().name()))
                .andExpect(jsonPath("$.content[0].title").value(matchingPreviewDto.getTitle()))
                .andExpect(jsonPath("$.content[0].matchingStartDateTime").value(matchingPreviewDto.getMatchingStartDateTime()))
                .andExpect(jsonPath("$.content[0].reserved").value(matchingPreviewDto.isReserved()))
                .andExpect(jsonPath("$.content[0].matchingType").value(matchingPreviewDto.getMatchingType().name()));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getApplyContents() throws Exception {
        given(matchingService.getApplyContents(anyString(), anyLong()))
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

    private Matching makeMatching(){
        return Matching.builder()
                .createTime(LocalDateTime.now())
                .age(AgeGroup.FORTIES)
                .content("내용")
                .confirmedNum(1)
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

    private MatchingDetailRequestDto makeMatchingDetailDto(){
        return MatchingDetailRequestDto.builder()
                .id(1L)
                .creatorUserId(1L)
                .title("제목")
                .content("본문")
                .location("서울특별시 중구 을지로 66")
                .lat(37.56556383681641)
                .lon(126.98540998152264)
                .locationImg("구장 이미지 주소")
                .date("2023-11-07")
                .startTime("18:00")
                .endTime("20:00")
                .recruitNum(4)
                .cost(5000)
                .isReserved(false)
                .ntrp(Ntrp.DEVELOPMENT)
                .ageGroup(AgeGroup.SENIOR)
                .recruitStatus(RecruitStatus.OPEN)
                .matchingType(MatchingType.MIXED_DOUBLE)
                .confirmedNum(2)
                .build();
    }

    private MatchingPreviewDto makeMatchingPreviewDto(){
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
                .applyNum(1)
                .recruitNum(4)
                .confirmedNum(1)
                .appliedMembers(List.of(ApplyMember
                        .builder()
                                .applyId(2L)
                                .siteUserId(2L)
                                .nickname("닉네임2")
                        .build()))
                .confirmedMembers(List.of(ApplyMember
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
}
