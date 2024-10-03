package com.example.reservationsystem.vehicle.presentation;

import com.example.reservationsystem.vehicle.application.RouteService;
import com.example.reservationsystem.vehicle.dto.RouteCreateRequest;
import com.example.reservationsystem.vehicle.dto.RouteScheduleCreateRequest;
import com.example.reservationsystem.vehicle.dto.RouteScheduleRequest;
import com.example.reservationsystem.vehicle.dto.RouteScheduleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/route")
public class RouteController {

    private final RouteService routeService;

    @PostMapping
    public ResponseEntity<Void> createRoute(
            @RequestBody RouteCreateRequest request
    ) {
        routeService.createRoute(request.departure(), request.arrival(), request.scheduleDate(), request.timeSlots().stream().map(RouteCreateRequest.TimeSlot::time).toList());
        return ResponseEntity.created(URI.create("/route/" + request.departure() + "/" + request.arrival())).build();
    }

    @PostMapping("/dispatch/bus")
    public ResponseEntity<Void> createRouteSchedule(
            @RequestBody RouteScheduleCreateRequest request
    ) {
        routeService.dispatchBus(request.routeId(), request.busId(), request.timeSlot());
        return ResponseEntity.created(URI.create("/route/" + request.routeId())).build();
    }

    @PostMapping("/route-schedules")
    public ResponseEntity<List<RouteScheduleResponse>> getAvailableRouteSchedules(
            @RequestBody RouteScheduleRequest request
    ) {
        List<RouteScheduleResponse> availableRouteSchedules = routeService.getAvailableRouteSchedules(request.departure(), request.arrival(), request.scheduleDate());
        return ResponseEntity.ok(availableRouteSchedules);
    }

}
