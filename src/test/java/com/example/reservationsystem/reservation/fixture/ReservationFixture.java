package com.example.reservationsystem.reservation.fixture;

import com.example.reservationsystem.vehicle.domain.Bus;
import com.example.reservationsystem.vehicle.domain.RouteTimeSlot;
import com.navercorp.fixturemonkey.ArbitraryBuilder;

import static com.example.reservationsystem.common.FixtureCommon.fixtureMonkey;

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
