package com.example.reservationsystem.common.exception;

import org.springframework.http.HttpStatus;

public interface BaseExceptionType {

    HttpStatus httpStatus();

    String errorMessage();

}
