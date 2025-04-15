package com.example.reservationsystem.concurrency;

import com.example.reservationsystem.reservation.application.ReservationService;
import com.example.reservationsystem.reservation.exception.ReservationException;
import com.example.reservationsystem.user.signup.infra.repository.UserRepository;
import com.example.reservationsystem.vehicle.domain.model.Bus;
import com.example.reservationsystem.vehicle.domain.model.RouteSchedule;
import com.example.reservationsystem.vehicle.domain.model.RouteTimeSlot;
import com.example.reservationsystem.vehicle.infra.repository.BusRepository;
import com.example.reservationsystem.vehicle.infra.repository.RouteScheduleRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.reservationsystem.reservation.fixture.ReservationFixture.bus;
import static com.example.reservationsystem.reservation.fixture.ReservationFixture.routeTimeSlot;
import static com.example.reservationsystem.user.fixture.UserFixture.유저;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("예약 시스템 동시성 처리에서")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ReservationConcurrencyTest {

    @Autowired
    private ReservationService reservationService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private RouteScheduleRepository routeScheduleRepository;

    @Autowired
    private BusRepository busRepository;

    @BeforeEach
    @Transactional
    void setup() {
        RouteTimeSlot routeTimeSlot = routeTimeSlot().sample();
        Bus bus = bus().sample();
        busRepository.save(bus);
        RouteSchedule routeSchedule = new RouteSchedule(
                routeTimeSlot,
                bus,
                30,
                10000L
        );
        routeScheduleRepository.save(routeSchedule);
    }

    @Test
    void 동시에_예약하면_하나만_성공해야_한다() throws InterruptedException {
        // given
        var startTime = System.currentTimeMillis();
        var threadCount = 1000;
        var routeScheduleId = 1L;
        var seatId = 1L;
        var executorService = Executors.newFixedThreadPool(threadCount);
        var countDownLatch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            final long userId = i + 1;
            executorService.submit(() -> {
                try {
                    userRepository.save( 유저(userId) );
                    reservationService.preserveSeat( userId, routeScheduleId, List.of( seatId ));
                    successCount.incrementAndGet();
                } catch (ReservationException e) {
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        executorService.shutdown();

        var endTime = System.currentTimeMillis();
        var duration = endTime - startTime;

        assertThat(successCount.get()).isEqualTo(1);
        System.out.println("실행 시간 : " + duration + "밀리초");
    }

}
