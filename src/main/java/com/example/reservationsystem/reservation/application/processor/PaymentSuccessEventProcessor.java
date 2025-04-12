package com.example.reservationsystem.reservation.application.processor;

import com.example.reservationsystem.common.domain.MessageProcessingService;
import com.example.reservationsystem.payment.domain.event.PaymentSuccessEvent;
import com.example.reservationsystem.reservation.application.ReservationService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentSuccessEventProcessor {

    private final MessageProcessingService processor;
    private final ReservationService reservationService;

    public void process( PaymentSuccessEvent event, String eventIdStr ) {
        processor.process(
                eventIdStr,
                event,
                e -> {
                    reservationService.confirmReservation(e.userId(), e.reservationId());
                },
                (e, ex) -> {
                },
                false
        );
    }

}
