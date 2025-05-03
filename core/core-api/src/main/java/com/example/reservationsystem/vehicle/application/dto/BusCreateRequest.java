package com.example.reservationsystem.vehicle.application.dto;

public record BusCreateRequest(
        String busName,
        String busNumber,
        Integer capacity
) {
}
