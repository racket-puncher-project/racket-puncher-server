package com.example.demo.matching.dto;

import com.example.demo.entity.Matching;
import com.example.demo.type.MatchingType;
import com.example.demo.type.Ntrp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchingPreviewDto {
    private Long id;
    private String locationImg;
    private boolean isReserved;
    private MatchingType matchingType;
    private Ntrp ntrp;
    private String title;
    private String recruitDueDateTime;
    private String matchingStartDateTime;
    private Double lat;
    private Double lon;

    public static MatchingPreviewDto fromEntity(Matching matching){
        return MatchingPreviewDto.builder()
                .id(matching.getId())
                .locationImg(matching.getLocationImg())
                .isReserved(matching.getIsReserved())
                .matchingType(matching.getMatchingType())
                .ntrp(matching.getNtrp())
                .title(matching.getTitle())
                .recruitDueDateTime(matching.getRecruitDueDateTime().toString())
                .matchingStartDateTime(matching.getDate().toString()
                        + "T"+ matching.getStartTime().toString())
                .lat(matching.getLat())
                .lon(matching.getLon())
                .build();
    }
}