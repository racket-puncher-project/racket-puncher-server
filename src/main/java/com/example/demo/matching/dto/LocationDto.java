package com.example.demo.matching.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDto {
    private Double lat;
    private Double lon;
}