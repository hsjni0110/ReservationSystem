package com.example.reservationsystem.vehicle.domain;

import com.example.reservationsystem.common.domain.BaseEntity;
import com.example.reservationsystem.reservation.domain.ScheduledSeat;
import com.example.reservationsystem.vehicle.exception.VehicleException;
import com.example.reservationsystem.vehicle.exception.VehicleExceptionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.example.reservationsystem.vehicle.exception.VehicleExceptionType.BUS_CAPACITY_OVERFLOW;

@Entity
@Table(name = "ROUTE_SCHEDULE")
@NoArgsConstructor
public class RouteSchedule extends BaseEntity {

    public enum SALE_STATUS {
        SOLD_OUT,
        ON_SALE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long routeScheduleId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "route_time_slot")
    private RouteTimeSlot routeTimeSlot;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bus")
    @Getter
    private Bus bus;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduledSeat> scheduledSeats = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private SALE_STATUS saleStatus;
    private int availableSeats;

    public RouteSchedule(RouteTimeSlot routeTimeSlot, Bus bus, int availableSeats, long seatPrice) {
        this.routeTimeSlot = routeTimeSlot;
        this.bus = bus;
        this.availableSeats = availableSeats;
        this.saleStatus = SALE_STATUS.ON_SALE;
        createScheduledSeats(availableSeats, this, seatPrice);
    }

    private void createScheduledSeats(int availableSeats, RouteSchedule routeSchedule, long seatPrice) {
        IntStream.range(0, availableSeats)
                .forEach(i -> scheduledSeats.add(ScheduledSeat.of(i, routeSchedule, seatPrice)));
    }

    public static RouteSchedule create(Bus bus, RouteTimeSlot timeSlot, long seatPrice) {
        return new RouteSchedule(timeSlot, bus, bus.getBusCapacity(), seatPrice);
    }

    public boolean isAvailableSeats() {
        return saleStatus == SALE_STATUS.ON_SALE;
    }

    public String getTimeSlot() {
        return this.routeTimeSlot.getTimeSlot();
    }

    public void decreaseSeat() {
        this.availableSeats--;
        if (this.availableSeats == 0) {
            this.saleStatus = SALE_STATUS.SOLD_OUT;
        }
    }

    /*
     1. 예약 가능 좌석 수가 0에서 1로 가는 경우(결제 실패 시) 판매 가능 상태가 되야 한다.
     2. 버스의 수용 용량보다 많아진다면, 예외를 발생시킨다.
     */
    public void increaseSeat() {
        if (this.availableSeats == 0) {
            this.saleStatus = SALE_STATUS.ON_SALE;
        }

        if (this.bus.getBusCapacity() == this.availableSeats) {
            throw new VehicleException(BUS_CAPACITY_OVERFLOW);
        }

        this.availableSeats++;
    }

}
