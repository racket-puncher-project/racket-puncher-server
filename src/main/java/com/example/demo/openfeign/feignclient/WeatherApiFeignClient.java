package com.example.demo.openfeign.feignclient;

import com.example.demo.openfeign.dto.address.JusoResponse;
import com.example.demo.openfeign.dto.weather.WeatherResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "weatherApi", url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst")
public interface WeatherApiFeignClient {

    @GetMapping
    WeatherResponse getWeather(
            @RequestParam("serviceKey") String serviceKey,
            @RequestParam("numOfRows") String numOfRows,
            @RequestParam("pageNo") String pageNo,
            @RequestParam("dataType") String dataType,
            @RequestParam("base_date") String baseDate,
            @RequestParam("base_time") String baseTime,
            @RequestParam("nx") String nx,
            @RequestParam("ny") String ny
            );
}
