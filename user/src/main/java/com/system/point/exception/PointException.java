package com.system.point.exception;

import com.system.exception.BaseException;
import com.system.exception.BaseExceptionType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PointException extends BaseException {

    private final PointExceptionType pointExceptionType;

    @Override
    public BaseExceptionType exceptionType() {
        return pointExceptionType;
    }

}
