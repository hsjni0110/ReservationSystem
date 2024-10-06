package com.example.reservationsystem.payment.domain.repository;

import com.example.reservationsystem.payment.domain.Payment;
import com.example.reservationsystem.reservation.domain.Reservation;
import com.example.reservationsystem.user.signup.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByUserAndReservation(User user, Reservation reservation);

}
