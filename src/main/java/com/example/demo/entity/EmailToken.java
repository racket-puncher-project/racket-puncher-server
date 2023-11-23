/*
package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "EMAIL_TOKEN")
public class EmailToken {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;

    @Column(name = "EXPIRATION_TIME", nullable = false)
    private LocalDateTime expirationTime;

    @Column(name = "SITE_USER_ID", nullable = false)
    private Long siteUserId;

    private static final long EMAIL_TOKEN_EXPIRATION_TIME_VALUE = 5L; // 토큰 만료 시간

    // 이메일 인증 토큰 생성
    public static EmailToken createEmailToken(Long siteUserId) {
        EmailToken emailToken = new EmailToken();
        emailToken.expirationTime = LocalDateTime.now().plusMinutes(EMAIL_TOKEN_EXPIRATION_TIME_VALUE); // 5분 후 만료
        emailToken.siteUserId = siteUserId;

        return emailToken;
    }
}*/
