package com.example.demo.notification.dto;

import com.example.demo.entity.Matching;
import com.example.demo.entity.SiteUser;
import com.example.demo.type.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDto {
    private SiteUser siteUser;
    private Matching matching;
    private NotificationType notificationType;
    private String content;
}
