package com.example.reservationsystem.reservation.application;

import com.example.reservationsystem.common.config.CacheConfig;
import com.example.reservationsystem.reservation.domain.Reservation;
import com.example.reservationsystem.reservation.domain.ScheduledSeat;
import com.example.reservationsystem.reservation.infra.repository.ScheduledSeatRepository;
import com.example.reservationsystem.reservation.application.dto.ScheduledSeatResponse;
import com.example.reservationsystem.reservation.application.dto.SeatReservationResponse;
import com.example.reservationsystem.reservation.exception.ReservationException;
import com.example.reservationsystem.vehicle.domain.model.RouteSchedule;
import com.example.reservationsystem.vehicle.infra.repository.RouteScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.reservationsystem.reservation.exception.ReservationExceptionType.*;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ScheduledSeatRepository scheduledSeatRepository;
    private final ReservationLockManager reservationLockManager;
    private final ReservationManager reservationManager;
    private final RouteScheduleRepository routeScheduleRepository;

    @Cacheable(
            cacheNames = CacheConfig.ONE_MIN_CACHE,
            key = "'route-schedule-' + #routeScheduleId",
            condition = "#routeScheduleId != null",
            sync = true
    )
    public List<ScheduledSeatResponse> getSeatsByRoute( Long routeScheduleId ) {
        RouteSchedule routeSchedule = routeScheduleRepository.findById( routeScheduleId )
                .orElseThrow(() -> new ReservationException(ROUTE_SCHEDULE_NOT_FOUND));
        List<ScheduledSeat> scheduledSeats = scheduledSeatRepository.findByRouteSchedule( routeSchedule );
        return scheduledSeats.stream()
                .map(scheduledSeat -> new ScheduledSeatResponse( scheduledSeat.getScheduledSeatId(), scheduledSeat.getSeatId(), scheduledSeat.getIsReserved()) )
                .toList();
    }

    public SeatReservationResponse preserveSeat( Long userId, Long routeScheduleId, List<Long> scheduleSeatIds ) {
        Reservation reservation = reservationLockManager.preserveWithLock(userId, routeScheduleId, scheduleSeatIds);
        return new SeatReservationResponse(
                reservation.getReservationId(),
                reservation.getScheduledSeats().stream()
                        .map(ScheduledSeat::getScheduledSeatId)
                        .toList()
        );
    }

    @Transactional
    public void cancelUnPaidReservations() {
        reservationManager.cancelReservations();
    }

    public Long confirmReservation( Long userId, Long reservationId ) {
        return reservationManager.confirmReservation( reservationId );
    }

}
