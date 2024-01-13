package com.example.demo.openfeign.feignclient;

import com.example.demo.matching.dto.LatAndLonResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "latAndLonApi", url = "https://dapi.kakao.com/v2/local/search")
public interface LatAndLonApiFeignClient {
    @RequestMapping(method = RequestMethod.GET, value = "/address")
    LatAndLonResponseDto getLatAndLon(@RequestParam("query") String address, @RequestHeader("Authorization") String apiKey);
}