package com.example.reservationsystem.common.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum BusinessExceptionType implements BaseExceptionType {

    OTHER_THREAD_ASSIGNED(HttpStatus.CONFLICT, "Other Thread is Assigned"),
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
