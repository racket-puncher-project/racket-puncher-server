package com.example.demo.siteuser.service;

import com.example.demo.entity.Matching;
import com.example.demo.entity.SiteUser;
import com.example.demo.siteuser.dto.*;
import com.example.demo.type.PenaltyCode;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;


public interface SiteUserInfoService {
    SiteUserInfoDto getSiteUserInfoById(Long userId);
    SiteUserMyInfoDto getSiteUserMyInfoById(Long userId);
    List<MatchingMyMatchingDto> getMatchingBySiteUser(Long userId);
    List<MatchingMyMatchingDto> getApplyBySiteUser(Long userId);
    void updateProfileImage(Long userId, String imageUrl);
    void updateSiteUserInfo(Long userId, SiteUserModifyDto siteUserModifyDto);
    String getProfileUrl(Long userId);
    List<SiteUserNotificationDto> getNotificationBySiteUser(Long userId);
    void deleteNotification(Long userId, Long notificationId);
    void updatePenaltyScore(Long userId, PenaltyCode penaltyCode);
    void createReportUser(ReportUserDto reportUserDto);
    List<ViewReportsDto> getAllReports();
    void processReviewCheckboxes(Long matchingId, Long objectUserId, ReviewCheckboxDto reviewDto);
}