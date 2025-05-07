package com.system.vehicle.application;

import com.system.vehicle.domain.model.Bus;
import com.system.vehicle.infra.repository.BusRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BusService {

    private final BusRepository busRepository;

    @Transactional
    public Long createBus(String busName, String busNumber, int capacity) {
        Bus bus = Bus.create(busName, busNumber, capacity);
        Bus saved = busRepository.save(bus);
        return saved.getBusId();
    }

}
