package com.example.reservationsystem.reservation.application;

import com.example.reservationsystem.reservation.domain.ScheduledSeat;
import com.example.reservationsystem.reservation.domain.repository.ScheduledSeatRepository;
import com.example.reservationsystem.reservation.dto.ScheduledSeatResponse;
import com.example.reservationsystem.reservation.exception.ReservationException;
import com.example.reservationsystem.vehicle.domain.Route;
import com.example.reservationsystem.vehicle.domain.RouteSchedule;
import com.example.reservationsystem.vehicle.domain.RouteTimeSlot;
import com.example.reservationsystem.vehicle.domain.repository.RouteRepository;
import com.example.reservationsystem.vehicle.domain.repository.RouteScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.example.reservationsystem.reservation.exception.ReservationExceptionType.ROUTE_NOT_FOUND;
import static com.example.reservationsystem.reservation.exception.ReservationExceptionType.ROUTE_SCHEDULE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final RouteRepository routeRepository;
    private final ScheduledSeatRepository scheduledSeatRepository;
    private final RouteScheduleRepository routeScheduleRepository;

    public List<ScheduledSeatResponse> getSeatsByRoute(String departure, String arrival, LocalDate specificDate, String timeSlot) {
        Route route = routeRepository.findByDepartureAndArrivalAAndScheduleDate(departure, arrival, specificDate).orElseThrow(() -> new ReservationException(ROUTE_NOT_FOUND));
        RouteTimeSlot matchedRouteTimeSlot = route.getMatchedRouteTimeSlot(timeSlot);
        RouteSchedule routeSchedule = routeScheduleRepository.findByRouteTimeSlot(matchedRouteTimeSlot).orElseThrow(() -> new ReservationException(ROUTE_SCHEDULE_NOT_FOUND));
        List<ScheduledSeat> scheduledSeats = scheduledSeatRepository.findByRouteSchedule(routeSchedule);
        return scheduledSeats.stream()
                .map(scheduledSeat -> new ScheduledSeatResponse(scheduledSeat.getScheduledSeatId(), scheduledSeat.getSeatId(), scheduledSeat.getIsReserved()))
                .toList();
    }

}
