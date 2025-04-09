package com.example.reservationsystem.user.point.application;

import com.example.reservationsystem.common.domain.model.Money;
import com.example.reservationsystem.user.point.domain.model.Point;
import com.example.reservationsystem.user.point.domain.model.PointHistory;
import com.example.reservationsystem.user.point.domain.model.PointPolicy;
import com.example.reservationsystem.user.point.infra.repository.PointHistoryRepository;
import com.example.reservationsystem.user.point.infra.repository.PointRepository;
import com.example.reservationsystem.user.point.exception.PointException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.reservationsystem.user.point.exception.PointExceptionType.ALREADY_ADDED_POINT;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointHistoryRepository pointHistoryRepository;
    private final PointPolicy pointPolicy;
    private final PointRepository pointRepository;

    @Transactional
    public void earnPoints( Long userId, Money paymentAmount ) {
        Money earnedPoints = calculateEarnedPoints(paymentAmount);
        if (!earnedPoints.isPositive()) return;

        Point point = findOrCreateUserPoint(userId);
        point.addPoints(earnedPoints);

        PointHistory history = PointHistory.earn(userId, earnedPoints);
        pointRepository.save(point);
        pointHistoryRepository.save(history);
    }

    private Money calculateEarnedPoints(Money paymentAmount) {
        return pointPolicy.calculatePoints(paymentAmount);
    }

    private Point findOrCreateUserPoint(Long userId) {
        return pointRepository.findByUserId(userId)
                .orElse(Point.of(userId));
    }

}
