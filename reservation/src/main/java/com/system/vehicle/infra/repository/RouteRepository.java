package com.system.vehicle.infra.repository;

import com.system.vehicle.domain.model.Route;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface RouteRepository extends JpaRepository<Route, Long> {

    Optional<Route> findById(Long id);
    Optional<Route> findByDepartureAndArrivalAndScheduleDate(String departure, String arrival, LocalDate scheduleDate);

    default Route getByIdOrThrow(Long id) {
        return findById(id).orElseThrow(EntityNotFoundException::new);
    }

}
