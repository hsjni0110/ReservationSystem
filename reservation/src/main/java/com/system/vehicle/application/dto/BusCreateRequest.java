package com.system.vehicle.application.dto;

public record BusCreateRequest(
        String busName,
        String busNumber,
        Integer capacity
) {
}
