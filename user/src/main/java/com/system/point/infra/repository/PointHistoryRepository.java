package com.system.point.infra.repository;

import com.system.point.domain.model.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
    List<PointHistory> findByUserIdOrderByCreatedAtDesc(Long userId);
}
