package com.reservation.reservationsystem.concurrency;

import com.system.application.AccountService;
import com.system.domain.Money;
import com.system.domain.model.Account;
import com.system.infra.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.system.user.signup.domain.model.User;
import com.system.user.signup.infra.repository.UserRepository;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static com.reservation.reservationsystem.user.fixture.UserFixture.유저;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("계좌 동시성 처리에서")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AccountConcurrencyTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    @Transactional
    void setup() {
        user = userRepository.save(유저());
        accountRepository.save(
                new Account(1L, user.getUserId(), Money.wons(100L))
        );
    }

    @Test
    void 동시에_여러_충전이_들어와도_한_번만_충전되어야_한다() throws InterruptedException {
        // given
        var startTime = System.currentTimeMillis();
        var threadCount = 100;
        var executorService = Executors.newFixedThreadPool(threadCount);
        var latch = new CountDownLatch(threadCount);
        var rechargeAmount = 100L;
        var successfulRecharges = new AtomicLong(0);
        var failedRecharges = new AtomicLong(0);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    accountService.recharge(user.getUserId(), rechargeAmount);
                    successfulRecharges.incrementAndGet();
                } catch (Exception e) {
                    failedRecharges.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        // then
        assertEquals(1, successfulRecharges.get(), "오직 한 번만 충전에 성공해야 한다.");
        assertEquals(99, failedRecharges.get(), "나머지는 모두 실패한다.");

        var endTime = System.currentTimeMillis();
        var duration = endTime - startTime;
        System.out.println("실행 시간 : " + duration + "밀리초");
    }

}
