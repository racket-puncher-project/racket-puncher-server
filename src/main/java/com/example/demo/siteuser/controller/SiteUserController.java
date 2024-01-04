package com.example.demo.siteuser.controller;

import com.example.demo.aws.S3Uploader;
import com.example.demo.common.ResponseDto;
import com.example.demo.common.ResponseUtil;
import com.example.demo.siteuser.dto.MatchingMyMatchingDto;
import com.example.demo.siteuser.dto.SiteUserInfoDto;
import com.example.demo.siteuser.dto.MyInfoDto;
import com.example.demo.siteuser.service.SiteUserService;
import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class SiteUserController {
    private final SiteUserService siteUserService;
    private final S3Uploader s3Uploader;

    @GetMapping("/profile/{userId}")
    public ResponseDto<SiteUserInfoDto> getSiteUserInfo(@PathVariable(value = "userId") Long userId) {
        SiteUserInfoDto siteUserInfoDto = siteUserService.getSiteUserInfo(userId);

        return ResponseUtil.SUCCESS(siteUserInfoDto);

    }

    @GetMapping("/my-page")
    public ResponseDto<MyInfoDto> getMyInfo(Principal principal) {
        var email = principal.getName();
        MyInfoDto myInfoDto = siteUserService.getMyInfo(email);

        return  ResponseUtil.SUCCESS(myInfoDto);

    }

    @GetMapping("/my-page/hosted/{userId}")
    public ResponseEntity<ResponseDto<List<MatchingMyMatchingDto>>> getMatchingBySiteUser(
            @PathVariable(value = "userId") Long userId) {
        List<MatchingMyMatchingDto> matchingMyHostedDtos = siteUserService.getMatchingBySiteUser(userId);

        if (!matchingMyHostedDtos.isEmpty()) {
            return new ResponseEntity<>(ResponseUtil.SUCCESS(matchingMyHostedDtos), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ResponseUtil.SUCCESS(Collections.emptyList()), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/my-page/apply/{userId}")
    public ResponseEntity<ResponseDto<List<MatchingMyMatchingDto>>> findApplyBySiteUser_Id(
            @PathVariable(value = "userId") Long userId) {
        List<MatchingMyMatchingDto> matchingMyAppliedDtos = siteUserService.getApplyBySiteUser(userId);

        if (!matchingMyAppliedDtos.isEmpty()) {
            return new ResponseEntity<>(ResponseUtil.SUCCESS(matchingMyAppliedDtos), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ResponseUtil.SUCCESS(Collections.emptyList()), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("{userId}/upload-profile-image")
    public ResponseEntity<?> uploadOrUpdateProfileImage(@PathVariable Long userId,
                                                        @RequestParam("imageFile") MultipartFile imageFile) {
        try {
//            // 기존 이미지 URL 가져오기 및 삭제
//            String oldImageUrl = siteUserInfoService.getProfileUrl(userId);
//            if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
//                s3Uploader.deleteFile(oldImageUrl);
//            }

            // 새 이미지 업로드 및 URL 반환
            String newImageUrl = s3Uploader.uploadFile(imageFile);

            // SiteUser의 profileImg 필드 업데이트
            siteUserService.updateProfileImage(userId, newImageUrl);

            return new ResponseEntity<>(newImageUrl, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to upload image", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @PatchMapping("my-page/modify/{userId}")
//    public ResponseEntity<?> updateSiteUser(@PathVariable Long userId, @RequestBody SiteUserModifyDto siteUserModifyDto) {
//    }

}