package com.example.reservationsystem.vehicle.presentation;

import com.example.reservationsystem.vehicle.application.BusService;
import com.example.reservationsystem.vehicle.dto.BusCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bus")
public class BusController {

    private final BusService busService;

    @PostMapping
    public ResponseEntity<Void> createBus(
            @RequestBody BusCreateRequest request
    ) {
        busService.createBus(request.busName(), request.busNumber(), request.capacity());
        return ResponseEntity
                .created(URI.create("/bus"))
                .build();
    }

}
