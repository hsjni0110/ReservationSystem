package com.system.vehicle.application.dto;

import java.time.LocalDate;

public record RouteScheduleRequest(
        String departure,
        String arrival,
        LocalDate scheduleDate
) {
}
