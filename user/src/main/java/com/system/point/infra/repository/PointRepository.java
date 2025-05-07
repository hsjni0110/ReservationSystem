package com.system.point.infra.repository;

import com.system.point.domain.model.Point;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointRepository extends JpaRepository<Point, Long> {

    Optional<Point> findByUserId( Long userId );

}
