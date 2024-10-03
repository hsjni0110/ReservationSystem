package com.example.reservationsystem.reservation.dto;

public record ScheduledSeatResponse(
        Long scheduledSeatId,
        Integer seatId,
        boolean isReserved
) {
}
