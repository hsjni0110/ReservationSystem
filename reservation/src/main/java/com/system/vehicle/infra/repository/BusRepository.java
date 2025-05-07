package com.system.vehicle.infra.repository;

import com.system.vehicle.domain.model.Bus;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusRepository extends JpaRepository<Bus, Long> {

    Optional<Bus> findById(Long id);

    default Bus getByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new EntityNotFoundException("Bus with id " + id + " not found"));
    }

}
