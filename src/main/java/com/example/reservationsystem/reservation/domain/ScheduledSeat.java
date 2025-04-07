package com.example.reservationsystem.reservation.domain;

import com.example.reservationsystem.common.domain.model.BaseEntity;
import com.example.reservationsystem.common.domain.model.Money;
import com.example.reservationsystem.common.converter.MoneyConverter;
import com.example.reservationsystem.reservation.exception.ReservationException;
import com.example.reservationsystem.vehicle.domain.model.RouteSchedule;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.example.reservationsystem.reservation.exception.ReservationExceptionType.ALREADY_PRESERVED_SEAT;
import static com.example.reservationsystem.reservation.exception.ReservationExceptionType.NOT_PRESERVED_SEAT;

@Entity
@Table(name = "SCHEDULED_SEAT")
@NoArgsConstructor
@Getter
public class ScheduledSeat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduledSeatId;
    private Integer seatId;

    @ManyToOne(fetch = FetchType.LAZY)
    private RouteSchedule routeSchedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @Setter
    private Reservation reservation;

    @Column(name = "seat_price")
    @Getter
    @Convert(converter = MoneyConverter.class)
    private Money seatPrice;

    @Column(name = "is_reserved")
    private Boolean isReserved;

    public ScheduledSeat(Integer seatId, RouteSchedule routeSchedule, boolean isReserved, long seatPrice) {
        this.seatId = seatId;
        this.routeSchedule = routeSchedule;
        this.isReserved = isReserved;
        this.seatPrice = Money.wons(seatPrice);
    }

    public static ScheduledSeat of(Integer seatId, RouteSchedule routeSchedule, long seatPrice) {
        return new ScheduledSeat(seatId, routeSchedule, false, seatPrice);
    }

    public void reserveSeat() {
        if (this.isReserved) {
            throw new ReservationException( ALREADY_PRESERVED_SEAT );
        }

        this.isReserved = true;
        routeSchedule.decreaseSeat();
    }

    public void cancelSeatReservation() {
        if (!this.isReserved) {
            throw new ReservationException(NOT_PRESERVED_SEAT);
        }

        this.isReserved = false;
        routeSchedule.increaseSeat();
    }

}
