package com.example.reservationsystem.reservation.domain.repository;

import com.example.reservationsystem.reservation.domain.ScheduledSeat;
import com.example.reservationsystem.vehicle.domain.RouteSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduledSeatRepository extends JpaRepository<ScheduledSeat, Long> {


    List<ScheduledSeat> findByRouteSchedule(RouteSchedule routeSchedule);

}
