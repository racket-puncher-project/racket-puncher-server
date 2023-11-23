package com.example.demo.notification.service;

import com.example.demo.entity.Matching;
import com.example.demo.entity.Notification;
import com.example.demo.entity.SiteUser;
import com.example.demo.notification.dto.NotificationDto;
import com.example.demo.type.NotificationType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {

    SseEmitter connectNotification(Long userId);

    void send(Long userId, Notification notification);

    Notification createNotification(NotificationDto notificationDto);

    void createAndSendNotification(SiteUser siteUser, Matching matching, NotificationType notificationType);
}
