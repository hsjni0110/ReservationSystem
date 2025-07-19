package com.system.point.application;

import com.system.domain.Money;
import com.system.point.application.dto.PointHistoryResponse;
import com.system.point.application.dto.PointResponse;
import com.system.point.domain.model.Point;
import com.system.point.domain.model.PointHistory;
import com.system.point.domain.model.PointPolicy;
import com.system.point.infra.repository.PointHistoryRepository;
import com.system.point.infra.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional(readOnly = true)
    public PointResponse getUserPoints(Long userId) {
        Point point = pointRepository.findByUserId(userId)
                .orElse(Point.of(userId));
        
        return new PointResponse(
                point.getPointId(),
                point.getUserId(),
                point.getTotalPoints()
        );
    }

    @Transactional(readOnly = true)
    public List<PointHistoryResponse> getUserPointHistory(Long userId) {
        List<PointHistory> histories = pointHistoryRepository.findByUserIdOrderByCreatedAtDesc(userId);
        
        return histories.stream()
                .map(history -> new PointHistoryResponse(
                        history.getPointHistoryId(),
                        history.getUserId(),
                        history.getEarnedPoints(),
                        getDescriptionByType(history.getSource()),
                        history.getCreatedAt()
                ))
                .toList();
    }

    private String getDescriptionByType(PointHistory.PointType type) {
        return switch (type) {
            case EARNED -> "결제 적립";
            case SPENT -> "포인트 사용";
            default -> "기타";
        };
    }

}
