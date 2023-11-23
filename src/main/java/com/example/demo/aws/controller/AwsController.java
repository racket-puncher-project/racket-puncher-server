package com.example.demo.aws.controller;

import com.example.demo.aws.S3Uploader;
import com.example.demo.common.ResponseDto;
import com.example.demo.common.ResponseUtil;
import com.example.demo.exception.impl.S3UploadFailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/aws")
public class AwsController {

    private final S3Uploader s3Uploader;

    @PostMapping("/upload-image")
    public ResponseDto<String> uploadImage(
           @RequestParam("imageFile") MultipartFile imageFile
    ) {
        try {
            String profileImageUrl = s3Uploader.uploadFile(imageFile);
            return ResponseUtil.SUCCESS(profileImageUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseUtil.SUCCESS("이미지 업로드 실패");
        }
    }
}