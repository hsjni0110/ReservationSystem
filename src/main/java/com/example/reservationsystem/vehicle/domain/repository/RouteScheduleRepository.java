package com.example.reservationsystem.vehicle.domain.repository;

import com.example.reservationsystem.vehicle.domain.RouteSchedule;
import com.example.reservationsystem.vehicle.domain.RouteTimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteScheduleRepository extends JpaRepository<RouteSchedule, Long> {

    List<RouteSchedule> findByRouteTimeSlotIn(List<RouteTimeSlot> routeTimeSlots);

}