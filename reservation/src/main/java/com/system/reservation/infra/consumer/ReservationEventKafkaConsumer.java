package com.system.reservation.infra.consumer;

import com.system.domain.event.PaymentSuccessEvent;
import com.system.reservation.application.processor.PaymentSuccessEventProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationEventKafkaConsumer {

    private final PaymentSuccessEventProcessor paymentSuccessEventProcessor;

    @KafkaListener(
            topics = "PAYMENT_SUCCESS",
            groupId = "group_1",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handlePaymentSuccess(
            @Payload PaymentSuccessEvent event,
            @Header("eventId") String eventId
    ) {
        paymentSuccessEventProcessor.process( event, eventId );
    }

}
