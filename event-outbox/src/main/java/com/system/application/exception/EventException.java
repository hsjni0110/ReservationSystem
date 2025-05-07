package com.system.application.exception;

import com.system.exception.BaseException;
import com.system.exception.BaseExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EventException extends BaseException {

    private final EventExceptionType eventException;

    @Override
    public BaseExceptionType exceptionType() {
        return eventException;
    }

}
