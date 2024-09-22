package com.example.reservationsystem.vehicle.dto;

public record BusCreateRequest(
        String busName,
        String busNumber,
        Integer capacity
) {
}
