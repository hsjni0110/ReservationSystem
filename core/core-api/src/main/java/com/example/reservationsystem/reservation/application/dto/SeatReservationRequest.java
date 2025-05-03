package com.example.reservationsystem.reservation.application.dto;

import java.util.List;

public record SeatReservationRequest(
        Long routeScheduleId,
        List<Long> scheduleSeatIds
) {
}
