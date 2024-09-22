package com.example.reservationsystem.auth.exception;

import com.example.reservationsystem.common.exception.BaseExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum AuthExceptionType implements BaseExceptionType {

    FORBIDDEN_AUTHORITY(HttpStatus.FORBIDDEN, "허가되지 않은 권한입니다."),
    INVALID_TOKEN_FORMAT(HttpStatus.BAD_REQUEST, "적합하지 못한 토큰 형식입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public HttpStatus httpStatus() {
        return null;
    }

    @Override
    public String errorMessage() {
        return "";
    }

}
