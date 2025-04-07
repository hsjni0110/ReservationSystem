package com.example.reservationsystem.user.point.infra.repository;

import com.example.reservationsystem.user.point.domain.model.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

    boolean existsByTransactionId( String transactionId );

}
