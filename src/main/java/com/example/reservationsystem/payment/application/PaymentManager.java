package com.example.reservationsystem.payment.application;

import com.example.reservationsystem.account.domain.model.Account;
import com.example.reservationsystem.common.domain.model.Money;
import com.example.reservationsystem.account.exception.AccountException;
import com.example.reservationsystem.common.infra.publisher.EventPublisher;
import com.example.reservationsystem.payment.domain.model.Payment;
import com.example.reservationsystem.account.infra.repository.AccountRepository;
import com.example.reservationsystem.payment.infra.repository.PaymentRepository;
import com.example.reservationsystem.payment.domain.event.PaymentAttemptEvent;
import com.example.reservationsystem.reservation.domain.Reservation;
import com.example.reservationsystem.reservation.domain.ScheduledSeat;
import com.example.reservationsystem.user.signup.domain.model.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static com.example.reservationsystem.account.exception.AccountExceptionType.ACCOUNT_NOT_FOUND;

@Component
public class PaymentManager {

    private final PaymentRepository paymentRepository;
    private final EventPublisher eventPublisher;

    public PaymentManager( PaymentRepository paymentRepository, @Qualifier("application") EventPublisher eventPublisher ) {
        this.paymentRepository = paymentRepository;
        this.eventPublisher = eventPublisher;
    }

    public Payment executePayment( User user, Reservation reservation ) {
        try {
            Money totalPrice = reservation.getScheduledSeats()
                    .stream()
                    .map( ScheduledSeat::getSeatPrice )
                    .reduce( Money.ZERO, Money::add );

            Payment payment = paymentRepository.findByUserAndReservation(user, reservation)
                    .orElseGet(() -> Payment.notPaidPayment( user, reservation, totalPrice ));
            payment.attemptPayment();
            Payment saved = paymentRepository.save(payment);

            eventPublisher.publishEvent( new PaymentAttemptEvent( saved.getPaymentId(), user.getUserId(), totalPrice.getAmount(), reservation.getReservationId() ) );
            return payment;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
