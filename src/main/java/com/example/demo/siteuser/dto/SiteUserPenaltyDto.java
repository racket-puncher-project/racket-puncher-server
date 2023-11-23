package com.example.demo.siteuser.dto;

import com.example.demo.entity.Notification;
import com.example.demo.type.PenaltyCode;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SiteUserPenaltyDto {
    private String penaltyCode;

    public String getPenaltyCode() {
        return penaltyCode;
    }

    public void setPenaltyCode(String penaltyCode) {
        this.penaltyCode = penaltyCode;
    }
}
