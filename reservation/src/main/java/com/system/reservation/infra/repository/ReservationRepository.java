package com.system.reservation.infra.repository;

import com.system.reservation.domain.Reservation;
import com.system.reservation.exception.ReservationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

import static com.system.reservation.exception.ReservationExceptionType.RESERVATION_NOT_FOUND;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    default Reservation getByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new ReservationException(RESERVATION_NOT_FOUND));
    }

    @Query("select r from Reservation r where r.status = :reservationStatus and r.createdAt < :expirationTime")
    List<Reservation> findExpiredReservation(Reservation.ReservationStatus reservationStatus, LocalDateTime expirationTime);

}
