package com.example.reservationsystem.vehicle.domain;

import com.example.reservationsystem.vehicle.exception.VehicleException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.example.reservationsystem.vehicle.exception.VehicleExceptionType.ROUTE_TIME_SLOT_NOT_FOUND;

@Entity
@Table(name = "ROUTE")
@NoArgsConstructor
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long routeId;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    private List<RouteTimeSlot> routeTimeSlots = new ArrayList<>();
    private String departure;
    private String arrival;

    @Getter
    private LocalDate scheduleDate;

    public Route(List<RouteTimeSlot> routeTimeSlots, String departure, String arrival, LocalDate scheduleDate) {
        this.routeTimeSlots = routeTimeSlots;
        this.departure = departure;
        this.arrival = arrival;
        this.scheduleDate = scheduleDate;
    }

    public static Route create(String departure, String arrival, LocalDate scheduleDate, List<String> times) {
        return new Route(times.stream().map(RouteTimeSlot::new).toList(), departure, arrival, scheduleDate);
    }

    public RouteTimeSlot getMatchedRouteTimeSlot(String time) {
        return routeTimeSlots.stream()
                .filter(routeTimeSlot -> routeTimeSlot.getTimeSlot().equals(time))
                .findFirst()
                .orElseThrow(() -> new VehicleException(ROUTE_TIME_SLOT_NOT_FOUND));
    }

}
