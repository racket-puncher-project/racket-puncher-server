package com.example.demo.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;
import java.time.LocalDateTime;


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

    public void setId(Long id) {
        this.id = id;
    }

    public void setMatching(Matching matching) {
        this.matching = matching;
    }

    public void setObjectUser(SiteUser objectUser) {
        this.objectUser = objectUser;
    }

    public void setSubjectUser(SiteUser subjectUser) {
        this.subjectUser = subjectUser;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public void setPositiveScore(Integer positiveScore) {
        this.positiveScore = positiveScore;
    }

    public void setNegativeScore(Integer negativeScore) {
        this.negativeScore = negativeScore;
    }

    @Column(name = "NEGATIVE_SCORE", nullable = false)
    private Integer negativeScore;

    public Long getId() {
        return id;
    }

    public Matching getMatching() {
        return matching;
    }

    public SiteUser getObjectUser() {
        return objectUser;
    }

    public SiteUser getSubjectUser() {
        return subjectUser;
    }

    public Integer getScore() {
        return score;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public Integer getPositiveScore() {
        return positiveScore;
    }

    public Integer getNegativeScore() {
        return negativeScore;
    }
}