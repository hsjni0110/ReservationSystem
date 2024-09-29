package com.example.reservationsystem.vehicle.dto;

public record RouteScheduleResponse(
        Long routeScheduleId,
        Long busId,
        String timeSlot
) {
}
