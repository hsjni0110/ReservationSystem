package com.example.reservationsystem.vehicle.dto;

import java.time.LocalDate;

public record RouteScheduleRequest(
        String departure,
        String arrival,
        LocalDate scheduleDate
) {
}
