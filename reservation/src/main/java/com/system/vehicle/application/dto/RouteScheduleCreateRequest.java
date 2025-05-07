package com.system.vehicle.application.dto;

public record RouteScheduleCreateRequest(
        Long routeId,
        Long busId,
        Long seatPrice,
        String timeSlot
) {
}
