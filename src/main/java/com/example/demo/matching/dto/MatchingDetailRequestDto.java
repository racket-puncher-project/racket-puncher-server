package com.example.demo.matching.dto;

import com.example.demo.entity.Matching;
import com.example.demo.type.AgeGroup;
import com.example.demo.type.MatchingType;
import com.example.demo.type.Ntrp;
import com.example.demo.type.RecruitStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchingDetailRequestDto {
    private long id;
    private long creatorUserId;
    private String title;
    private String content;
    private String location;
    private double lat;
    private double lon;
    private String locationImg;
    private String date;
    private String startTime;
    private String endTime;
    private String recruitDueDate;
    private String recruitDueTime;
    private int recruitNum;
    private int cost;
    private Boolean isReserved;
    private Ntrp ntrp;
    private AgeGroup ageGroup;
    private RecruitStatus recruitStatus;
    private MatchingType matchingType;
    private int confirmedNum;
    private String createTime;
}