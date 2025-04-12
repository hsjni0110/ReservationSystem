package com.example.reservationsystem.payment.application.processors;

import com.example.reservationsystem.account.domain.event.AccountDebitFailedEvent;
import com.example.reservationsystem.common.domain.MessageProcessingService;
import com.example.reservationsystem.payment.application.PaymentService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountDebitFailedEventProcessor {

    private final MessageProcessingService processor;
    private final PaymentService paymentService;

    public void process(AccountDebitFailedEvent event, String eventIdStr) {
        processor.process(
                eventIdStr,
                event,
                e -> {
                    paymentService.cancelPayment(e.userId(), e.reservationId());
                },
                (e, ex) -> {
                },
                true
        );
    }
}