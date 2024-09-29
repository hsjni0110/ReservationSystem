package com.example.reservationsystem.vehicle.domain.repository;

import com.example.reservationsystem.vehicle.domain.Bus;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusRepository extends JpaRepository<Bus, Long> {

    Optional<Bus> findById(Long id);

    default Bus getByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new EntityNotFoundException("Bus with id " + id + " not found"));
    }

}
