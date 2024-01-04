package com.example.demo.siteuser.service;

import com.example.demo.entity.SiteUser;
import com.example.demo.siteuser.dto.MatchingMyMatchingDto;
import com.example.demo.siteuser.dto.SiteUserInfoDto;
import com.example.demo.siteuser.dto.MyInfoDto;
import com.example.demo.siteuser.dto.NotificationDto;
import com.example.demo.siteuser.dto.UpdateSiteUserInfoDto;
import java.util.List;


public interface SiteUserService {
    SiteUserInfoDto getSiteUserInfo(Long userId);
    MyInfoDto getMyInfo(String email);
    List<MatchingMyMatchingDto> getMatchingBySiteUser(Long userId);
    List<MatchingMyMatchingDto> getApplyBySiteUser(Long userId);
    SiteUser updateSiteUserInfo(String email, UpdateSiteUserInfoDto updateSiteUserInfoDto);

    List<NotificationDto> getNotifications(String email);
}