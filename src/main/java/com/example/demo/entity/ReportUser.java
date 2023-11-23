package com.example.demo.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "REPORT_USER")
public class ReportUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public SiteUser getReportingUser() {
        return reportingUser;
    }

    public void setReportingUser(SiteUser reportingUser) {
        this.reportingUser = reportingUser;
    }

    public SiteUser getReportedUser() {
        return reportedUser;
    }

    public void setReportedUser(SiteUser reportedUser) {
        this.reportedUser = reportedUser;
    }

    @ManyToOne
    @JoinColumn(name = "REPORTING_SITE_USER_ID", nullable = false)
    private SiteUser reportingUser;

    @ManyToOne
    @JoinColumn(name = "REPORTED_SITE_USER_ID", nullable = false)
    private SiteUser reportedUser;

    @Column(name = "TITLE", length = 50, nullable = false)
    private String title;

    @Column(name = "CONTENT", length = 1023)
    private String content;


    @CreatedDate
    @Column(name = "CREATE_TIME") // yyyy-MM-dd HH:mm
    private LocalDateTime createTime;


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
