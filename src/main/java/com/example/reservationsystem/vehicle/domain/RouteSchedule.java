package com.example.reservationsystem.vehicle.domain;

import com.example.reservationsystem.common.domain.BaseEntity;
import com.example.reservationsystem.reservation.domain.ScheduledSeat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Entity
@Table(name = "ROUTE_SCHEDULE")
@NoArgsConstructor
public class RouteSchedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long routeScheduleId;

    @OneToOne
    @JoinColumn(name = "route_time_slot")
    private RouteTimeSlot routeTimeSlot;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bus")
    @Getter
    private Bus bus;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduledSeat> scheduledSeats = new ArrayList<>();

    private int availableSeats;

    public RouteSchedule(RouteTimeSlot routeTimeSlot, Bus bus, int availableSeats) {
        this.routeTimeSlot = routeTimeSlot;
        this.bus = bus;
        this.availableSeats = availableSeats;
        createScheduledSeats(availableSeats, this);
    }

    private void createScheduledSeats(int availableSeats, RouteSchedule routeSchedule) {
        IntStream.range(0, availableSeats)
                .forEach(i -> scheduledSeats.add(ScheduledSeat.of(i, routeSchedule)));
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
