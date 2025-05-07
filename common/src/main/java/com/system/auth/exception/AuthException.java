package com.system.auth.exception;

import com.system.exception.BaseException;
import com.system.exception.BaseExceptionType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthException extends BaseException {

    private final AuthExceptionType exceptionType;

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
    
}
