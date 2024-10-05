package com.example.reservationsystem.reservation.domain;

import com.example.reservationsystem.common.domain.BaseEntity;
import com.example.reservationsystem.user.signup.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "RESERVEATION")
@NoArgsConstructor
public class Reservation extends BaseEntity {

    public enum ReservationStatus {
        NOT_RESERVED, PENDING, RESERVED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long reservationId;

    @ManyToOne
    @JoinColumn
    private User user;

    @OneToMany
    @Getter
    private List<ScheduledSeat> scheduledSeats = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    public Reservation(User user, List<ScheduledSeat> scheduledSeats, ReservationStatus status) {
        this.user = user;
        this.status = status;
        this.addScheduledSeats(scheduledSeats);
    }

    private void addScheduledSeats(List<ScheduledSeat> scheduledSeats) {
        for (ScheduledSeat seat : scheduledSeats) {
            this.addScheduledSeat(seat);
        }
    }

    private void addScheduledSeat(ScheduledSeat scheduledSeat) {
        this.scheduledSeats.add(scheduledSeat);
        scheduledSeat.setReservation(this);
    }

    public static Reservation from(User user, List<ScheduledSeat> scheduledSeat) {
        return new Reservation(user, scheduledSeat, ReservationStatus.PENDING);
    }

}
