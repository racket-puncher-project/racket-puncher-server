package com.example.demo.apply.dto;

import com.example.demo.entity.Apply;
import com.example.demo.entity.Matching;
import com.example.demo.entity.SiteUser;
import com.example.demo.type.ApplyStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplyDto {

    private Matching matching;
    private SiteUser siteUser;
    private String createTime;
    private ApplyStatus applyStatus;

    public static ApplyDto fromEntity(Apply apply) {
        return ApplyDto.builder()
                .matching(apply.getMatching())
                .siteUser(apply.getSiteUser())
                .createTime(apply.getCreateTime().toString())
                .applyStatus(apply.getApplyStatus())
                .build();
    }
}
