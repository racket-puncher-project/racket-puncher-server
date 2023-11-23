package com.example.demo.openfeign.feignclient;

import com.example.demo.openfeign.dto.address.JusoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "addressApi", url = "https://business.juso.go.kr/addrlink/addrLinkApi.do")
public interface AddressApiFeignClient {

    @GetMapping
    JusoResponse getAddress(
            @RequestParam("confmKey") String apiKey,
            @RequestParam("currentPage") Integer currentPage,
            @RequestParam("countPerPage") Integer countPerPage,
            @RequestParam("keyword") String keyword,
            @RequestParam("resultType") String resultType,
            @RequestParam("hstryYn") String hstryYn,
            @RequestParam("firstSort") String firstSort
            );
}
