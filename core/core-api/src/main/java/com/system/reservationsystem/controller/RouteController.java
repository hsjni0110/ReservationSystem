package com.system.reservationsystem.controller;

import com.system.vehicle.application.RouteService;
import com.system.vehicle.application.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<RouteCreateResponse> createRoute(
            @RequestBody RouteCreateRequest request
    ) {
        Long routeId = routeService.createRoute(
                request.departure(),
                request.arrival(),
                request.scheduleDate(),
                request.timeSlots().stream().map(RouteCreateRequest.TimeSlot::time).toList()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new RouteCreateResponse(routeId));
    }

    @PostMapping("/dispatch/bus")
    public ResponseEntity<Void> createRouteSchedule(
            @RequestBody RouteScheduleCreateRequest request
    ) {
        routeService.dispatchBus(request.routeId(), request.busId(), request.timeSlot(), request.seatPrice());
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
