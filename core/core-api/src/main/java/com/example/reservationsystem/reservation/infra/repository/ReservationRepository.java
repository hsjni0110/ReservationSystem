package com.example.reservationsystem.reservation.infra.repository;

import com.example.reservationsystem.reservation.domain.Reservation;
import com.example.reservationsystem.reservation.exception.ReservationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.reservationsystem.reservation.exception.ReservationExceptionType.RESERVATION_NOT_FOUND;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    default Reservation getByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new ReservationException(RESERVATION_NOT_FOUND));
    }

    @Query("select r from Reservation r where r.status = :reservationStatus and r.createdAt < :expirationTime")
    List<Reservation> findExpiredReservation(Reservation.ReservationStatus reservationStatus, LocalDateTime expirationTime);

}
