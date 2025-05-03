package com.example.reservationsystem.common.exception;

import com.example.reservationsystem.common.domain.model.AggregateEvent;

public interface KafkaExceptionHandler {

    boolean supports(Exception e);
    void handle(Exception e, AggregateEvent event);

}
