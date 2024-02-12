package com.example.demo.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NewMessageArrivedDto {
    private String matchingId;
}
