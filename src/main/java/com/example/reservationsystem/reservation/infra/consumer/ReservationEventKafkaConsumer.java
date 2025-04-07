package com.example.reservationsystem.reservation.infra.consumer;

import com.example.reservationsystem.payment.domain.event.PaymentSuccessEvent;
import com.example.reservationsystem.reservation.application.ReservationEventProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationEventKafkaConsumer {

    private final ReservationEventProcessor reservationEventProcessor;

    @KafkaListener( topics = "PAYMENT_SUCCESS", groupId = "group_1" )
    public void handlePaymentSuccess( PaymentSuccessEvent event ) {
        try {
            reservationEventProcessor.handlePaymentSuccess( event );
        } catch ( Exception e ) {
            log.error( "❌ [Kafka] PAYMENT_SUCCESS 처리 중 오류 발생. event={}", event, e );
            reservationEventProcessor.markFailure( event );
        }
    }

}
