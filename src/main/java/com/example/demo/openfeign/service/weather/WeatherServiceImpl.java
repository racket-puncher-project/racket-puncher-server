package com.example.demo.openfeign.service.weather;

import com.example.demo.notification.dto.LocationAndDateFromMatching;
import com.example.demo.openfeign.dto.weather.Item;
import com.example.demo.openfeign.dto.weather.WeatherRequestDto;
import com.example.demo.openfeign.dto.weather.WeatherResponse;
import com.example.demo.openfeign.dto.weather.WeatherResponseDto;
import com.example.demo.openfeign.feignclient.WeatherApiFeignClient;
import com.example.demo.type.PrecipitationType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final WeatherApiFeignClient weatherApiFeignClient;
    @Value("${weather-api.key}")
    private String apiKey;

    @Override
    public WeatherResponseDto getWeather(LocationAndDateFromMatching
                                                 locationAndDateFromMatching) {
        WeatherRequestDto weatherRequestDto
                = WeatherRequestDto.fromLocationAndDate(locationAndDateFromMatching);
        WeatherResponse weatherResponse = weatherApiFeignClient
                .getWeather(apiKey,
                        weatherRequestDto.getNumOfRows(),
                        weatherRequestDto.getPageNo(),
                        weatherRequestDto.getDataType(),
                        weatherRequestDto.getBaseDate(),
                        weatherRequestDto.getBaseTime(),
                        weatherRequestDto.getNx(),
                        weatherRequestDto.getNy());

        List<Item> items = weatherResponse.getResponse()
                .getBody().getItems().getItem();

        String precipitationCode = items.get(6).getFcstValue();
        String precipitationProbability = items.get(7).getFcstValue();

        if (Integer.valueOf(precipitationProbability) <= 0) {
            return null;
        }

        return WeatherResponseDto.builder()
                .precipitationProbability(precipitationProbability)
                .precipitationType(PrecipitationType.findPrecipitationType(precipitationCode))
                .build();
    }
}
