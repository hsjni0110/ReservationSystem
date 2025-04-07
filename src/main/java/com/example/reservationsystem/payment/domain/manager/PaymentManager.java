package com.example.reservationsystem.payment.domain.manager;

import com.example.reservationsystem.account.domain.Account;
import com.example.reservationsystem.common.domain.Money;
import com.example.reservationsystem.account.exception.AccountException;
import com.example.reservationsystem.infrastructure.publisher.EventPublisher;
import com.example.reservationsystem.payment.domain.Payment;
import com.example.reservationsystem.account.domain.repository.AccountRepository;
import com.example.reservationsystem.payment.domain.repository.PaymentRepository;
import com.example.reservationsystem.payment.event.PaymentAttemptEvent;
import com.example.reservationsystem.reservation.domain.Reservation;
import com.example.reservationsystem.reservation.domain.ScheduledSeat;
import com.example.reservationsystem.user.signup.domain.User;
import jdk.jfr.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static com.example.reservationsystem.account.exception.AccountExceptionType.ACCOUNT_NOT_FOUND;

@Component
public class PaymentManager {

    private final AccountRepository accountRepository;
    private final PaymentRepository paymentRepository;
    private final EventPublisher eventPublisher;

    public PaymentManager(AccountRepository accountRepository, PaymentRepository paymentRepository, @Qualifier("application") EventPublisher eventPublisher) {
        this.accountRepository = accountRepository;
        this.paymentRepository = paymentRepository;
        this.eventPublisher = eventPublisher;
    }

    public Payment executePayment(User user, Reservation reservation ) {
        try {
            Account account = accountRepository.findByUserForUpdate( user ).orElseThrow(() -> new AccountException( ACCOUNT_NOT_FOUND ));
            Money totalPrice = reservation.getScheduledSeats()
                    .stream()
                    .map( ScheduledSeat::getSeatPrice )
                    .reduce( Money.ZERO, Money::add );

            Payment payment = Payment.successFrom(user, reservation, totalPrice);
            Payment saved = paymentRepository.save(payment);

            eventPublisher.publishEvent( new PaymentAttemptEvent( saved.getPaymentId(), user.getUserId(), totalPrice.getAmount() ) );
            return payment;
        } catch (Exception e) {
            // 실패
            throw new RuntimeException(e);
        }
    }

}
