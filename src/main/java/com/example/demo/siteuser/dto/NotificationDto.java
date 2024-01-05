package com.example.demo.siteuser.dto;

import com.example.demo.entity.Notification;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDto {
    private Long matchingId;
    private String title;
    private String content;
    private String createTime;

    public static NotificationDto fromEntity(Notification notification) {
        return NotificationDto.builder()
                .matchingId(notification.getMatching().getId())
                .title(notification.getMatching().getTitle())
                .content(notification.getContent())
                .createTime(notification.getCreateTime().toString())
                .build();
    }
}
