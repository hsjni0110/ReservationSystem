package com.example.reservationsystem.reservation.exception;

import com.example.reservationsystem.common.exception.BaseExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ReservationExceptionType implements BaseExceptionType {
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "Reservation not found"),
    ROUTE_NOT_FOUND(HttpStatus.NOT_FOUND, "Route Not Found"),
    ROUTE_SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "Route Schedule Not Found"),
    SCHEDULED_SEAT_NOT_FOUND(HttpStatus.NOT_FOUND, "Scheduled Seat Not Found"),
    ALREADY_PRESERVED_SEAT(HttpStatus.CONFLICT, "Already Preserved Seat"),
    NOT_PRESERVED_SEAT(HttpStatus.CONFLICT, "Not Preserved Seat"),
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
