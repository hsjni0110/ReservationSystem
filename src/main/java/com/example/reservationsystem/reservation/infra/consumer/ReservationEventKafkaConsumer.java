package com.example.reservationsystem.reservation.infra.consumer;

import com.example.reservationsystem.payment.domain.event.PaymentSuccessEvent;
import com.example.reservationsystem.reservation.application.ReservationEventProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationEventKafkaConsumer {

    private final ReservationEventProcessor reservationEventProcessor;

    @Async
    @KafkaListener( topics = "PAYMENT_SUCCESS", groupId = "group_1" )
    public void handlePaymentSuccess( PaymentSuccessEvent event ) {
        try {
            reservationEventProcessor.handlePaymentSuccess( event );
        } catch ( Exception e ) {
            reservationEventProcessor.markFailure( event );
        }
    }

}
