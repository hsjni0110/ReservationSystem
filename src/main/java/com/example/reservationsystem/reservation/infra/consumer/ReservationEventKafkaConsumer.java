package com.example.reservationsystem.reservation.infra.consumer;

import com.example.reservationsystem.payment.domain.event.PaymentSuccessEvent;
import com.example.reservationsystem.reservation.application.processor.PaymentSuccessEventProcessor;
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

    @KafkaListener( topics = "PAYMENT_SUCCESS", groupId = "group_1" )
    public void handlePaymentSuccess(
            @Payload PaymentSuccessEvent event,
            @Header("eventId") String eventId
    ) {
        paymentSuccessEventProcessor.process( event, eventId );
    }

}
