package com.system.reservationsystem.controller;

import com.system.vehicle.application.BusService;
import com.system.vehicle.application.dto.BusCreateRequest;
import com.system.vehicle.application.dto.BusCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bus")
public class BusController {

    private final BusService busService;

    @PostMapping
    public ResponseEntity<BusCreateResponse> createBus(
            @RequestBody BusCreateRequest request
    ) {
        Long busId = busService.createBus(request.busName(), request.busNumber(), request.capacity());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new BusCreateResponse(busId));
    }

}
