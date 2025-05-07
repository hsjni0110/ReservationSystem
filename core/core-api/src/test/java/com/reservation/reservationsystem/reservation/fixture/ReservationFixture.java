package com.reservation.reservationsystem.reservation.fixture;

import com.navercorp.fixturemonkey.ArbitraryBuilder;
import com.system.vehicle.domain.model.Bus;
import com.system.vehicle.domain.model.RouteTimeSlot;

import static com.reservation.reservationsystem.common.FixtureCommon.fixtureMonkey;

public class ReservationFixture {

    public static ArbitraryBuilder<RouteTimeSlot> routeTimeSlot() {
        return fixtureMonkey.giveMeBuilder(RouteTimeSlot.class)
                .setNull("route")
                .setNull("routeTimeSlotId");
    }

    public static ArbitraryBuilder<Bus> bus() {
        return fixtureMonkey.giveMeBuilder(Bus.class)
                .setNull("busId");
    }

}
