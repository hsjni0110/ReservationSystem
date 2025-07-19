import com.system.annotation.DistributedSimpleLock;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class TestReservationService {

    @DistributedSimpleLock(
            key = "'reservation:' + #routeScheduleId + ':[' + #scheduleSeatIds + ']'",
            waitTime = 30,
            releaseTime = 10,
            timeUnit = TimeUnit.SECONDS
    )
    public void preserveWithLock(Long userId, Long routeScheduleId, List<Long> scheduleSeatIds) {
        System.out.printf("[User %d] Trying to reserve seats: %s%n", userId, scheduleSeatIds);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
