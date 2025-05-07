package com.reservation.reservationsystem.vehicle.domain;

import com.reservation.reservationsystem.common.DomainTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.system.vehicle.domain.model.Bus;
import com.system.vehicle.domain.model.Route;
import com.system.vehicle.domain.model.RouteSchedule;
import com.system.vehicle.domain.model.RouteTimeSlot;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("RouteSchedule은")
public class RouteScheduleTest extends DomainTest {

    private Bus bus;
    private Route route;
    private RouteTimeSlot routeTimeSlot;

    @BeforeEach
    void setUp() {
        bus = Bus.create("강남고속", "1번", 30);
        route = Route.create("대전복합터미널", "센트럴시티", LocalDate.of(2024, 10, 10), List.of("10:00", "11:00", "13:00"));
        routeTimeSlot = route.getMatchedRouteTimeSlot("10:00");
    }

    @Test
    void RouteSchedule은_생성_시_좌석이_가용가능한_상태여야_한다() {
        // given
        RouteSchedule routeSchedule = RouteSchedule.create(bus, routeTimeSlot, 10000);

        // when, then
        assertTrue(routeSchedule.isAvailableSeats());
    }

    @Test
    void RouteSchedule은_적합한_TimeSlot을_가져올_수_있다() {
        // given
        RouteSchedule routeSchedule = RouteSchedule.create(bus, routeTimeSlot, 10000);

        // when
        String timeSlot = routeSchedule.getTimeSlot();

        // then
        assertThat(routeTimeSlot.getTimeSlot()).isEqualTo(timeSlot);
    }

}
