package com.example.demo.notification.controller;

import com.example.demo.auth.dto.AccessTokenDto;
import com.example.demo.auth.security.TokenProvider;
import com.example.demo.notification.service.NotificationService;
import com.example.demo.siteuser.repository.SiteUserRepository;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
    @GetMapping("/connect/{accessToken}")
    public SseEmitter connect(@PathVariable(value = "accessToken") String accessToken) {

        String email = tokenProvider.getUserEmail(accessToken);
        var siteUser = siteUserRepository.findByEmail(email);
        return notificationService.connectNotification(siteUser.get().getId());
    }
}