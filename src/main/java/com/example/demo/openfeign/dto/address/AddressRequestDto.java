package com.example.demo.openfeign.dto.address;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AddressRequestDto {

    private int currentPage;
    private int countPerPage;
    private String keyword;
    private String resultType;
    private String hstryYn;
    private String firstSort;

    public static AddressRequestDto fromKeyword(String keyword) {
        return AddressRequestDto.builder()
                .currentPage(1)
                .countPerPage(15)
                .keyword(keyword)
                .resultType("json")
                .hstryYn("Y")
                .firstSort("none")
                .build();
    }
}
