package com.system.reservation.application;

import com.system.config.CacheConfig;
import com.system.reservation.domain.Reservation;
import com.system.reservation.domain.ScheduledSeat;
import com.system.reservation.infra.repository.ScheduledSeatRepository;
import com.system.reservation.application.dto.ScheduledSeatResponse;
import com.system.reservation.application.dto.SeatReservationResponse;
import com.system.reservation.exception.ReservationException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.system.vehicle.domain.model.RouteSchedule;
import com.system.vehicle.infra.repository.RouteScheduleRepository;

import java.util.List;

import static com.system.reservation.exception.ReservationExceptionType.*;

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
