package com.example.reservationsystem.payment.domain.manager;

import com.example.reservationsystem.account.domain.Account;
import com.example.reservationsystem.account.domain.Money;
import com.example.reservationsystem.account.exception.AccountException;
import com.example.reservationsystem.common.type.PaymentStatus;
import com.example.reservationsystem.payment.domain.Payment;
import com.example.reservationsystem.account.domain.repository.AccountRepository;
import com.example.reservationsystem.payment.domain.repository.PaymentRepository;
import com.example.reservationsystem.reservation.domain.Reservation;
import com.example.reservationsystem.reservation.domain.ScheduledSeat;
import com.example.reservationsystem.user.signup.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.example.reservationsystem.account.exception.AccountExceptionType.ACCOUNT_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class PaymentManager {

    private final AccountRepository accountRepository;
    private final PaymentRepository paymentRepository;

    public Payment executePayment( User user, Reservation reservation ) {
        try {
            Account account = accountRepository.findByUserForUpdate( user ).orElseThrow(() -> new AccountException( ACCOUNT_NOT_FOUND ));
            Money totalPrice = reservation.getScheduledSeats()
                    .stream()
                    .map( ScheduledSeat::getSeatPrice )
                    .reduce( Money.ZERO, Money::add );

            account.pay( totalPrice );

            Payment payment = Payment.successFrom(user, reservation, totalPrice);
            return paymentRepository.save(payment);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
