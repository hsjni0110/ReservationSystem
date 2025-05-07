package com.system.point.exception;

import com.system.exception.BaseExceptionType;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum PointExceptionType implements BaseExceptionType {

    ALREADY_ADDED_POINT( HttpStatus.ALREADY_REPORTED, "이미 적립된 포인트입니다." ),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }

    @Override
    public String errorMessage() {
        return message;
    }

}
