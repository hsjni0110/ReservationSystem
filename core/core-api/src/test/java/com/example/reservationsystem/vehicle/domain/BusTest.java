package com.example.reservationsystem.vehicle.domain;

import com.example.reservationsystem.vehicle.domain.model.Bus;
import org.junit.jupiter.api.Test;

public class BusTest {

    @Test
    void 버스를_등록할_수_있다() {
        Bus bus = Bus.create("강남고속", "1", 30);
    }

}
