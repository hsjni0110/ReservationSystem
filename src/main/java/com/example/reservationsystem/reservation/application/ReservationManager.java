package com.example.reservationsystem.reservation.application;

import com.example.reservationsystem.reservation.domain.Reservation;
import com.example.reservationsystem.reservation.domain.ScheduledSeat;
import com.example.reservationsystem.reservation.domain.repository.ReservationRepository;
import com.example.reservationsystem.reservation.domain.repository.ScheduledSeatRepository;
import com.example.reservationsystem.user.signup.domain.User;
import com.example.reservationsystem.user.signup.domain.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReservationManager {

    private final UserRepository userRepository;
    private final ScheduledSeatRepository scheduledSeatRepository;
    private final ReservationRepository reservationRepository;

    public ReservationManager(UserRepository userRepository, ScheduledSeatRepository scheduledSeatRepository, ReservationRepository reservationRepository) {
        this.userRepository = userRepository;
        this.scheduledSeatRepository = scheduledSeatRepository;
        this.reservationRepository = reservationRepository;
    }

    public Reservation preserve(Long userId, List<Long> scheduleSeatIds) {
        User user = userRepository.getByIdOrThrow(userId);
        List<ScheduledSeat> scheduledSeats = scheduleSeatIds.stream()
                .map(scheduledSeatRepository::getByIdOrThrow)
                .toList();
        Reservation reservation = Reservation.from(user, scheduledSeats);
        return reservationRepository.save(reservation);
    }

}
