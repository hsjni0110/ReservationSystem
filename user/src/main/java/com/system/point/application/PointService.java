package com.system.point.application;

import com.system.domain.Money;
import com.system.point.domain.model.Point;
import com.system.point.domain.model.PointHistory;
import com.system.point.domain.model.PointPolicy;
import com.system.point.infra.repository.PointHistoryRepository;
import com.system.point.infra.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointHistoryRepository pointHistoryRepository;
    private final PointPolicy pointPolicy;
    private final PointRepository pointRepository;

    public void earnPoints( Long userId, Money paymentAmount ) {
        Money earnedPoints = calculateEarnedPoints(paymentAmount);
        if (!earnedPoints.isPositive()) return;

        Point point = findOrCreateUserPoint(userId);
        point.addPoints(earnedPoints);

        PointHistory history = PointHistory.earn( userId, earnedPoints );
        pointRepository.save(point);
        pointHistoryRepository.save(history);
    }

    private Money calculateEarnedPoints(Money paymentAmount) {
        return pointPolicy.calculatePoints(paymentAmount);
    }

    private Point findOrCreateUserPoint( Long userId ) {
        return pointRepository.findByUserId( userId )
                .orElse(Point.of(userId));
    }

}
