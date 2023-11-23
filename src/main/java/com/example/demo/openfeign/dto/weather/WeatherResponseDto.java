package com.example.demo.openfeign.dto.weather;

import com.example.demo.type.PrecipitationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeatherResponseDto {

    private String precipitationProbability;
    private PrecipitationType precipitationType;
}
