package com.example.reservationsystem.vehicle.exception;

import com.example.reservationsystem.common.exception.BaseException;
import com.example.reservationsystem.common.exception.BaseExceptionType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VehicleException extends BaseException {

    private final VehicleExceptionType exceptionType;

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }

}
