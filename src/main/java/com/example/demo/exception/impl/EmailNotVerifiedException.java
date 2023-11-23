package com.example.demo.exception.impl;

import com.example.demo.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class EmailNotVerifiedException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "이메일 인증 후 로그인해주세요.";
    }
}
