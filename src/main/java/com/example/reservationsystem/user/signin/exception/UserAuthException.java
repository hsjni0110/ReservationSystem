package com.example.reservationsystem.user.signin.exception;

import com.example.reservationsystem.common.BaseException;
import com.example.reservationsystem.common.BaseExceptionType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserAuthException extends BaseException {

    private final UserAuthExceptionType exceptionType;

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }

}
