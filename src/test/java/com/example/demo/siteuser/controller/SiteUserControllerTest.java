package com.example.demo.siteuser.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.example.demo.auth.security.JwtAuthenticationFilter;
import com.example.demo.auth.security.SecurityConfiguration;
import com.example.demo.auth.security.TokenProvider;
import com.example.demo.aws.S3Uploader;
import com.example.demo.entity.SiteUser;
import com.example.demo.siteuser.dto.InputReviewDto;
import com.example.demo.siteuser.dto.MyInfoDto;
import com.example.demo.siteuser.dto.SiteUserInfoDto;
import com.example.demo.siteuser.dto.NotificationDto;
import com.example.demo.siteuser.dto.ReviewPageInfoDto;
import com.example.demo.siteuser.dto.UpdateSiteUserInfoDto;
import com.example.demo.siteuser.service.SiteUserService;
import com.example.demo.type.AgeGroup;
import com.example.demo.type.GenderType;
import com.example.demo.type.NegativeReviewType;
import com.example.demo.type.Ntrp;
import com.example.demo.type.PositiveReviewType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(SiteUserController.class)
@Import(SecurityConfiguration.class)
public class SiteUserControllerTest {
    @MockBean
    private SiteUserService siteUserService;

    @MockBean
    private S3Uploader s3Uploader;

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getSiteUserInfo() throws Exception {
        // given
        given(siteUserService.getSiteUserInfo(1L))
                .willReturn(getSiteUserInfoDto());

        // when
        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/profile/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    void getMyInfo() throws Exception {
        // given
        given(siteUserService.getMyInfo("email@naver.com"))
                .willReturn(getMyInfoDto());

        // when
        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/my-page"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    void updateSiteUserInfo() throws Exception {
        // given
        given(siteUserService.updateSiteUserInfo("email@naver.com", getUpdateSiteUserInfoDto()))
                .willReturn(getUpdateSiteUser());

        // when
        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/my-page/modify"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    void getNotifications() throws Exception {
        // given
        given(siteUserService.getNotifications("email@naver.com"))
                .willReturn(getNotificationDtoList());

        // when
        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/notifications"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    void getReviewPageInfo() throws Exception {
        // given
        given(siteUserService.getReviewPageInfo("email@naver.com", 1L))
                .willReturn(getReviewPageInfoDtoList());

        // when
        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/review/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    void review() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/review/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
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

    private MyInfoDto getMyInfoDto() {
        return MyInfoDto.builder()
                .id(1L)
                .profileImg("img.png")
                .siteUserName("userName")
                .nickname("nickName")
                .email("email@naver.com")
                .phoneNumber("010-1234-5678")
                .address("address")
                .zipCode("zipCode")
                .ntrp(Ntrp.BEGINNER)
                .gender(GenderType.FEMALE)
                .mannerScore(3)
                .ageGroup(AgeGroup.TWENTIES)
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

    private SiteUser getUpdateSiteUser() {
        return SiteUser.builder()
                .id(1L)
                .email("email@naver.com")
                .password("2222")
                .nickname("nickName")
                .siteUserName("userName")
                .phoneNumber("010-1234-5678")
                .mannerScore(3)
                .gender(GenderType.FEMALE)
                .ntrp(Ntrp.BEGINNER)
                .address("address")
                .zipCode("zipCode")
                .ageGroup(AgeGroup.TWENTIES)
                .profileImg("update.png")
                .createDate(LocalDateTime.now())
                .build();
    }

    private List<NotificationDto> getNotificationDtoList() {
        List<NotificationDto> notificationList = new ArrayList<>();

        NotificationDto notification1 = NotificationDto
                .builder()
                .matchingId(1L)
                .title("title1")
                .content("content1")
                .createTime("2024-01-04 10:00:00")
                .build();

        NotificationDto notification2 = NotificationDto
                .builder()
                .matchingId(2L)
                .title("title2")
                .content("content2")
                .createTime("2024-01-04 10:00:00")
                .build();

        notificationList.add(notification1);
        notificationList.add(notification2);

        return notificationList;
    }

    private List<ReviewPageInfoDto> getReviewPageInfoDtoList() {
        List<ReviewPageInfoDto> reviewPageInfoDtoList = new ArrayList<>();

        ReviewPageInfoDto reviewPageInfoDto1 = ReviewPageInfoDto
                .builder()
                .siteUserId(1L)
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

        ReviewPageInfoDto reviewPageInfoDto2 = ReviewPageInfoDto
                .builder()
                .siteUserId(2L)
                .profileImg("img2.png")
                .siteUserName("userName2")
                .nickname("nickName2")
                .address("address2")
                .zipCode("zipCode2")
                .ntrp(Ntrp.BEGINNER)
                .gender(GenderType.FEMALE)
                .mannerScore(3)
                .ageGroup(AgeGroup.TWENTIES)
                .build();

        reviewPageInfoDtoList.add(reviewPageInfoDto1);
        reviewPageInfoDtoList.add(reviewPageInfoDto2);

        return reviewPageInfoDtoList;
    }
}
