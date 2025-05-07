package com.system.application;

import com.system.domain.Money;
import com.system.domain.model.Payment;
import com.system.infra.repository.PaymentRepository;
import com.system.domain.event.PaymentAttemptEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.system.publisher.DomainEventPublisher;
import com.system.reservation.domain.Reservation;
import com.system.reservation.domain.ScheduledSeat;
import com.system.user.signup.domain.model.User;

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
