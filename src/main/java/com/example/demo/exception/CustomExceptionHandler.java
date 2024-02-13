package com.example.demo.exception;

import com.example.demo.exception.type.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.message.exception.NurigoBadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(RacketPuncherException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(RacketPuncherException e) {
        log.error("customException", e);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(e.getStatusCode())
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.resolve(errorResponse.getCode()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> handleJsonParsingException(HttpMessageNotReadableException e) {
        log.error("jsonException", e);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ErrorCode.JSON_PARSING_FAILED.getCode())
                .message(ErrorCode.JSON_PARSING_FAILED.getDescription() + " : "+ e.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.resolve(errorResponse.getCode()));
    }

    @ExceptionHandler(NurigoBadRequestException.class)
    protected ResponseEntity<ErrorResponse> handelSmsSendException(NurigoBadRequestException e) {
        log.error("smsSendException", e);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ErrorCode.SMS_SEND_FAIL.getCode())
                .message(ErrorCode.SMS_SEND_FAIL.getDescription())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.resolve(errorResponse.getCode()));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handelOtherException(Exception e) {
        log.error("Exception", e);
        return null;
    }
}
