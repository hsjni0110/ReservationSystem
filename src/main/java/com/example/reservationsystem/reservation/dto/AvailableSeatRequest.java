package com.example.reservationsystem.reservation.dto;

import java.time.LocalDate;

public record AvailableSeatRequest(
        Long routeScheduleId
) {
}
