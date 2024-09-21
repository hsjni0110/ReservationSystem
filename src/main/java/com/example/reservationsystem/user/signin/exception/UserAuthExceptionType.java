package com.example.reservationsystem.user.signin.exception;

import com.example.reservationsystem.common.BaseExceptionType;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum UserAuthExceptionType implements BaseExceptionType {

    INVALID_PASSWORD(HttpStatus.FORBIDDEN, "잘못된 비밀번호입니다."),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "주어진 이메일의 유저를 찾지 못했습니다."),
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
