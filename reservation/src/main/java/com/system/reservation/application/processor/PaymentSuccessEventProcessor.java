package com.system.reservation.application.processor;

import com.system.domain.event.PaymentSuccessEvent;
import com.system.reservation.application.ReservationService;
import com.system.domain.MessageProcessingService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.system.type.ConsumerType;

@Service
@RequiredArgsConstructor
public class PaymentSuccessEventProcessor {

    private final MessageProcessingService processor;
    private final ReservationService reservationService;

    @Transactional
    public void process( PaymentSuccessEvent event, String eventIdStr ) {
        processor.process(
                eventIdStr,
                event,
                ConsumerType.CONFIRM_RESERVATION,
                e -> {
                    reservationService.confirmReservation(e.userId(), e.reservationId());
                },
                (e, ex) -> {
                },
                false
        );
    }

}
