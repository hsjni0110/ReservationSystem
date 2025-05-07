package com.system.reservation.application;

import com.system.annotation.DistributedSimpleLock;
import com.system.reservation.domain.Reservation;
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
