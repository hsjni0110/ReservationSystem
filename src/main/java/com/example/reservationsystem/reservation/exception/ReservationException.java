package com.example.reservationsystem.reservation.exception;

import com.example.reservationsystem.common.exception.BaseException;
import com.example.reservationsystem.common.exception.BaseExceptionType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReservationException extends BaseException {

    private final ReservationExceptionType exceptionType;

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }

}
