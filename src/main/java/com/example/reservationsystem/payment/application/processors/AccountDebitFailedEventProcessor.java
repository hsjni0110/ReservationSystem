package com.example.reservationsystem.payment.application.processors;

import com.example.reservationsystem.account.domain.event.AccountDebitFailedEvent;
import com.example.reservationsystem.common.domain.MessageProcessingService;
import com.example.reservationsystem.common.type.ConsumerType;
import com.example.reservationsystem.payment.application.PaymentService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

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