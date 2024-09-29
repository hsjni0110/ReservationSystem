package com.example.reservationsystem.vehicle.dto;

public record RouteScheduleCreateRequest(
        Long routeId,
        Long busId,
        String timeSlot
) {
}
