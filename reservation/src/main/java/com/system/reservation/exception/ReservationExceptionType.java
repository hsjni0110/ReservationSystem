package com.system.reservation.exception;

import com.system.exception.BaseExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RequiredArgsConstructor
public enum ReservationExceptionType implements BaseExceptionType {

    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "Reservation not found"),
    ROUTE_NOT_FOUND(HttpStatus.NOT_FOUND, "Route Not Found"),
    ROUTE_SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "Route Schedule Not Found"),
    SCHEDULED_SEAT_NOT_FOUND(HttpStatus.NOT_FOUND, "Scheduled Seat Not Found"),
    ALREADY_PRESERVED_SEAT(HttpStatus.CONFLICT, "Already Preserved Seat"),
    NOT_PRESERVED_SEAT(HttpStatus.CONFLICT, "Not Preserved Seat"),
    USER_NOT_MATCHED(UNAUTHORIZED, "user not matched"),
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
