package com.example.reservationsystem.user.point.infra.consumers;

import com.example.reservationsystem.common.application.EventOutboxService;
import com.example.reservationsystem.common.domain.model.Money;
import com.example.reservationsystem.payment.domain.event.PaymentSuccessEvent;
import com.example.reservationsystem.user.point.application.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointEventKafkaConsumer {

    private final EventOutboxService eventOutboxService;
    private final PointService pointService;

    @KafkaListener( topics = "PAYMENT_SUCCESS", groupId = "group_1" )
    public void handlePaymentSuccess( PaymentSuccessEvent event ) {
        if (eventOutboxService.checkDuplicateEvent( event )) return;

        pointService.earnPoints(
                event.userId(),
                Money.wons( event.paymentAmount() )
        );
        eventOutboxService.recordEventSuccess( event );
    }

}
