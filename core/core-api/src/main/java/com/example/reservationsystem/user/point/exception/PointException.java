package com.example.reservationsystem.user.point.exception;

import com.example.reservationsystem.common.exception.BaseException;
import com.example.reservationsystem.common.exception.BaseExceptionType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PointException extends BaseException {

    private final PointExceptionType pointExceptionType;

    @Override
    public BaseExceptionType exceptionType() {
        return pointExceptionType;
    }

}
