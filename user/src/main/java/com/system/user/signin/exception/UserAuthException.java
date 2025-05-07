package com.system.user.signin.exception;

import com.system.exception.BaseException;
import com.system.exception.BaseExceptionType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserAuthException extends BaseException {

    private final UserAuthExceptionType exceptionType;

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }

}
