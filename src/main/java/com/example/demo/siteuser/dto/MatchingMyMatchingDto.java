package com.example.demo.siteuser.dto;

import com.example.demo.entity.Matching;
import com.example.demo.type.MatchingType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchingMyMatchingDto {
    private Long id;
    private String title;
    private String location;
    private String date;
    private MatchingType matchingType;

    public static MatchingMyMatchingDto fromEntity(Matching matching) {
        return MatchingMyMatchingDto.builder()
                .id(matching.getId())
                .title(matching.getTitle())
                .location(matching.getLocation())
                .date(matching.getDate().toString())
                .matchingType(matching.getMatchingType())
                .build();
    }
}
