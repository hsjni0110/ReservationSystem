package com.example.reservationsystem.payment.infra.consumer;

import com.example.reservationsystem.account.domain.event.AccountDebitFailedEvent;
import com.example.reservationsystem.account.domain.event.AccountDebitedEvent;
import com.example.reservationsystem.payment.application.PaymentEventProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventKafkaConsumer {

    private final PaymentEventProcessor paymentEventProcessor;

    @KafkaListener( topics = "ACCOUNT_DEBITED", groupId = "group_1" )
    public void handleAccountDebitedEvent( AccountDebitedEvent accountDebitedEvent ) {
        // 멱등성이 지켜지므로 중복 검사 x
        try {
            paymentEventProcessor.handleAccountDebited( accountDebitedEvent );
        } catch ( Exception e ) {
            log.error( "❌ [Kafka] ACCOUNT_DEBITED 처리 중 오류 발생. event={}", accountDebitedEvent, e );
            paymentEventProcessor.markFailure( accountDebitedEvent );
        }
    }

    @KafkaListener( topics = "ACCOUNT_DEBITED_FAILURE", groupId = "group_1" )
    public void handleInsufficientBalance( AccountDebitFailedEvent event ) {
        try {
            paymentEventProcessor.handleInsufficientAmount( event );
        } catch ( Exception e ) {
            log.error( "❌ [Kafka] ACCOUNT_DEBITED_FAILURE 처리 중 오류 발생. event={}", event, e );
            paymentEventProcessor.markFailure( event );
        }
    }

}
