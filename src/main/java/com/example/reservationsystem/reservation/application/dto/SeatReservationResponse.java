package com.example.reservationsystem.reservation.application.dto;

import java.util.List;

public record SeatReservationResponse(
        Long reservationId,
        List<Long> scheduledSeatIds
) {
}
