package com.example.demo.siteuser.dto;

import com.example.demo.entity.ReportUser;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViewReportsDto {
    private Long id;
    private Long reportingUserId;
    private Long reportedUserId;
    private String title;
    private String content;
    private String createTime;

    public static ViewReportsDto fromEntity(ReportUser reportUser) {
        return ViewReportsDto.builder()
                .id(reportUser.getId())
                .reportingUserId(reportUser.getReportingUser().getId())
                .reportedUserId(reportUser.getReportedUser().getId())
                .title(reportUser.getTitle())
                .content(reportUser.getContent())
                .createTime(reportUser.getCreateTime().toString())
                .build();
    }
}
