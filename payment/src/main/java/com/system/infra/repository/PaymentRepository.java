package com.system.infra.repository;

import com.system.domain.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import com.system.reservation.domain.Reservation;
import com.system.user.signup.domain.model.User;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByUserAndReservation( User user, Reservation reservation);

}
