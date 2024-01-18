package com.example.demo.siteuser.service;

import com.example.demo.entity.SiteUser;
import com.example.demo.siteuser.dto.*;

import java.util.List;


public interface SiteUserService {
    SiteUserInfoDto getSiteUserInfo(Long userId);
    MyInfoDto getMyInfo(String email);
    List<HostedMatchingDto> getMatchingHostedBySiteUser(String email);
    List<AppliedMatchingDto> getMatchingAppliedBySiteUser(String email);
    SiteUser updateSiteUserInfo(String email, UpdateSiteUserInfoDto updateSiteUserInfoDto);
    List<NotificationDto> getNotifications(String email);
    List<ReviewPageInfoDto> getReviewPageInfo(String email, Long matchingId);
    void review(String email, Long matchingId, List<InputReviewDto> inputReviewDtos);
}