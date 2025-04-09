package com.example.reservationsystem.account.infra.exception;

import com.example.reservationsystem.account.application.AccountEventProcessor;
import com.example.reservationsystem.account.domain.event.AccountDebitFailedEvent;
import com.example.reservationsystem.common.domain.model.AggregateEvent;
import com.example.reservationsystem.common.exception.KafkaExceptionHandler;
import com.example.reservationsystem.common.infra.publisher.EventPublisher;
import com.example.reservationsystem.payment.domain.event.PaymentAttemptEvent;
import com.example.reservationsystem.payment.exception.PaymentException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static com.example.reservationsystem.payment.exception.PaymentExceptionType.AMOUNT_IS_NOT_SUFFICIENT;

@Component
@Order(1)
@Slf4j
public class InsufficientAmountExceptionHandler implements KafkaExceptionHandler {

    private final EventPublisher eventPublisher;
    private final AccountEventProcessor processor;

    public InsufficientAmountExceptionHandler( @Qualifier("application") EventPublisher eventPublisher, AccountEventProcessor processor ) {
        this.eventPublisher = eventPublisher;
        this.processor = processor;
    }

    @Override
    public boolean supports( Exception e ) {
        return ( e instanceof PaymentException pe ) &&
                pe.exceptionType().equals( AMOUNT_IS_NOT_SUFFICIENT );
    }

    @Override
    @Transactional
    public void handle( Exception e, AggregateEvent event ) {
        PaymentException pe = ( PaymentException ) e;
        PaymentAttemptEvent paymentAttemptEvent = ( PaymentAttemptEvent ) event;

        log.warn("잔액 부족! accountId={}, 보유금액={}, 요청금액={}",
                pe.getAccountId(), pe.getCurrentAmount(), pe.getRequestedAmount());

        processor.markFailure( event );
        eventPublisher.publishEvent( new AccountDebitFailedEvent(
                pe.getAccountId(), paymentAttemptEvent.userId(), paymentAttemptEvent.reservationId()
        ) );
    }

}
