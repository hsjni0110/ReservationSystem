package com.example.reservationsystem.user.point.infra.consumers;

import com.example.reservationsystem.payment.domain.event.PaymentSuccessEvent;
import com.example.reservationsystem.user.point.application.PaymentSuccessProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PointEventKafkaConsumer {

    private final PaymentSuccessProcessor paymentSuccessProcessor;

    @KafkaListener(
            topics = "PAYMENT_SUCCESS",
            groupId = "point_group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handlePaymentSuccess(
            @Payload PaymentSuccessEvent event,
            @Header("eventId") String eventId
    ) {
        log.info("Point 적립!");
        paymentSuccessProcessor.process( event, eventId );
    }

}
