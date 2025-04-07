package com.example.reservationsystem.payment.infra.consumer;

import com.example.reservationsystem.account.domain.event.AccountDebitedEvent;
import com.example.reservationsystem.payment.application.PaymentEventProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventKafkaConsumer {

    private final PaymentEventProcessor paymentEventProcessor;

    @Async
    @KafkaListener( id = "simple", topics = "ACCOUNT_DEBITED" )
    public void handleAccountDebitedEvent( AccountDebitedEvent accountDebitedEvent ) {
        // 멱등성이 지켜지므로 중복 검사 x
        try {
            paymentEventProcessor.handleAccountDebited( accountDebitedEvent );
        } catch ( Exception e ) {
            paymentEventProcessor.markFailure( accountDebitedEvent);
        }
    }

}
