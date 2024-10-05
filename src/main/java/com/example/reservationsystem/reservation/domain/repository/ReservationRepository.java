package com.example.reservationsystem.reservation.domain.repository;

import com.example.reservationsystem.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
