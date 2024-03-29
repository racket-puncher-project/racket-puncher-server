package com.example.demo.notification.controller;

import com.example.demo.notification.service.NotificationService;
import com.example.demo.siteuser.repository.SiteUserRepository;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final SiteUserRepository siteUserRepository;
    @GetMapping("/connect")
    public SseEmitter connect(Principal principal) {

        String email = principal.getName();
        var siteUser = siteUserRepository.findByEmail(email);
        return notificationService.connectNotification(siteUser.get().getId());
    }
}