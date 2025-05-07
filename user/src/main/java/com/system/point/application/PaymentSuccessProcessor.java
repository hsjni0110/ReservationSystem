package com.system.point.application;

import com.system.domain.MessageProcessingService;
import com.system.domain.Money;
import com.system.domain.event.PaymentSuccessEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.system.type.ConsumerType;

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
