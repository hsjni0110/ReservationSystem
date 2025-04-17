package com.example.reservationsystem.reservation.application;

import com.example.reservationsystem.common.annotation.DistributedSimpleLock;
import com.example.reservationsystem.reservation.domain.Reservation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReservationLockManager {

    private final ReservationManager reservationManager;

    public ReservationLockManager(ReservationManager reservationManager) {
        this.reservationManager = reservationManager;
    }

    @DistributedSimpleLock(
            key = "'reservation:' + #routeScheduleId + ':[' + #scheduleSeatIds + ']'",
            waitTime = 1,
            releaseTime = 5
    )
    public Reservation preserveWithLock(Long userId, Long routeScheduleId, List<Long> scheduleSeatIds ) {
        return reservationManager.preserve( userId, routeScheduleId, scheduleSeatIds );
    }

}
