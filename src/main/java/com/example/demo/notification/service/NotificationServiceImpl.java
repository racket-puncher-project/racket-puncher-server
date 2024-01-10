package com.example.demo.notification.service;

import static com.example.demo.exception.type.ErrorCode.*;

import com.example.demo.entity.Matching;
import com.example.demo.entity.Notification;
import com.example.demo.entity.SiteUser;
import com.example.demo.exception.RacketPuncherException;
import com.example.demo.notification.dto.NotificationDto;
import com.example.demo.notification.repository.EmitterRepository;
import com.example.demo.notification.repository.NotificationRepository;
import com.example.demo.type.NotificationType;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final static Long DEFAULT_TIMEOUT = 3600000L;
    private final static String NOTIFICATION_NAME = "notify";

    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public SseEmitter connectNotification(Long userId) {
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(userId, sseEmitter);

        sseEmitter.onCompletion(() -> emitterRepository.delete(userId));
        sseEmitter.onTimeout(() -> emitterRepository.delete(userId));

        try {
            sseEmitter.send(SseEmitter.event().id("").name(NOTIFICATION_NAME).data("Connection succeed"));
        } catch (IOException e) {
            throw new RacketPuncherException(NOTIFICATION_CONNECTION_FAILED);
        }
        return sseEmitter;
    }

    @Override
    public void send(Long userId, Notification notification) {
        emitterRepository.get(userId).ifPresentOrElse(sseEmitter -> {
            try {
                sseEmitter.send(SseEmitter.event().id(notification.getId().toString())
                        .name(notification.getNotificationType().toString())
                        .data(notification.getNotificationType().getMessage()));
            } catch (IOException e) {
                emitterRepository.delete(userId);
                throw new RacketPuncherException(NOTIFICATION_CONNECTION_FAILED);
            }
        }, () -> log.info("No emitter found"));
    }

    @Transactional
    @Override
    public Notification createNotification(NotificationDto notificationDto) {
        return notificationRepository.save(Notification.fromDto(notificationDto));
    }

    @Transactional
    @Override
    public void createAndSendNotification(SiteUser siteUser, Matching matching, NotificationType notificationType) {
        var notificationDto = NotificationDto.builder()
                .siteUser(siteUser)
                .matching(matching)
                .notificationType(notificationType)
                .content(notificationType.getMessage())
                .build();

        var notification = createNotification(notificationDto);
        send(siteUser.getId(), notification);
    }
}
