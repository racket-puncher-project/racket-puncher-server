package com.example.demo.siteuser.controller;

import com.example.demo.aws.S3Uploader;
import com.example.demo.common.ResponseDto;
import com.example.demo.common.ResponseUtil;
import com.example.demo.entity.Matching;
import com.example.demo.entity.SiteUser;
import com.example.demo.siteuser.dto.*;
import com.example.demo.siteuser.service.SiteUserInfoService;
import com.example.demo.type.PenaltyCode;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class SiteUserInfoController {
    private final SiteUserInfoService siteUserInfoService;
    private final S3Uploader s3Uploader;

    @GetMapping("/profile/{userId}")
    public ResponseEntity<ResponseDto<SiteUserInfoDto>> getSiteUserInfoById(@PathVariable(value = "userId") Long siteUser) {
        SiteUserInfoDto siteUserInfoDto = siteUserInfoService.getSiteUserInfoById(siteUser);

        if (siteUserInfoDto != null) {
            return new ResponseEntity<>(ResponseUtil.SUCCESS(siteUserInfoDto), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ResponseUtil.SUCCESS(null), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/my-page/{userId}")
    public ResponseEntity<ResponseDto<SiteUserMyInfoDto>> getSiteUserMyInfoById(@PathVariable(value = "userId") Long siteUser) {
        SiteUserMyInfoDto siteUserMyInfoDto = siteUserInfoService.getSiteUserMyInfoById(siteUser);

        if (siteUserMyInfoDto != null) {
            return new ResponseEntity<>(ResponseUtil.SUCCESS(siteUserMyInfoDto), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ResponseUtil.SUCCESS(null), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/my-page/hosted/{userId}")
    public ResponseEntity<ResponseDto<List<MatchingMyMatchingDto>>> getMatchingBySiteUser(@PathVariable(value = "userId") Long userId) {
        List<MatchingMyMatchingDto> matchingMyHostedDtos = siteUserInfoService.getMatchingBySiteUser(userId);

        if (!matchingMyHostedDtos.isEmpty()) {
            return new ResponseEntity<>(ResponseUtil.SUCCESS(matchingMyHostedDtos), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ResponseUtil.SUCCESS(Collections.emptyList()), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/my-page/apply/{userId}")
    public ResponseEntity<ResponseDto<List<MatchingMyMatchingDto>>> findApplyBySiteUser_Id(@PathVariable(value = "userId") Long userId) {
        List<MatchingMyMatchingDto> matchingMyAppliedDtos = siteUserInfoService.getApplyBySiteUser(userId);

        if (!matchingMyAppliedDtos.isEmpty()) {
            return new ResponseEntity<>(ResponseUtil.SUCCESS(matchingMyAppliedDtos), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ResponseUtil.SUCCESS(Collections.emptyList()), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("{userId}/upload-profile-image")
    public ResponseEntity<?> uploadOrUpdateProfileImage(@PathVariable Long userId, @RequestParam("imageFile") MultipartFile imageFile) {
        try {
//            // 기존 이미지 URL 가져오기 및 삭제
//            String oldImageUrl = siteUserInfoService.getProfileUrl(userId);
//            if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
//                s3Uploader.deleteFile(oldImageUrl);
//            }

            // 새 이미지 업로드 및 URL 반환
            String newImageUrl = s3Uploader.uploadFile(imageFile);

            // SiteUser의 profileImg 필드 업데이트
            siteUserInfoService.updateProfileImage(userId, newImageUrl);

            return new ResponseEntity<>(newImageUrl, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to upload image", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("my-page/modify/{userId}")
    public ResponseEntity<?> updateSiteUser(@PathVariable Long userId, @RequestBody SiteUserModifyDto siteUserModifyDto) {
        siteUserInfoService.updateSiteUserInfo(userId, siteUserModifyDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/my-page/notification/{userId}")
    public ResponseEntity<ResponseDto<List<SiteUserNotificationDto>>> getNotificationBySiteUser(@PathVariable(value = "userId") Long userId) {
        List<SiteUserNotificationDto> siteUserNotificationDtos = siteUserInfoService.getNotificationBySiteUser(userId);

        if (!siteUserNotificationDtos.isEmpty()) {
            return new ResponseEntity<>(ResponseUtil.SUCCESS(siteUserNotificationDtos), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ResponseUtil.SUCCESS(Collections.emptyList()), HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    @DeleteMapping("/my-page/notification/{userId}/{notificationId}")
    public ResponseEntity<?> deleteNotification(@PathVariable Long userId, @PathVariable Long notificationId) {
        try {
            siteUserInfoService.deleteNotification(userId, notificationId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();  // Log the exception
            return new ResponseEntity<>("Error occurred while deleting the notification: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/penalty/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> applyPenalty(@PathVariable Long userId, @RequestBody SiteUserPenaltyDto siteUserPenaltyDto) {
        try {
            // DTO에서 penaltyCode를 가져와서 enum으로 변환
            PenaltyCode penaltyCode = PenaltyCode.fromString(siteUserPenaltyDto.getPenaltyCode().toUpperCase());
            siteUserInfoService.updatePenaltyScore(userId, penaltyCode);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            // 여기서도 DTO의 값을 사용하여 오류 메시지를 생성
            return new ResponseEntity<>("Invalid penalty code: " + siteUserPenaltyDto.getPenaltyCode(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/report")
    public ResponseEntity<?> createReportUser(@RequestBody ReportUserDto reportUserDto) {
        try {
            siteUserInfoService.createReportUser(reportUserDto);
            return new ResponseEntity<>(null, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/reports")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto<List<ViewReportsDto>>> getAllReports() {
        List<ViewReportsDto> reportList = siteUserInfoService.getAllReports();

        if (!reportList.isEmpty()) {
            return new ResponseEntity<>(ResponseUtil.SUCCESS(reportList), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ResponseUtil.SUCCESS(Collections.emptyList()), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/register-review/{matchingId}/{objectUserId}")
    public ResponseEntity<ResponseDto<Void>> submitReview(@PathVariable Long matchingId,
                                                          @PathVariable Long objectUserId,
                                                          @RequestBody ReviewCheckboxDto reviewDto) {
        siteUserInfoService.processReviewCheckboxes(matchingId, objectUserId, reviewDto);
        ResponseDto<Void> response = ResponseUtil.SUCCESS(null);
        return ResponseEntity.ok(response);
    }
}