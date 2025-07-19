package com.reservationsystem;

import com.navercorp.fixturemonkey.ArbitraryBuilder;
import com.system.vehicle.domain.model.Bus;
import com.system.vehicle.domain.model.RouteTimeSlot;

public class ReservationFixture {

    public static ArbitraryBuilder<RouteTimeSlot> routeTimeSlot() {
        return FixtureCommon.fixtureMonkey.giveMeBuilder(RouteTimeSlot.class)
                .setNull("route")
                .setNull("routeTimeSlotId");
    }

    public static ArbitraryBuilder<Bus> bus() {
        return FixtureCommon.fixtureMonkey.giveMeBuilder(Bus.class)
                .setNull("busId");
    }

}
