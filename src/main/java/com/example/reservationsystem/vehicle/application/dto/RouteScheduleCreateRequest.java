package com.example.reservationsystem.vehicle.application.dto;

public record RouteScheduleCreateRequest(
        Long routeId,
        Long busId,
        Long seatPrice,
        String timeSlot
) {
}
