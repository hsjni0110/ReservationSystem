package com.example.reservationsystem.reservation.domain.manager;

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
            key = "reservationUserId:#userId",
            waitTime = 5,
            releaseTime = 10
    )
    public Reservation preserveWithLock(Long userId, Long routeScheduleId, List<Long> scheduleSeatIds ) {
        return reservationManager.preserve( userId, routeScheduleId, scheduleSeatIds );
    }

}
