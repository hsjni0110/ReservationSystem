package com.example.reservationsystem.payment.application;

import com.example.reservationsystem.common.type.PaymentStatus;
import com.example.reservationsystem.payment.domain.model.Payment;
import com.example.reservationsystem.payment.infra.repository.PaymentRepository;
import com.example.reservationsystem.payment.application.dto.PaymentResponse;
import com.example.reservationsystem.payment.exception.PaymentException;
import com.example.reservationsystem.reservation.domain.Reservation;
import com.example.reservationsystem.reservation.infra.repository.ReservationRepository;
import com.example.reservationsystem.user.point.application.PointService;
import com.example.reservationsystem.user.signup.domain.model.User;
import com.example.reservationsystem.user.signup.infra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.example.reservationsystem.payment.exception.PaymentExceptionType.NOT_PAYABLE;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final PaymentManager paymentManager;
    private final PaymentRepository paymentRepository;
    private final PointService pointService;

    @Transactional
    public PaymentResponse pay( Long userId, Long reservationId ) {
        User user = userRepository.getByIdOrThrow( userId );
        Reservation reservation = reservationRepository.getByIdOrThrow( reservationId );
        validate( user, reservation );
        Payment payment = paymentManager.executePayment( user, reservation );
        reservation.successPayment();
        pointService.earnPoints( userId, payment.getTotalPrice(), UUID.randomUUID().toString() );
        return new PaymentResponse( payment.getPaymentId(), payment.getTotalPrice().getAmount(), payment.getPaymentStatus(), payment.getCreatedAt() );
    }

    private void validate( User user, Reservation reservation ) {
        validateUser( user, reservation );
        validateReservationStatus( reservation );
        validatePaymentStatus( user, reservation );
    }

    private void validateUser( User user, Reservation reservation ) {
        reservation.authorizeUser( user );
    }

    private void validateReservationStatus( Reservation reservation ) {
        if ( !reservation.isPayable() ) {
            throw new PaymentException( NOT_PAYABLE );
        }
    }

    private void validatePaymentStatus( User user, Reservation reservation ) {
        paymentRepository.findByUserAndReservation( user, reservation )
                .ifPresent((payment -> {
                    if ( payment.getPaymentStatus().equals( PaymentStatus.PAYED ) ) {
                        throw new PaymentException( NOT_PAYABLE );
                    }
                }));
    }

}
