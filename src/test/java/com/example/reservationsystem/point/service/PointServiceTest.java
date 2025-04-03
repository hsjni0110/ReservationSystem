package com.example.reservationsystem.point.service;

import com.example.reservationsystem.common.ServiceTest;
import com.example.reservationsystem.common.domain.Money;
import com.example.reservationsystem.user.point.application.PointService;
import com.example.reservationsystem.user.point.domain.Point;
import com.example.reservationsystem.user.point.domain.PointHistory;
import com.example.reservationsystem.user.point.domain.PointPolicy;
import com.example.reservationsystem.user.point.domain.repository.PointHistoryRepository;
import com.example.reservationsystem.user.point.domain.repository.PointRepository;
import com.example.reservationsystem.user.point.exception.PointException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
    void 이미_처리된_트랜잭션이면_예외를_던진다() {
        // given
        String transactionId = "tx-123";
        when(pointHistoryRepository.existsByTransactionId(transactionId)).thenReturn(true);

        // when & then
        assertThatThrownBy(() ->
                pointService.earnPoints(1L, Money.wons(10000), transactionId))
                .isInstanceOf(PointException.class);
    }

    @Test
    void 적립포인트가_0이하면_포인트와_이력이_저장되지_않는다() {
        // given
        when(pointHistoryRepository.existsByTransactionId(anyString())).thenReturn(false);
        when(pointPolicy.calculatePoints(any())).thenReturn(Money.ZERO);

        // when
        pointService.earnPoints(1L, Money.wons(10000), "tx-001");

        // then
        verify(pointRepository, never()).findByUserId(any());
        verify(pointHistoryRepository, never()).save(any());
    }

    @Test
    void 유저_포인트가_없으면_새로_생성된다() {
        // given
        when(pointHistoryRepository.existsByTransactionId(anyString())).thenReturn(false);
        when(pointPolicy.calculatePoints(any())).thenReturn(Money.wons(500));
        when(pointRepository.findByUserId(1L)).thenReturn(Optional.empty());

        // when
        pointService.earnPoints(1L, Money.wons(10000), "tx-002");

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

        when(pointHistoryRepository.existsByTransactionId(transactionId)).thenReturn(false);
        when(pointPolicy.calculatePoints(payment)).thenReturn(earned);
        when(pointRepository.findByUserId(userId)).thenReturn(Optional.of(point));

        // when
        pointService.earnPoints(userId, payment, transactionId);

        // then
        assertThat(point.getTotalPoints()).isEqualTo(earned);

        ArgumentCaptor<PointHistory> historyCaptor = ArgumentCaptor.forClass(PointHistory.class);
        verify(pointHistoryRepository).save(historyCaptor.capture());

        PointHistory savedHistory = historyCaptor.getValue();
        assertThat(savedHistory).extracting("userId", "transactionId", "earnedPoints")
                .containsExactly(userId, transactionId, earned);
    }

}
