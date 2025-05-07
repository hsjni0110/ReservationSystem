package com.system.infra.consumers;

import com.system.application.AccountEventProcessor;
import com.system.domain.event.PaymentAttemptEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccountEventKafkaConsumer {

    private final AccountEventProcessor processor;

    @KafkaListener( topics = "PAYMENT_ATTEMPT", groupId = "group_1" )
    public void handlePaymentAttemptEvent(
            @Payload PaymentAttemptEvent event,
            @Header("eventId") String eventId
    ) {
        processor.process( event, eventId );
    }

}
