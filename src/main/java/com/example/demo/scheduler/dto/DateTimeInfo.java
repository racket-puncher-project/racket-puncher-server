package com.example.demo.scheduler.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DateTimeInfo {
    private LocalDateTime recruitDueDateTime;
    private LocalDate today;
    private LocalTime currentTime;
}
