package com.example.demo.notification.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LocationAndDateFromMatching {

    private String baseDate;
    private String nx;
    private String ny;
}
