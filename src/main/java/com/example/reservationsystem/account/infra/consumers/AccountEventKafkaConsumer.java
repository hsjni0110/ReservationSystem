package com.example.reservationsystem.account.infra.consumers;

import com.example.reservationsystem.account.application.AccountEventProcessor;
import com.example.reservationsystem.account.domain.event.InsufficientAmountEvent;
import com.example.reservationsystem.common.exception.KafkaExceptionHandler;
import com.example.reservationsystem.payment.domain.event.PaymentAttemptEvent;
import com.example.reservationsystem.payment.exception.PaymentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.example.reservationsystem.payment.exception.PaymentExceptionType.AMOUNT_IS_NOT_SUFFICIENT;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccountEventKafkaConsumer {

    private final AccountEventProcessor processor;
    private final List<KafkaExceptionHandler> exceptionHandlers;

    @KafkaListener( topics = "PAYMENT_ATTEMPT", groupId = "group_1" )
    public void handlePaymentAttemptEvent( @Payload PaymentAttemptEvent event ) {
        if (processor.isDuplicate( event )) return;

        try {
            processor.handlePaymentAttempt(event);
        } catch ( Exception e ) {
            exceptionHandlers.stream()
                    .filter(handler -> handler.supports(e))
                    .findFirst()
                    .ifPresent(handler -> handler.handle(e, event));
        }
    }

}
