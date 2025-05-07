package com.system.application.processors;

import com.system.application.PaymentService;
import com.system.domain.MessageProcessingService;
import com.system.domain.event.AccountDebitFailedEvent;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.system.type.ConsumerType;

@Service
@RequiredArgsConstructor
public class AccountDebitFailedEventProcessor {

    private final MessageProcessingService processor;
    private final PaymentService paymentService;

    @Transactional
    public void process( AccountDebitFailedEvent event, String eventIdStr ) {
        processor.process(
                eventIdStr,
                event,
                ConsumerType.CANCEL_PAYMENT,
                e -> {
                    paymentService.cancelPayment(e.userId(), e.reservationId());
                },
                (e, ex) -> {
                },
                true
        );
    }
}