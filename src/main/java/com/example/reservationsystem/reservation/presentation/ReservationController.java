package com.example.reservationsystem.reservation.presentation;

import com.example.reservationsystem.auth.Auth;
import com.example.reservationsystem.reservation.application.ReservationService;
import com.example.reservationsystem.reservation.dto.AvailableSeatRequest;
import com.example.reservationsystem.reservation.dto.ScheduledSeatResponse;
import com.example.reservationsystem.reservation.dto.SeatReservationRequest;
import com.example.reservationsystem.reservation.dto.SeatReservationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/seats")
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
