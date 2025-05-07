package com.system.vehicle.exception;

import com.system.exception.BaseExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
public enum VehicleExceptionType implements BaseExceptionType {

    ROUTE_NOT_FOUND(NOT_FOUND, "Route Not Found"),
    ROUTE_TIME_SLOT_NOT_FOUND(NOT_FOUND, "Route Time Slot Not Found"),
    DUPLICATE_ROUTE(CONFLICT, "Duplicate Route"),
    DUPLICATE_DISPATCH_TIME(CONFLICT, "Duplicate Dispatch Time"),
    BUS_CAPACITY_OVERFLOW(CONFLICT, "Bus Capacity Overflow"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }

    @Override
    public String errorMessage() {
        return message;
    }

}
