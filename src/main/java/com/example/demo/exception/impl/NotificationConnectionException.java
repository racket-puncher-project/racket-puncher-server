package com.example.demo.exception.impl;

import com.example.demo.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NotificationConnectionException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "알림 연결에 문제가 발생하였습니다.";
    }
}
