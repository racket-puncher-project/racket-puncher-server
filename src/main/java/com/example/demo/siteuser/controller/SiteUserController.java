package com.example.demo.siteuser.controller;

import com.example.demo.aws.S3Uploader;
import com.example.demo.common.ResponseDto;
import com.example.demo.common.ResponseUtil;
import com.example.demo.entity.SiteUser;
import com.example.demo.siteuser.dto.MatchingMyMatchingDto;
import com.example.demo.siteuser.dto.SiteUserInfoDto;
import com.example.demo.siteuser.dto.MyInfoDto;
import com.example.demo.siteuser.dto.UpdateSiteUserInfoDto;
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

    @PatchMapping("my-page/modify")
    public void updateSiteUserInfo(@RequestBody UpdateSiteUserInfoDto updateSiteUserInfoDto, Principal principal) {
        var email = principal.getName();
        siteUserService.updateSiteUserInfo(email, updateSiteUserInfoDto);
    }

}