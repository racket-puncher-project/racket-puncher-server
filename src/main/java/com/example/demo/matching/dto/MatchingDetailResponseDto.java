package com.example.demo.matching.dto;

import com.example.demo.entity.Matching;
import com.example.demo.type.AgeGroup;
import com.example.demo.type.MatchingType;
import com.example.demo.type.Ntrp;
import com.example.demo.type.RecruitStatus;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchingDetailResponseDto {
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
    private String recruitDueDateTime;
    private int recruitNum;
    private int cost;
    private Boolean isReserved;
    private Ntrp ntrp;
    private AgeGroup ageGroup;
    private RecruitStatus recruitStatus;
    private MatchingType matchingType;
    private int confirmedNum;
    private String createTime;

    public static MatchingDetailResponseDto fromEntity(Matching matching) {
        return MatchingDetailResponseDto.builder()
                .id(matching.getId())
                .creatorUserId(matching.getSiteUser().getId())
                .title(matching.getTitle())
                .content(matching.getContent())
                .location(matching.getLocation())
                .lat(matching.getLat())
                .lon(matching.getLon())
                .locationImg(matching.getLocationImg())
                .date(matching.getDate().toString())
                .startTime(matching.getStartTime().toString())
                .endTime(matching.getEndTime().toString())
                .recruitDueDateTime(matching.getRecruitDueDateTime().toString())
                .recruitNum(matching.getRecruitNum())
                .confirmedNum(matching.getConfirmedNum())
                .cost(matching.getCost())
                .isReserved(matching.getIsReserved())
                .ntrp(matching.getNtrp())
                .ageGroup(matching.getAge())
                .recruitStatus(matching.getRecruitStatus())
                .matchingType(matching.getMatchingType())
                .createTime(matching.getCreateTime().toString())
                .build();
    }
}