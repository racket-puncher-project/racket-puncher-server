package com.example.demo.openfeign.service.weather;

import com.example.demo.openfeign.dto.weather.WeatherResponseDto;
import com.example.demo.notification.dto.LocationAndDateFromMatching;

public interface WeatherService {

    WeatherResponseDto getWeather(LocationAndDateFromMatching locationAndDateFromMatching);
}
