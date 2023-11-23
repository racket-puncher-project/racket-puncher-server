package com.example.demo.entity;

import com.example.demo.notification.dto.NotificationDto;
import com.example.demo.type.NotificationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "NOTIFICATION")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "SITE_USER_ID", nullable = false)
    private SiteUser siteUser;

    @ManyToOne
    @JoinColumn(name = "MATCHING_ID")
    private Matching matching;

    @Enumerated(EnumType.STRING)
    @Column(name = "NOTIFICATION_TYPE", length = 50, nullable = false)
    private NotificationType notificationType;

    @Column(name = "CONTENT", length = 255, nullable = false)
    private String content;

    @CreatedDate
    @Column(name = "CREATE_TIME")
    private LocalDateTime createTime;

    public static Notification fromDto(NotificationDto notificationDto) {
        return Notification.builder()
                .siteUser(notificationDto.getSiteUser())
                .matching(notificationDto.getMatching())
                .notificationType(notificationDto.getNotificationType())
                .content(notificationDto.getContent())
                .build();
    }
}