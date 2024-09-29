package com.example.reservationsystem.vehicle.dto;

import java.util.List;

public record RouteCreateRequest(
        String departure,
        String arrival,
        List<TimeSlot> timeSlots
) {

    public record TimeSlot(
            String time
    ) {}

}
