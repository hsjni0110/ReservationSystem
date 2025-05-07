package com.reservation.reservationsystem.lock;

import com.reservation.reservationsystem.common.aop.DistributedSimpleLockAspect;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DistributedLockDeadlockTest {

    @Autowired
    private TestReservationService testReservationService;

    private final ExecutorService executor = Executors.newFixedThreadPool(2);

    @Test
    void testPossibleDeadlockScenario() throws InterruptedException {
        Long routeScheduleId = 123L;

        List<Long> seatSetA = List.of(1L, 2L, 3L);
        List<Long> seatSetB = List.of(3L, 2L, 1L);

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(2);

        executor.submit(() -> {
            try {
                testReservationService.preserveWithLock(1L, routeScheduleId, seatSetA);
                System.out.println("Thread A Success");
            } catch (Exception e) {
                System.out.println("Thread A Failed: " + e.getMessage());
            } finally {
                endLatch.countDown();
            }
        });

        executor.submit(() -> {
            try {
                testReservationService.preserveWithLock(2L, routeScheduleId, seatSetB);
                System.out.println("Thread B Success");
            } catch (Exception e) {
                System.out.println("Thread B Failed: " + e.getMessage());
            } finally {
                endLatch.countDown();
            }
        });

        startLatch.countDown();
        endLatch.await(30, TimeUnit.SECONDS);
    }
}
