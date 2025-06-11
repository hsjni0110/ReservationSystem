package com.system.application;

import com.system.application.dto.CompletedPaymentResponse;
import com.system.application.dto.PaymentStatusResponse;
import com.system.domain.model.Payment;
import com.system.infra.repository.PaymentRepository;
import com.system.application.dto.PaymentResponse;
import com.system.exception.PaymentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.system.reservation.domain.Reservation;
import com.system.reservation.exception.ReservationException;
import com.system.reservation.infra.repository.ReservationRepository;
import com.system.user.signup.domain.model.User;
import com.system.user.signup.infra.repository.UserRepository;

import static com.system.exception.PaymentExceptionType.NOT_PAYABLE;
import static com.system.reservation.exception.ReservationExceptionType.USER_NOT_MATCHED;
import static com.system.type.PaymentStatus.PAYED;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final PaymentManager paymentManager;
    private final PaymentRepository paymentRepository;

    @Transactional
    public PaymentResponse pay( Long userId, Long reservationId ) {
        User user = userRepository.getByIdOrThrow( userId );
        Reservation reservation = reservationRepository.getByIdOrThrow( reservationId );
        validate( user, reservation );
        Payment payment = paymentManager.executePayment( user, reservation );
        return new PaymentResponse( payment.getPaymentId(), payment.getTotalPrice().getMoney(), payment.getPaymentStatus(), payment.getCreatedAt() );
    }

    public CompletedPaymentResponse successPayment(Long userId, Long reservationId ) {
        User user = userRepository.getByIdOrThrow( userId );
        Reservation reservation = reservationRepository.getByIdOrThrow( reservationId );
        Payment payment = paymentRepository.findByUserAndReservation(user, reservation)
                .orElseThrow(() -> new ReservationException( USER_NOT_MATCHED ));
        payment.completePayment();
        return new CompletedPaymentResponse( payment.getPaymentId(), payment.getTotalPrice().getMoney().longValue() );
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
                    if ( payment.getPaymentStatus().equals( PAYED ) ) {
                        throw new PaymentException( NOT_PAYABLE );
                    }
                }));
    }

    @Transactional
    public void cancelPayment( Long userId, Long reservationId ) {
        User user = userRepository.getByIdOrThrow( userId );
        Reservation reservation = reservationRepository.getByIdOrThrow( reservationId );
        Payment payment = paymentRepository.findByUserAndReservation(user, reservation)
                .orElseThrow(() -> new ReservationException( USER_NOT_MATCHED ));
        payment.cancelPayment();
        paymentRepository.save( payment );
    }

    public PaymentStatusResponse getPaymentStatus(Long userId, Long reservationId ) {
        User user = userRepository.getByIdOrThrow( userId );
        Reservation reservation = reservationRepository.getByIdOrThrow( reservationId );
        Payment payment = paymentRepository.findByUserAndReservation( user, reservation )
                .orElseThrow(() -> new ReservationException( USER_NOT_MATCHED ));
        return new PaymentStatusResponse( payment.getPaymentStatus() );
    }

}
