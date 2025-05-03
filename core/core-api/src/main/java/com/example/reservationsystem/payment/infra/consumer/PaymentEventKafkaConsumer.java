package com.example.reservationsystem.payment.infra.consumer;

import com.example.reservationsystem.account.domain.event.AccountDebitFailedEvent;
import com.example.reservationsystem.account.domain.event.AccountDebitedEvent;
import com.example.reservationsystem.payment.application.processors.AccountDebitFailedEventProcessor;
import com.example.reservationsystem.payment.application.processors.AccountDebitedEventProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventKafkaConsumer {

    private final AccountDebitedEventProcessor accountDebitedEventProcessor;
    private final AccountDebitFailedEventProcessor accountDebitFailedEventProcessor;

    @KafkaListener(
            topics = "ACCOUNT_DEBITED",
            groupId = "group_1",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleAccountDebitedEvent(
            @Payload AccountDebitedEvent accountDebitedEvent,
            @Header("eventId") String eventId
    ) {
        accountDebitedEventProcessor.process( accountDebitedEvent, eventId );
    }

    @KafkaListener( topics = "ACCOUNT_DEBITED_FAILURE", groupId = "group_1" )
    public void handleAccountDebitedFailedEvent(
            @Payload AccountDebitFailedEvent event,
            @Header("eventId") String eventId
    ) {
        accountDebitFailedEventProcessor.process( event, eventId );
    }

}
