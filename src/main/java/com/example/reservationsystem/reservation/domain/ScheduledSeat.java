package com.example.reservationsystem.reservation.domain;

import com.example.reservationsystem.common.domain.BaseEntity;
import com.example.reservationsystem.vehicle.domain.RouteSchedule;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private Boolean isReserved;

    public ScheduledSeat(Integer seatId, RouteSchedule routeSchedule, boolean isReserved) {
        this.seatId = seatId;
        this.routeSchedule = routeSchedule;
        this.isReserved = isReserved;
    }

    public static ScheduledSeat of(Integer seatId, RouteSchedule routeSchedule) {
        return new ScheduledSeat(seatId, routeSchedule, false);
    }

}
