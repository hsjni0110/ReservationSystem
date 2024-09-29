package com.example.reservationsystem.vehicle.domain;

import com.example.reservationsystem.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ROUTE_SCHEDULE")
@NoArgsConstructor
public class RouteSchedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long routeScheduleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_time_slot")
    private RouteTimeSlot routeTimeSlot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bus")
    private Bus bus;
    private int availableSeats;

    public RouteSchedule(RouteTimeSlot routeTimeSlot, Bus bus, int availableSeats) {
        this.routeTimeSlot = routeTimeSlot;
        this.bus = bus;
        this.availableSeats = availableSeats;
    }

    public static RouteSchedule create(Bus bus, RouteTimeSlot timeSlot) {
        return new RouteSchedule(timeSlot, bus, bus.getBusCapacity());
    }

    public boolean isAvailableSeats() {
        return availableSeats > 0;
    }

    public String getTimeSlot() {
        return this.routeTimeSlot.getTimeSlot();
    }

}
