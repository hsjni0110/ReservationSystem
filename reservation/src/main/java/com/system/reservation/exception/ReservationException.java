package com.system.reservation.exception;

import com.system.exception.BaseException;
import com.system.exception.BaseExceptionType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReservationException extends BaseException {

    private final ReservationExceptionType exceptionType;

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }

}
