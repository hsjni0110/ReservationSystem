package com.reservation.reservationsystem.point.service;

import com.reservation.reservationsystem.common.ServiceTest;
import com.system.domain.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import com.system.point.application.PointService;
import com.system.point.domain.model.Point;
import com.system.point.domain.model.PointHistory;
import com.system.point.domain.model.PointPolicy;
import com.system.point.infra.repository.PointHistoryRepository;
import com.system.point.infra.repository.PointRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("포인트 적립 서비스는")
public class PointServiceTest extends ServiceTest {

    @InjectMocks
    private PointService pointService;

    @Mock
    private PointHistoryRepository pointHistoryRepository;

    @Mock
    private PointPolicy pointPolicy;

    @Mock
    private PointRepository pointRepository;

    @Test
    void 적립포인트가_0이하면_포인트와_이력이_저장되지_않는다() {
        // given
        when(pointPolicy.calculatePoints(any())).thenReturn(Money.ZERO);

        // when
        pointService.earnPoints(1L, Money.wons(10000));

        // then
        verify(pointRepository, never()).findByUserId(any());
        verify(pointHistoryRepository, never()).save(any());
    }

    @Test
    void 유저_포인트가_없으면_새로_생성된다() {
        // given
        when(pointPolicy.calculatePoints(any())).thenReturn(Money.wons(500));
        when(pointRepository.findByUserId(1L)).thenReturn(Optional.empty());

        // when
        pointService.earnPoints(1L, Money.wons(10000));

        // then
        verify(pointRepository).findByUserId(1L);
    }

    @Test
    void 포인트와_히스토리가_정상적으로_저장된다() {
        // given
        Long userId = 1L;
        String transactionId = "tx-003";
        Money payment = Money.wons(10000);
        Money earned = Money.wons(1000);

        Point point = Point.of(userId);

        when(pointPolicy.calculatePoints(payment)).thenReturn(earned);
        when(pointRepository.findByUserId(userId)).thenReturn(Optional.of(point));

        // when
        pointService.earnPoints(userId, payment);

        // then
        assertThat(point.getTotalPoints()).isEqualTo(earned);

        ArgumentCaptor<PointHistory> historyCaptor = ArgumentCaptor.forClass(PointHistory.class);
        verify(pointHistoryRepository).save(historyCaptor.capture());

        PointHistory savedHistory = historyCaptor.getValue();
        assertThat(savedHistory).extracting("userId", "transactionId", "earnedPoints")
                .containsExactly(userId, transactionId, earned);
    }

}
