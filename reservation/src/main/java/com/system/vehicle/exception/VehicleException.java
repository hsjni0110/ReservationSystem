package com.system.vehicle.exception;

import com.system.exception.BaseException;
import com.system.exception.BaseExceptionType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VehicleException extends BaseException {

    private final VehicleExceptionType exceptionType;

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }

}
