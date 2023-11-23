package com.example.demo.openfeign.dto.address;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AddressResponseDto {
    private String roadAddr;
    private String jibunAddr;
    private String zipNo;
}
