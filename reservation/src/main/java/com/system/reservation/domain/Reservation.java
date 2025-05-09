package com.system.reservation.domain;

import com.system.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.system.reservation.exception.ReservationException;
import com.system.user.signup.domain.model.User;

import java.util.ArrayList;
import java.util.List;

import static com.system.reservation.exception.ReservationExceptionType.USER_NOT_MATCHED;

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

    @OneToMany(mappedBy = "reservation")
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
        Reservation reservation = new Reservation(user, scheduledSeat, ReservationStatus.PENDING);
        for (ScheduledSeat seat : scheduledSeat) {
            seat.setReservation(reservation);
            seat.reserveSeat();
        }
        return reservation;
    }

    public void authorizeUser(User user) {
        if (!this.user.equals(user)) {
            throw new ReservationException( USER_NOT_MATCHED );
        }
    }

    public boolean isPayable() {
        return status == ReservationStatus.PENDING;
    }

    public void successPayment() {
        this.status = ReservationStatus.RESERVED;
    }

    public void cancelReservation() {
        this.scheduledSeats.forEach( ScheduledSeat::cancelSeatReservation );
    }

}
