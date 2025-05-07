package com.system.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountException extends BaseException {

    private final AccountExceptionType exceptionType;

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }

}
