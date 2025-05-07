package com.system.vehicle.domain.model;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    private Route route;

    @Getter
    private String timeSlot;

    public RouteTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

}
