package com.example.reservationsystem.payment.application;

import com.example.reservationsystem.common.infra.publisher.EventPublisher;
import com.example.reservationsystem.common.type.PaymentStatus;
import com.example.reservationsystem.payment.application.dto.CompletedPaymentResponse;
import com.example.reservationsystem.payment.application.dto.PaymentStatusResponse;
import com.example.reservationsystem.payment.domain.model.Payment;
import com.example.reservationsystem.payment.infra.repository.PaymentRepository;
import com.example.reservationsystem.payment.application.dto.PaymentResponse;
import com.example.reservationsystem.payment.exception.PaymentException;
import com.example.reservationsystem.reservation.domain.Reservation;
import com.example.reservationsystem.reservation.infra.repository.ReservationRepository;
import com.example.reservationsystem.user.signup.domain.model.User;
import com.example.reservationsystem.user.signup.infra.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.reservationsystem.payment.exception.PaymentExceptionType.NOT_PAYABLE;
import static com.example.reservationsystem.payment.exception.PaymentExceptionType.USER_NOT_MATCHED;

@Service
public class PaymentService {

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final PaymentManager paymentManager;
    private final PaymentRepository paymentRepository;
    private final EventPublisher eventPublisher;

    public PaymentService( UserRepository userRepository, ReservationRepository reservationRepository, PaymentManager paymentManager, PaymentRepository paymentRepository, @Qualifier("application") EventPublisher eventPublisher ) {
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
        this.paymentManager = paymentManager;
        this.paymentRepository = paymentRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public PaymentResponse pay( Long userId, Long reservationId ) {
        User user = userRepository.getByIdOrThrow( userId );
        Reservation reservation = reservationRepository.getByIdOrThrow( reservationId );
        validate( user, reservation );
        Payment payment = paymentManager.executePayment( user, reservation );
        return new PaymentResponse( payment.getPaymentId(), payment.getTotalPrice().getAmount(), payment.getPaymentStatus(), payment.getCreatedAt() );
    }

    public CompletedPaymentResponse successPayment(Long userId, Long reservationId ) {
        User user = userRepository.getByIdOrThrow( userId );
        Reservation reservation = reservationRepository.getByIdOrThrow( reservationId );
        Payment payment = paymentRepository.findByUserAndReservation(user, reservation)
                .orElseThrow(() -> new PaymentException(USER_NOT_MATCHED));
        payment.completePayment();
        return new CompletedPaymentResponse( payment.getPaymentId(), payment.getTotalPrice().getAmount().longValue() );
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

    public void cancelPayment( Long userId, Long reservationId ) {
        User user = userRepository.getByIdOrThrow( userId );
        Reservation reservation = reservationRepository.getByIdOrThrow( reservationId );
        Payment payment = paymentRepository.findByUserAndReservation(user, reservation)
                .orElseThrow(() -> new PaymentException( USER_NOT_MATCHED ));
        payment.cancelPayment();
        paymentRepository.save( payment );
    }

    public PaymentStatusResponse getPaymentStatus(Long userId, Long reservationId ) {
        User user = userRepository.getByIdOrThrow( userId );
        Reservation reservation = reservationRepository.getByIdOrThrow( reservationId );
        Payment payment = paymentRepository.findByUserAndReservation( user, reservation )
                .orElseThrow(() -> new PaymentException( USER_NOT_MATCHED ));
        return new PaymentStatusResponse( payment.getPaymentStatus() );
    }

}
