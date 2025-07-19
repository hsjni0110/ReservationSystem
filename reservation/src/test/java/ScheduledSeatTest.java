import com.system.reservation.domain.ScheduledSeat;
import com.system.reservation.exception.ReservationException;
import com.system.reservation.exception.ReservationExceptionType;
import com.system.vehicle.domain.model.RouteSchedule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@DisplayName("ScheduledSeat는")
public class ScheduledSeatTest {

    @Mock
    RouteSchedule routeSchedule;

    @Test
    void 이미_예약된_좌성이면_ScheduledSeat는_예외를_반환한다() {
        // given
        Integer seatId = 1;

        // when
        ScheduledSeat scheduledSeat1 = new ScheduledSeat(seatId, routeSchedule, true, 10000);

        // then
        ReservationException reservationException = assertThrows(ReservationException.class, scheduledSeat1::reserveSeat);
        assertEquals(ReservationExceptionType.ALREADY_PRESERVED_SEAT, reservationException.exceptionType());
    }

}
