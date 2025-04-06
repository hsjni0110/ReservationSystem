package com.example.reservationsystem.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EventException extends BaseException{

    private final EventExceptionType eventException;

    @Override
    public BaseExceptionType exceptionType() {
        return eventException;
    }

}
