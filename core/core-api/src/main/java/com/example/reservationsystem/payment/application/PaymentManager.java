package com.example.reservationsystem.payment.application;

import com.example.reservationsystem.common.domain.model.Money;
import com.example.reservationsystem.common.infra.publisher.DomainEventPublisher;
import com.example.reservationsystem.payment.domain.model.Payment;
import com.example.reservationsystem.payment.infra.repository.PaymentRepository;
import com.example.reservationsystem.payment.domain.event.PaymentAttemptEvent;
import com.example.reservationsystem.reservation.domain.Reservation;
import com.example.reservationsystem.reservation.domain.ScheduledSeat;
import com.example.reservationsystem.user.signup.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentManager {

    private final PaymentRepository paymentRepository;
    private final DomainEventPublisher eventPublisher;

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

            eventPublisher.publish( new PaymentAttemptEvent( saved.getPaymentId(), user.getUserId(), totalPrice.getAmount(), reservation.getReservationId() ) );
            return payment;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
