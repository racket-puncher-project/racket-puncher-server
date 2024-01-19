package com.example.demo.siteuser.controller;

import com.example.demo.aws.S3Uploader;
import com.example.demo.common.ResponseDto;
import com.example.demo.common.ResponseUtil;
import com.example.demo.siteuser.dto.*;
import com.example.demo.siteuser.service.SiteUserService;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class SiteUserController {
    private final SiteUserService siteUserService;
    private final S3Uploader s3Uploader;

    @GetMapping("/profile/{userId}")
    public ResponseDto<SiteUserInfoDto> getSiteUserInfo(@PathVariable(value = "userId") Long userId) {
        var result = siteUserService.getSiteUserInfo(userId);

        return ResponseUtil.SUCCESS(result);

    }

    @GetMapping("/my-page")
    public ResponseDto<MyInfoDto> getMyInfo(Principal principal) {
        var email = principal.getName();
        var result = siteUserService.getMyInfo(email);

        return  ResponseUtil.SUCCESS(result);
    }

    @GetMapping("/my-page/hosted")
    public ResponseDto<List<HostedMatchingDto>> getMatchingHostedBySiteUser(Principal principal) {
        var email = principal.getName();
        var result = siteUserService.getMatchingHostedBySiteUser(email);
        return ResponseUtil.SUCCESS(result);
    }

    @GetMapping("/my-page/applied")
    public ResponseDto<List<AppliedMatchingDto>> getMatchingAppliedBySiteUser(Principal principal) {
        var email = principal.getName();
        var result = siteUserService.getMatchingAppliedBySiteUser(email);
        return ResponseUtil.SUCCESS(result);
    }

    @PatchMapping("my-page/modify")
    public void updateSiteUserInfo(@RequestBody UpdateSiteUserInfoDto updateSiteUserInfoDto, Principal principal) {
        var email = principal.getName();
        siteUserService.updateSiteUserInfo(email, updateSiteUserInfoDto);
    }

    @GetMapping("/notifications")
    public ResponseDto<List<NotificationDto>> getNotifications(Principal principal) {
        var email = principal.getName();
        var result = siteUserService.getNotifications(email);

        return  ResponseUtil.SUCCESS(result);
    }

    @GetMapping("/review/{matchingId}")
    public ResponseDto<List<ReviewPageInfoDto>> getReviewPageInfo(Principal principal, @PathVariable Long matchingId) {
        var email = principal.getName();
        var result = siteUserService.getReviewPageInfo(email, matchingId);

        return  ResponseUtil.SUCCESS(result);
    }

    @PostMapping("/review/{matchingId}")
    public void review(@RequestBody List<InputReviewDto> inputReviewDtos, Principal principal, @PathVariable Long matchingId) {
        var email = principal.getName();
        siteUserService.review(email, matchingId, inputReviewDtos);
    }
}