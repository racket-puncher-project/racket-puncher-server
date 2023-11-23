package com.example.demo.matching.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilterRequestDto {
    private LocationDto location;
    private FilterDto filters;
}