package com.example.reservationsystem.reservation.application.dto;

import java.time.LocalDate;

public record AvailableSeatRequest(
        Long routeScheduleId
) {
}
