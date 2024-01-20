package com.example.demo.matching.dto;

import com.example.demo.matching.filter.Region;
import com.example.demo.type.AgeGroup;
import com.example.demo.type.MatchingType;
import com.example.demo.type.Ntrp;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilterDto {    private String date;
    private List<Region> regions;
    private List<MatchingType> matchingTypes;
    private List<AgeGroup> ageGroups;
    private List<Ntrp> ntrps;

    public static boolean isFilterDtoEmpty(FilterDto filterDto) {
        return filterDto.getDate().isBlank()
                && filterDto.getRegions().isEmpty()
                && filterDto.getMatchingTypes().isEmpty()
                && filterDto.getAgeGroups().isEmpty()
                && filterDto.getNtrps().isEmpty();
    }
}
