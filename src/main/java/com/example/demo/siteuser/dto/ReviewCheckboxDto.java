package com.example.demo.siteuser.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewCheckboxDto {
    private boolean positive1;

    public void setPositive1(boolean positive1) {
        this.positive1 = positive1;
    }

    public void setPositive2(boolean positive2) {
        this.positive2 = positive2;
    }

    public void setPositive3(boolean positive3) {
        this.positive3 = positive3;
    }

    public void setPositive4(boolean positive4) {
        this.positive4 = positive4;
    }

    public void setPositive5(boolean positive5) {
        this.positive5 = positive5;
    }

    public void setNegative1(boolean negative1) {
        this.negative1 = negative1;
    }

    public void setNegative2(boolean negative2) {
        this.negative2 = negative2;
    }

    public void setNegative3(boolean negative3) {
        this.negative3 = negative3;
    }

    public void setNegative4(boolean negative4) {
        this.negative4 = negative4;
    }

    public void setNegative5(boolean negative5) {
        this.negative5 = negative5;
    }

    public void setMatchingId(Long matchingId) {
        this.matchingId = matchingId;
    }

    public void setObjectSiteUserId(Long objectSiteUserId) {
        this.objectSiteUserId = objectSiteUserId;
    }

    public void setSubjectSiteUserId(Long subjectSiteUserId) {
        this.subjectSiteUserId = subjectSiteUserId;
    }

    public boolean isPositive1() {
        return positive1;
    }

    public boolean isPositive2() {
        return positive2;
    }

    public boolean isPositive3() {
        return positive3;
    }

    public boolean isPositive4() {
        return positive4;
    }

    public boolean isPositive5() {
        return positive5;
    }

    public boolean isNegative1() {
        return negative1;
    }

    public boolean isNegative2() {
        return negative2;
    }

    public boolean isNegative3() {
        return negative3;
    }

    public boolean isNegative4() {
        return negative4;
    }

    public boolean isNegative5() {
        return negative5;
    }

    public Long getMatchingId() {
        return matchingId;
    }

    public Long getObjectSiteUserId() {
        return objectSiteUserId;
    }

    public Long getSubjectSiteUserId() {
        return subjectSiteUserId;
    }

    private boolean positive2;
    private boolean positive3;
    private boolean positive4;
    private boolean positive5;
    private boolean negative1;
    private boolean negative2;
    private boolean negative3;
    private boolean negative4;
    private boolean negative5;

    private Long matchingId;
    private Long objectSiteUserId;
    private Long subjectSiteUserId;
}
