package com.example.demo.matching.dto;

import com.example.demo.type.ApplyStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplyMember {
    private long applyId;
    private long siteUserId;
    private String nickname;
}
