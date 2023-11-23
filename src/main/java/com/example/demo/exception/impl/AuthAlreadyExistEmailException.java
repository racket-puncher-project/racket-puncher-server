package com.example.demo.exception.impl;

import com.example.demo.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class AuthAlreadyExistEmailException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "이미 사용 중인 이메일입니다.";
    }
}
