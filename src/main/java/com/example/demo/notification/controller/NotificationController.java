package com.example.demo.notification.controller;

import com.example.demo.auth.security.TokenProvider;
import com.example.demo.common.ResponseDto;
import com.example.demo.common.ResponseUtil;
import com.example.demo.notification.service.NotificationService;
import com.example.demo.siteuser.repository.SiteUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final SiteUserRepository siteUserRepository;
    private final TokenProvider tokenProvider;
    @GetMapping(value = "/connect/{accessToken}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseDto<SseEmitter> connect(@PathVariable(value = "accessToken") String accessToken) {

        String email = tokenProvider.getUserEmail(accessToken);
        var siteUser = siteUserRepository.findByEmail(email);
        var result = notificationService.connectNotification(siteUser.get().getId());
        return ResponseUtil.SUCCESS(result);
    }
}