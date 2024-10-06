package com.example.reservationsystem.reservation;

import com.example.reservationsystem.common.DomainTest;
import com.example.reservationsystem.reservation.domain.ScheduledSeat;
import com.example.reservationsystem.reservation.exception.ReservationException;
import com.example.reservationsystem.reservation.exception.ReservationExceptionType;
import com.example.reservationsystem.vehicle.domain.RouteSchedule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@DisplayName("ScheduledSeat는")
public class ScheduledSeatTest extends DomainTest {

    @Mock
    RouteSchedule routeSchedule;

    @Test
    void 이미_예약된_좌성이면_ScheduledSeat는_예외를_반환한다() {
        // given
        Integer seatId = 1;

        // when
        ScheduledSeat scheduledSeat1 = new ScheduledSeat(seatId, routeSchedule, true, 10000);

        // then
        ReservationException reservationException = assertThrows(ReservationException.class, scheduledSeat1::isReserved);
        assertEquals(ReservationExceptionType.ALREADY_PRESERVED_SEAT, reservationException.exceptionType());
    }

}
