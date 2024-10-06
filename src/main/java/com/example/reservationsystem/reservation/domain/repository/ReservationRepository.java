package com.example.reservationsystem.reservation.domain.repository;

import com.example.reservationsystem.reservation.domain.Reservation;
import com.example.reservationsystem.reservation.exception.ReservationException;
import org.springframework.data.jpa.repository.JpaRepository;

import static com.example.reservationsystem.reservation.exception.ReservationExceptionType.RESERVATION_NOT_FOUND;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    default Reservation getByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new ReservationException(RESERVATION_NOT_FOUND));
    }

}
