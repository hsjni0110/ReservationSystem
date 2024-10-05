package com.example.reservationsystem.reservation.dto;

import java.util.List;

public record SeatReservationResponse(
        Long reservationId,
        List<Long> scheduledSeatIds
) {
}
