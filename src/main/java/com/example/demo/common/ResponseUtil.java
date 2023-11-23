package com.example.demo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ResponseUtil {

    public static <T>ResponseDto<T> SUCCESS(T response) {
        return new ResponseDto(response);
    }
}