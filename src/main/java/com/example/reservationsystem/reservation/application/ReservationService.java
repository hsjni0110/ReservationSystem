package com.example.reservationsystem.reservation.application;

import com.example.reservationsystem.reservation.domain.Reservation;
import com.example.reservationsystem.reservation.domain.ScheduledSeat;
import com.example.reservationsystem.reservation.domain.manager.ReservationManager;
import com.example.reservationsystem.reservation.domain.repository.ScheduledSeatRepository;
import com.example.reservationsystem.reservation.dto.ScheduledSeatResponse;
import com.example.reservationsystem.reservation.dto.SeatReservationResponse;
import com.example.reservationsystem.reservation.exception.ReservationException;
import com.example.reservationsystem.vehicle.domain.Route;
import com.example.reservationsystem.vehicle.domain.RouteSchedule;
import com.example.reservationsystem.vehicle.domain.RouteTimeSlot;
import com.example.reservationsystem.vehicle.domain.repository.RouteRepository;
import com.example.reservationsystem.vehicle.domain.repository.RouteScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.example.reservationsystem.reservation.exception.ReservationExceptionType.*;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final RouteRepository routeRepository;
    private final ScheduledSeatRepository scheduledSeatRepository;
    private final RouteScheduleRepository routeScheduleRepository;
    private final ReservationManager reservationManager;

    public List<ScheduledSeatResponse> getSeatsByRoute(String departure, String arrival, LocalDate specificDate, String timeSlot) {
        Route route = routeRepository.findByDepartureAndArrivalAndScheduleDate(departure, arrival, specificDate).orElseThrow(() -> new ReservationException(ROUTE_NOT_FOUND));
        RouteTimeSlot matchedRouteTimeSlot = route.getMatchedRouteTimeSlot(timeSlot);
        RouteSchedule routeSchedule = routeScheduleRepository.findByRouteTimeSlot(matchedRouteTimeSlot).orElseThrow(() -> new ReservationException(ROUTE_SCHEDULE_NOT_FOUND));
        List<ScheduledSeat> scheduledSeats = scheduledSeatRepository.findByRouteSchedule(routeSchedule);
        return scheduledSeats.stream()
                .map(scheduledSeat -> new ScheduledSeatResponse(scheduledSeat.getScheduledSeatId(), scheduledSeat.getSeatId(), scheduledSeat.getIsReserved()))
                .toList();
    }

    @Transactional
    public SeatReservationResponse preserveSeat(Long userId, Long routeScheduleId, List<Long> scheduleSeatIds) {
        validate(routeScheduleId, scheduleSeatIds);
        Reservation reservation = reservationManager.preserve(userId, scheduleSeatIds);
        return new SeatReservationResponse(
                reservation.getReservationId(),
                reservation.getScheduledSeats().stream()
                        .map(ScheduledSeat::getScheduledSeatId)
                        .toList());
    }

    private void validate(Long routeScheduleId, List<Long> scheduleSeatId) {
        validateRouteScheduleExist(routeScheduleId);
        validateScheduledSeatIsReserved(scheduleSeatId);
        validateReserveTime(routeScheduleId);
    }

    private void validateReserveTime(Long routeScheduleId) {
        // 출발 시간 15분 이전인지 확인
    }

    private void validateRouteScheduleExist(Long routeScheduleId) {
        routeScheduleRepository.findById(routeScheduleId).orElseThrow(() -> new ReservationException(ROUTE_SCHEDULE_NOT_FOUND));
    }

    private void validateScheduledSeatIsReserved(List<Long> scheduleSeatIds) {
        scheduleSeatIds.forEach( scheduleSeatId -> {
                ScheduledSeat scheduledSeat = scheduledSeatRepository.findById(scheduleSeatId).orElseThrow(() -> new ReservationException(SCHEDULED_SEAT_NOT_FOUND));
                scheduledSeat.isReserved();
            }
        );
    }

}
