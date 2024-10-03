package com.example.reservationsystem.reservation.presentation;

import com.example.reservationsystem.reservation.application.ReservationService;
import com.example.reservationsystem.reservation.dto.AvailableSeatRequest;
import com.example.reservationsystem.reservation.dto.ScheduledSeatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/seats")
    public ResponseEntity<List<ScheduledSeatResponse>> getAvailableSeats(
            @ModelAttribute AvailableSeatRequest request
    ) {
        List<ScheduledSeatResponse> scheduledSeats = reservationService.getSeatsByRoute(request.departure(), request.arrival(), request.specificDate(), request.timeSlot());
        return ResponseEntity.ok(scheduledSeats);
    }

}
