package com.system.reservationsystem.controller;

import com.system.reservation.application.ReservationService;
import com.system.reservation.application.dto.AvailableSeatRequest;
import com.system.reservation.application.dto.ScheduledSeatResponse;
import com.system.reservation.application.dto.SeatReservationRequest;
import com.system.reservation.application.dto.SeatReservationResponse;
import com.system.auth.domain.Auth;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/seats")
    public ResponseEntity<List<ScheduledSeatResponse>> getAvailableSeats(
            @RequestBody AvailableSeatRequest request
    ) {
        List<ScheduledSeatResponse> scheduledSeats = reservationService.getSeatsByRoute(request.routeScheduleId());
        return ResponseEntity.ok(scheduledSeats);
    }

    @PostMapping("/seat")
    public ResponseEntity<SeatReservationResponse> preserveSeat(
            @RequestBody SeatReservationRequest request,
            @Auth Long userId
    ) {
        SeatReservationResponse seatReservationResponse = reservationService.preserveSeat(userId, request.routeScheduleId(), request.scheduleSeatIds());
        return ResponseEntity.ok(seatReservationResponse);
    }

}
