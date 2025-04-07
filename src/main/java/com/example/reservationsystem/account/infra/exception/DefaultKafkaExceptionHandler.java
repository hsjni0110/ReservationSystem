package com.example.reservationsystem.account.infra.exception;

import com.example.reservationsystem.account.application.AccountEventProcessor;
import com.example.reservationsystem.common.domain.model.AggregateEvent;
import com.example.reservationsystem.common.exception.KafkaExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(999)
@Slf4j
public class DefaultKafkaExceptionHandler implements KafkaExceptionHandler {

    private final AccountEventProcessor processor;

    public DefaultKafkaExceptionHandler( AccountEventProcessor processor ) {
        this.processor = processor;
    }

    @Override
    public boolean supports( Exception e ) {
        return true;
    }

    @Override
    public void handle( Exception e, AggregateEvent event ) {
        log.error("❌ Kafka 처리 중 오류 발생: {}", event, e);
        processor.markFailure( event );
    }

}
