package com.example.reservationsystem.infrastructure.consumer;

import com.example.reservationsystem.common.domain.EventOutboxService;
import com.example.reservationsystem.payment.event.PaymentAttemptEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventKafkaConsumer {

    private final EventOutboxService eventOutboxService;

    @Async
    @KafkaListener( id = "simple", topics = "PAYMENT_ATTEMPT" )
    public void handlePaymentAttemptEvent( @Payload PaymentAttemptEvent event ) {
        eventOutboxService.recordEvent( event );

    }

}
