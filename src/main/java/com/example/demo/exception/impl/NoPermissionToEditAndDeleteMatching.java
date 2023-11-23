package com.example.demo.exception.impl;

import com.example.demo.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NoPermissionToEditAndDeleteMatching extends AbstractException {

    @Override
    public int getStatusCode() {
        return HttpStatus.FORBIDDEN.value();
    }

    @Override
    public String getMessage() {
        return "매칭글을 수정할 권한이 없습니다.";
    }
}
