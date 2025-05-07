package com.system.reservation.application.scheduler;

import com.system.reservation.application.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReservationScheduler {

    private final ReservationService reservationService;

    public ReservationScheduler(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Scheduled(fixedRate = 60000)
    public void cancelExpiredReservations() {
        log.info("Cancel Expired Reservation");
        reservationService.cancelUnPaidReservations();
    }

}
