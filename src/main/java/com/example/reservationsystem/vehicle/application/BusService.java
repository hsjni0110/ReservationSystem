package com.example.reservationsystem.vehicle.application;

import com.example.reservationsystem.vehicle.domain.Bus;
import com.example.reservationsystem.vehicle.domain.repository.BusRepository;
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
