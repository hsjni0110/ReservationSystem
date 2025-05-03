package com.example.reservationsystem.user.point.application;

import com.example.reservationsystem.common.domain.MessageProcessingService;
import com.example.reservationsystem.common.domain.model.Money;
import com.example.reservationsystem.common.type.ConsumerType;
import com.example.reservationsystem.payment.domain.event.PaymentSuccessEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentSuccessProcessor {

    private final MessageProcessingService processor;
    private final PointService pointService;

    @Transactional
    public void process( PaymentSuccessEvent event, String eventIdStr ) {
        processor.process(
                eventIdStr,
                event,
                ConsumerType.EARN_POINT,
                e -> {
                    pointService.earnPoints(
                            event.userId(),
                            Money.wons( event.paymentAmount() )
                    );
                },
                (e, ex) -> {
                },
                false
        );
    }

}
