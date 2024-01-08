package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "REVIEW")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MATCHING_ID", nullable = false)
    private Matching matching;

    @ManyToOne
    @JoinColumn(name = "OBJECT_USER_ID", nullable = false)
    private SiteUser objectUser;

    @ManyToOne
    @JoinColumn(name = "SUBJECT_USER_ID", nullable = false)
    private SiteUser subjectUser;

    @Column(name = "SCORE", nullable = false)
    private Integer score;

    @CreatedDate
    @Column(name = "CREATE_TIME")
    private LocalDateTime createTime;

    @Column(name = "POSITIVE_SCORE", nullable = false)
    private Integer positiveScore;

    @Column(name = "NEGATIVE_SCORE", nullable = false)
    private Integer negativeScore;

}