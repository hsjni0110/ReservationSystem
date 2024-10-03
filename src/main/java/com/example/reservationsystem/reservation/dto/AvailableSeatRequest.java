package com.example.reservationsystem.reservation.dto;

import java.time.LocalDate;

public record AvailableSeatRequest(
        LocalDate specificDate,
        String departure,
        String arrival,
        String timeSlot
) {
}
