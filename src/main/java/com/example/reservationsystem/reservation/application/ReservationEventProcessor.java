package com.example.reservationsystem.reservation.application;

import com.example.reservationsystem.common.application.EventOutboxService;
import com.example.reservationsystem.payment.domain.event.PaymentSuccessEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationEventProcessor {

    private final ReservationService reservationService;
    private final EventOutboxService eventOutboxService;

    public ReservationEventProcessor(ReservationService reservationService, EventOutboxService eventOutboxService) {
        this.reservationService = reservationService;
        this.eventOutboxService = eventOutboxService;
    }

    @Transactional
    public void handlePaymentSuccess( PaymentSuccessEvent event ) {
        Long reservationId = reservationService.confirmReservation(event.userId(), event.reservationId());
        eventOutboxService.recordEventSuccess( event );
    }

    public void markFailure( PaymentSuccessEvent event ) {
        eventOutboxService.recordEventFailure( event );
    }

}
