package com.example.demo.exception;

import com.example.demo.exception.type.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RacketPuncherException extends RuntimeException{
    private ErrorCode errorCode;

    public int getStatusCode() {
        return this.errorCode.getCode();
    };
    public String getMessage() {
        return this.errorCode.getDescription();
    };

}
