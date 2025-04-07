package com.example.reservationsystem.account.infra.consumers;

import com.example.reservationsystem.account.application.AccountEventProcessor;
import com.example.reservationsystem.payment.domain.event.PaymentAttemptEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountEventKafkaConsumer {

    private final AccountEventProcessor processor;

    @Async
    @KafkaListener( topics = "PAYMENT_ATTEMPT", groupId = "group_1" )
    public void handlePaymentAttemptEvent( @Payload PaymentAttemptEvent event ) {
        if (processor.isDuplicate( event )) return;

        try {
            processor.handlePaymentAttempt( event );
        } catch ( Exception e ) {
            processor.markFailure( event );
        }
    }

}
