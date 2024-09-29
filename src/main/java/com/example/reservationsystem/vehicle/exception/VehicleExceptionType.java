package com.example.reservationsystem.vehicle.exception;

import com.example.reservationsystem.common.exception.BaseExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
public enum VehicleExceptionType implements BaseExceptionType {

    ROUTE_NOT_FOUND(NOT_FOUND, "Route Not Found"),
    ROUTE_TIME_SLOT_NOT_FOUND(NOT_FOUND, "Route Time Slot Not Found"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public HttpStatus httpStatus() {
        return null;
    }

    @Override
    public String errorMessage() {
        return "";
    }

}
