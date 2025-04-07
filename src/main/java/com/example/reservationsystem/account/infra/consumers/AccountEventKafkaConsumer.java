package com.example.reservationsystem.account.infra.consumers;

import com.example.reservationsystem.account.application.AccountEventProcessor;
import com.example.reservationsystem.payment.event.PaymentAttemptEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccountEventKafkaConsumer {

    private final AccountEventProcessor processor;

    @Async
    @KafkaListener( id = "simple", topics = "PAYMENT_ATTEMPT" )
    public void handlePaymentAttemptEvent( @Payload PaymentAttemptEvent event ) {
        if (processor.isDuplicate( event )) return;

        try {
            processor.handlePaymentAttempt( event );
        } catch ( Exception e ) {
            processor.markFailure( event );
        }
    }

}
