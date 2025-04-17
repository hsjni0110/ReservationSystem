package com.example.reservationsystem.reservation.infra.repository;

import com.example.reservationsystem.reservation.domain.ScheduledSeat;
import com.example.reservationsystem.vehicle.domain.model.RouteSchedule;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScheduledSeatRepository extends JpaRepository<ScheduledSeat, Long> {

    List<ScheduledSeat> findByRouteSchedule(RouteSchedule routeSchedule);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from ScheduledSeat s where s.seatId in :seatIds and s.routeSchedule.routeScheduleId = :routeScheduleId")
    List<ScheduledSeat> findAllByIdsWithPessimisticLock(@Param("seatIds") List<Long> seatIds, @Param("routeScheduleId") Long routeScheduleId);

    default ScheduledSeat getByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new RuntimeException("Scheduled Seat with id " + id + " not found"));
    }

}
