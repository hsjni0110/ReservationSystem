package com.example.reservationsystem.account.application;

import com.example.reservationsystem.account.domain.event.AccountDebitFailedEvent;
import com.example.reservationsystem.account.domain.event.AccountDebitedEvent;
import com.example.reservationsystem.common.domain.MessageProcessingService;
import com.example.reservationsystem.common.infra.publisher.DomainEventPublisher;
import com.example.reservationsystem.payment.domain.event.PaymentAttemptEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountEventProcessor {

    private final MessageProcessingService processor;
    private final AccountService accountService;
    private final DomainEventPublisher eventPublisher;

    /*
     성공 시에는 AccountDebitedEvent 이벤트 발행
     실패 시에는 AccountDebitFailedEvent 이벤트 발행
     실패 시의 다음 스킵 여부 전달.(반드시 재처리가 되어야 하는 경우) true면 재처리 필요 없이 롤백
     */
    public void process( PaymentAttemptEvent event, String eventIdStr ) {
        processor.process(
                eventIdStr,
                event,
                e -> {
                    Long accountId = accountService.debit( e.userId(), e.totalPrice().longValue() );
                    eventPublisher.publish( new AccountDebitedEvent( accountId, e.userId(), e.reservationId() ) );
                },
                (e, ex) -> {
                    eventPublisher.publish( new AccountDebitFailedEvent( e.userId(), e.reservationId() ) );
                },
                true
        );
    }

}
