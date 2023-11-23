package com.example.demo.matching.dto;

import com.example.demo.entity.Matching;
import com.example.demo.type.MatchingType;
import com.example.demo.type.Ntrp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchingPreviewDto {
    private Long id;
    private boolean isReserved;
    private MatchingType matchingType;
    private Ntrp ntrp;
    private String title;
    private String matchingStartDateTime;

    public static MatchingPreviewDto fromEntity(Matching matching){
        return MatchingPreviewDto.builder()
                .id(matching.getId())
                .isReserved(matching.getIsReserved())
                .matchingType(matching.getMatchingType())
                .ntrp(matching.getNtrp())
                .title(matching.getTitle())
                .matchingStartDateTime(matching.getDate().toString()
                        + " "+ matching.getStartTime().toString())
                .build();
    }
}