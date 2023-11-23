package com.example.demo.siteuser.dto;

import com.example.demo.entity.Notification;
import com.example.demo.entity.SiteUser;
import com.example.demo.type.AgeGroup;
import com.example.demo.type.GenderType;
import com.example.demo.type.Ntrp;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SiteUserNotificationDto {
    private Long matchingId;
    private String title;
    private String content;
    private String createTime;

    public static SiteUserNotificationDto fromEntity(Notification notification) {
        return SiteUserNotificationDto.builder()
                .matchingId(notification.getMatching().getId())
                .title(notification.getMatching().getTitle())
                .content(notification.getContent())
                .createTime(notification.getCreateTime().toString())
                .build();
    }
}
