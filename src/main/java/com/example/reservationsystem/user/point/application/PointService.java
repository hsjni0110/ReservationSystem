package com.example.reservationsystem.user.point.application;

import com.example.reservationsystem.common.domain.Money;
import com.example.reservationsystem.user.point.domain.Point;
import com.example.reservationsystem.user.point.domain.PointHistory;
import com.example.reservationsystem.user.point.domain.PointPolicy;
import com.example.reservationsystem.user.point.domain.repository.PointHistoryRepository;
import com.example.reservationsystem.user.point.domain.repository.PointRepository;
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
    public void earnPoints(Long userId, Money paymentAmount, String transactionId) {
        validateTransactionId(transactionId);

        Money earnedPoints = calculateEarnedPoints(paymentAmount);
        if (!earnedPoints.isPositive()) return;

        Point point = findOrCreateUserPoint(userId);
        point.addPoints(earnedPoints);

        PointHistory history = PointHistory.earn(userId, transactionId, earnedPoints);
        pointRepository.save(point);
        pointHistoryRepository.save(history);
    }

    private void validateTransactionId(String transactionId) {
        if (pointHistoryRepository.existsByTransactionId(transactionId)) {
            throw new PointException(ALREADY_ADDED_POINT);
        }
    }

    private Money calculateEarnedPoints(Money paymentAmount) {
        return pointPolicy.calculatePoints(paymentAmount);
    }

    private Point findOrCreateUserPoint(Long userId) {
        return pointRepository.findByUserId(userId)
                .orElse(Point.of(userId));
    }

}
