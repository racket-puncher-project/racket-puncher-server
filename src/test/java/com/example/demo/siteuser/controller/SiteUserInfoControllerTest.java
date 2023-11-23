package com.example.demo.siteuser.controller;

import com.example.demo.aws.S3Uploader;
import com.example.demo.entity.SiteUser;
import com.example.demo.siteuser.dto.*;
import com.example.demo.siteuser.service.SiteUserInfoService;
import com.example.demo.type.MatchingType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasValue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class SiteUserInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private SiteUserInfoService siteUserInfoService;

    @Mock
    private S3Uploader s3Uploader;

    @InjectMocks
    private SiteUserInfoController siteUserInfoController;

    @BeforeEach
    public void setup() {
        siteUserInfoService = Mockito.mock(SiteUserInfoService.class);
        s3Uploader = Mockito.mock(S3Uploader.class);
        siteUserInfoController = new SiteUserInfoController(siteUserInfoService, s3Uploader);
        mockMvc = MockMvcBuilders.standaloneSetup(siteUserInfoController).build();
    }

    @Test
    public void testGetSiteUserInfoById() throws Exception {
        SiteUserInfoDto mockDto = SiteUserInfoDto.builder()
                        .nickname("test")
                        .build();
        when(siteUserInfoService.getSiteUserInfoById(any(Long.class))).thenReturn(mockDto);

        mockMvc.perform(get("/api/users/profile/{siteUser}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nickname").value("test")); // replace field and expected value accordingly
    }

    @Test
    public void testGetSiteUserMyInfoById() throws Exception {
        SiteUserMyInfoDto mockDto = SiteUserMyInfoDto.builder()
                .nickname("test")
                .build();
        when(siteUserInfoService.getSiteUserMyInfoById(any(Long.class))).thenReturn(mockDto);

        mockMvc.perform(get("/api/users/my-page/{siteUser}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nickname").value("test")); // replace field and expected value accordingly
    }

    @Test
    public void testGetMatchingBySiteUser() throws Exception {
        MatchingMyMatchingDto dto = MatchingMyMatchingDto.builder()
                .title("testTitle1")
                .location("testLocation1")
                .date("2022-10-01")
                .matchingType(MatchingType.SINGLE)
                .build();
        List<MatchingMyMatchingDto> mockList = Arrays.asList(dto);

        // Mocking service method
        when(siteUserInfoService.getMatchingBySiteUser(anyLong())).thenReturn(mockList);

        // Act: Perform the actual request
        mockMvc.perform(get("/api/users/my-page/hosted/1")
                        .contentType(MediaType.APPLICATION_JSON))
                // Assert: Verify the outcomes
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("testTitle1"));
    }

    @Test
    public void testGetApplyBySiteUser() throws Exception {
        MatchingMyMatchingDto dto = MatchingMyMatchingDto.builder()
                .title("testTitle1")
                .location("testLocation1")
                .date("2022-10-01")
                .matchingType(MatchingType.SINGLE)
                .build();
        List<MatchingMyMatchingDto> mockList = Arrays.asList(dto);
        when(siteUserInfoService.getApplyBySiteUser(anyLong())).thenReturn(mockList);

        mockMvc.perform(get("/api/users/my-page/apply/{siteUser}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("testTitle1"));
    }

    @Test
    public void testUploadOrUpdateProfileImage() throws Exception {
        Long userId = 1L;
        String oldImageUrl = "http://example.com/old-image.jpg";
        String newImageUrl = "http://example.com/new-image.jpg";

        MockMultipartFile mockFile = new MockMultipartFile("imageFile", "new-image.jpg", "image/jpeg", "new image content".getBytes());

        // 기존 이미지가 있는 경우
        when(siteUserInfoService.getProfileUrl(userId)).thenReturn(oldImageUrl);
        // 기존 이미지가 없는 경우
        when(siteUserInfoService.getProfileUrl(userId)).thenReturn(null);

        when(s3Uploader.uploadFile(any(MultipartFile.class))).thenReturn(newImageUrl);
        doNothing().when(s3Uploader).deleteFile(anyString());
        doNothing().when(siteUserInfoService).updateProfileImage(anyLong(), anyString());

        mockMvc.perform(multipart("/api/users/{userId}/upload-profile-image", userId)
                        .file(mockFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().string(newImageUrl));
    }
    @Test
    public void testUpdateSiteUser() throws Exception {
        SiteUserModifyDto modifyDto = SiteUserModifyDto.builder()
                .nickname("test")
                .build();

        String jsonModifyDto = new ObjectMapper().writeValueAsString(modifyDto);

        mockMvc.perform(patch("/api/users/my-page/modify/{userId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonModifyDto))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetNotificationBySiteUser() throws Exception {
        Long userId = 1L;
        List<SiteUserNotificationDto> mockNotifications = Arrays.asList(
                new SiteUserNotificationDto(1L, "Notification Title 1", "Notification Content 1", LocalDateTime.now().toString()),
                new SiteUserNotificationDto(2L, "Notification Title 2", "Notification Content 2", LocalDateTime.now().toString())
        );

        when(siteUserInfoService.getNotificationBySiteUser(userId)).thenReturn(mockNotifications);

        mockMvc.perform(get("/api/users/my-page/notification/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("Notification Title 1"))
                .andExpect(jsonPath("$[1].title").value("Notification Title 2"));
    }

    @Test
    public void testDeleteNotification() throws Exception {
        Long userId = 1L;
        Long notificationId = 100L;

        doNothing().when(siteUserInfoService).deleteNotification(userId, notificationId);

        mockMvc.perform(delete("/api/users/my-page/notification/{userId}/{notificationId}", userId, notificationId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(siteUserInfoService).deleteNotification(userId, notificationId);
    }

    @Test
    public void updatePenaltyScoreTest() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(siteUserInfoController).build();

        String jsonContent = "{\"penaltyCode\":\"OFFENSE_CHAT\"}";

        mockMvc.perform(post("/api/users/penalty/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateReportUser() throws Exception {
        ReportUserDto mockReportUserDto = new ReportUserDto(1L, 2L, "Test Title 1", "Test Content 1");
        doNothing().when(siteUserInfoService).createReportUser(any(ReportUserDto.class));

        mockMvc.perform(post("/api/users/report")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockReportUserDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Report created successfully"));

        verify(siteUserInfoService).createReportUser(any(ReportUserDto.class));
    }

    @Test
    public void testGetAllReports() throws Exception {
        List<ViewReportsDto> mockReports = Arrays.asList(new ViewReportsDto(/* 필요한 파라미터 초기화 */));
        when(siteUserInfoService.getAllReports()).thenReturn(mockReports);

        mockMvc.perform(get("/api/users/reports")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(mockReports.size())));

        verify(siteUserInfoService).getAllReports();
    }
}
