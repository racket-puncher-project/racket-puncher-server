package com.example.demo.siteuser.service;

import com.example.demo.siteuser.dto.MatchingMyMatchingDto;
import com.example.demo.siteuser.dto.SiteUserInfoDto;
import com.example.demo.siteuser.dto.MyInfoDto;
import com.example.demo.siteuser.dto.SiteUserNotificationDto;
import java.util.List;


public interface SiteUserService {
    SiteUserInfoDto getSiteUserInfo(Long userId);
    MyInfoDto getMyInfo(String email);
    List<MatchingMyMatchingDto> getMatchingBySiteUser(Long userId);
    List<MatchingMyMatchingDto> getApplyBySiteUser(Long userId);
    void updateProfileImage(Long userId, String imageUrl);
    String getProfileUrl(Long userId);
    List<SiteUserNotificationDto> getNotificationBySiteUser(Long userId);
}