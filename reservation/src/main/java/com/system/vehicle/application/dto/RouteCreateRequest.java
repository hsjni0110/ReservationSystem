package com.system.vehicle.application.dto;

import java.time.LocalDate;
import java.util.List;

public record RouteCreateRequest(
        String departure,
        String arrival,
        LocalDate scheduleDate,
        List<TimeSlot> timeSlots
) {

    public record TimeSlot(
            String time
    ) {}

}
