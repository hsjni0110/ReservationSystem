package com.example.reservationsystem.vehicle.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ROUTE_TIME_SLOT")
@NoArgsConstructor
public class RouteTimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long routeTimeSlotId;

    @Getter
    private String timeSlot;

    public RouteTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

}
