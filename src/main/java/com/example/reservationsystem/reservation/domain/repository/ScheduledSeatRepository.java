package com.example.reservationsystem.reservation.domain.repository;

import com.example.reservationsystem.reservation.domain.ScheduledSeat;
import com.example.reservationsystem.vehicle.domain.RouteSchedule;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ScheduledSeatRepository extends JpaRepository<ScheduledSeat, Long> {

    List<ScheduledSeat> findByRouteSchedule(RouteSchedule routeSchedule);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from ScheduledSeat s where s.seatId = :id and s.routeSchedule.routeScheduleId = :routeScheduleId")
    ScheduledSeat findByIdWithPessimisticLock( Long id, Long routeScheduleId );

    default ScheduledSeat getByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new RuntimeException("Scheduled Seat with id " + id + " not found"));
    }

}
