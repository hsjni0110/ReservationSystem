# 동시성과 관련된 문제 해결

잔액 충전 로직, 버스 예약 로직에서의 문제는 다음과 같습니다.

### 잔액 충전 로직에서의 문제

- 유저가 잔액 충전을 하려고 할 때 네트워크 지연 등의 문제로 여러 번 충전 버튼을 눌러 한 번에 여러 번 호출 요청이 들어갈 수 있습니다.
- 사용자가 여러 번 충전 요청을 보내더라도, 단 한 번만 증가해야 합니다.

### 버스 예약 로직에서의 문제

- 두 명의 유저가 동시에 같은 좌석을 예약하려고 할 때 두 명다 성공하면 안됩니다.
- 먼저 온 순서대로 한 명만이 성공해야 합니다.
- 동시성 제어 실패로 인한 중복 예약을 막아야 합니다.

위와 같이 동시성을 제어하지 못하는 원인은 아래와 같습니다.

1. 트랜잭션의 범위가 넓다.
2. 범위가 넓음으로 인해 동시 요청 처리 시 데드락이 발생하거나, 성능이 저하될 가능성이 있다.
3. 특정 부분만 트랜잭션을 적용시키지 못한다.
4. 데이터베이스 연결이 오래 유지되기에 성능이 저하된다.

즉, 위와 같은 동시성 문제에 대응하기 위해서 트랜잭션의 범위를 좁히고 더 세밀하게 동시성을 제어할 필요가 있습니다.

## 낙관적 락을 통한 동시성 문제 해결

낙관적 락(Optimistic Lock)은 업데이트가 드물게 일어난다는 가정 하에 사용합니다. 이는 실제 DB 단에서 Lock을 설정하지 않고 
Version을 관리하는 컬럼을 테이블에 추가해서 현재 수정하려는 테이블의 버전이 일치하는지 확인합니다.

다만 실제 DB Lock을 거는 방식이 아니라 실패 시 롤백을 직접 어플리케이션 단에서 구현해 주어야 합니다.
낙관적 락을 구현해주기 위해서는 간단하게 구현해 놓은 엔티티에 `@Version` 어노테이션을 추가한 필드를 추가하면 됩니다.

```java
@Entity
@Table(name = "ACCOUNT")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @OneToOne
    @JoinColumn
    private User user;

    @Getter
    @Convert(converter = MoneyConverter.class)
    @Builder.Default
    private Money amount = Money.ZERO;

    @Version
    private Long version; // 여기 추가합니다
```

위와 같이 버전을 추가해준 후 실제 조회 시에 낙관적 락을 사용할 수 있습니다.
JPA에서는 락에 대한 어노테이션을 지원하기 때문에 간편하게 락을 걸 수 있습니다.

```java
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select a from Account a where a.user = :user")
    Optional<Account> findByUserForUpdate(User user);

}
```

만약 버전이 일치하지 않는 경우 단순히 예외를 발생시키는 것으로 끝나는 것이 아니라 재 조회를 한다던가 하려면 직접 구현해 주어야 합니다. 이 과정에서 비관적 락보다는 시간이 조금 길어질 수 있습니다.

```java
@DisplayName("계좌 동시성 처리에서")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
                new Account(1L, user, Money.wons(100L))
        );
    }

    @Test
    void 동시에_여러_충전이_들어와도_한_번만_충전되어야_한다() throws InterruptedException {
        // given
        var threadCount = 10;
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
        assertEquals(9, failedRecharges.get(), "나머지는 모두 실패한다.");
    }

}
```

위 코드는 스레드 10개에 대한 동시성 로직 테스트입니다.

<img width="865" alt="image" src="https://github.com/user-attachments/assets/075629f9-6cf7-4e5c-ac27-bed60a944d52">

보다시피 잘 통과하는 것을 확인할 수 있습니다.
하지만, 스레드 100개면 어떨까요?

<img width="1156" alt="image" src="https://github.com/user-attachments/assets/9abebf75-6d2e-4f54-9d65-10817eeb268f">

한번만 성공해야 하는 테스트는 13번이나 성공하여 100원만 충전되길 원했으나 1300원이 충전되버렸습니다.

## 비관적 락

비관적 락은 동시 업데이트가 빈번하게 일어날 것이라 '비관적'으로 가정하고 락을 거는 방식입니다.
따라서 데이터를 읽는 시점에 락을 걸어 다른 트랜잭션의 접근을 차단합니다. 이는 데이터의 무결성을 강하게 보장하지만, 동시성 처리 속도가 느려질 수 있습니다.

```java
...
public SeatReservationResponse preserveSeat( Long userId, Long routeScheduleId, List<Long> scheduleSeatIds ) {
    validate( routeScheduleId, scheduleSeatIds );
    try {
        Reservation reservation = reservationManager.preserve( userId, scheduleSeatIds );
        return new SeatReservationResponse(
                reservation.getReservationId(),
                reservation.getScheduledSeats().stream()
                        .map(ScheduledSeat::getScheduledSeatId)
                        .toList());
    } catch ( PessimisticLockingFailureException e ) {
        throw new ReservationException( ALREADY_PRESERVED_SEAT );
    }
    }
...
```

기존 코드에서 트랜잭션의 범위를 줄여주고, 실질적인 예약 프로세스는 `preserve()`에서 진행합니다.

```java
@Transactional
public Reservation preserve( Long userId, List<Long> scheduleSeatIds ) {
    User user = userRepository.getByIdOrThrow( userId );

    List<ScheduledSeat> scheduledSeats = scheduleSeatIds.stream()
            .map( scheduledSeatRepository::findByIdWithPessimisticLock )
            .toList();

    scheduledSeats.forEach(
            scheduledSeat -> {
                if ( scheduledSeat.isReserved() ) {
                    throw new ReservationException(ALREADY_PRESERVED_SEAT);
                }
                scheduledSeat.updateStatus(true);
            }
    );

    Reservation reservation = Reservation.from( user, scheduledSeats );
    return reservationRepository.save( reservation );
}
```

`preserve()`함수에서 비관적 락을 걸어놓은 ScheduleSeat 테이블에 대해서 조회하고, 각각에 대해서 이미 예약된 좌석인지 확인합니다.
만약, 예약되지 않은 좌석이라면 예약 상태를 true로 바꾸어놓습니다.

```java
@Test
void 동시에_예약하면_하나만_성공해야_한다() throws InterruptedException {
    // given
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
                userRepository.save(유저(userId));
                reservationService.preserveSeat(userId, routeScheduleId, List.of(seatId));
                successCount.incrementAndGet();
            } catch (ReservationException e) {
            } finally {
                countDownLatch.countDown();
            }
        });
    }

    countDownLatch.await();
    executorService.shutdown();

    assertThat(successCount.get()).isEqualTo(1);
}
```
동시에 예약하면, 한 명만이 성공하는 지 확인합니다. 1000개의 스레드로 접근해도 하나만 성공하도록 보장합니다.

<img width="829" alt="image" src="https://github.com/user-attachments/assets/12310407-e5d7-4b6b-b266-439cb9f7b28c">

- 비관적 락의 특징
  - 비관적 락은 데이터 정합성을 강하게 보장한다.
  - 동시성 환경이 높은 경우 시스템 전체 처리량이 저하될 수 있다.
  - 데이터베이스 수준의 락을 사용하므로 확장성에 제한이 있다. 분산 환경에서의 확장성에서는 제한을 받을 수 있다.


## 분산 락

분산 락은 여러 서버나 인스턴스에서 동시에 접근하는 리소스에 대해서 동시성을 제어하기 위해 사용됩니다.
Redis와 AOP를 통해서 분산락을 구현합니다.

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedSimpleLock {
    String key();
    long waitTime() default 5;
    long releaseTime() default 10;
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
```

위와 같이 어노테이션으로 등록하여 분산락을 적용할 수 있도록 합니다.

- key : 리소스를 점유하고 있는 스레드를 구분하고자 작성한다.
- waitTime : 대기 시간
- releaseTime : 임대 시간

```java
@Aspect
@Component
public class DistributedSimpleLockAspect {

    private final RedisSimpleLock redisSimpleLock;

    public DistributedSimpleLockAspect( RedisSimpleLock redisSimpleLock ) {
        this.redisSimpleLock = redisSimpleLock;
    }

    @Around("@annotation(com.example.reservationsystem.common.annotation.DistributedSimpleLock)")
    public Object around( ProceedingJoinPoint joinPoint ) throws Throwable {
        MethodSignature signature = ( MethodSignature ) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedSimpleLock distributedSimpleLock = method.getAnnotation( DistributedSimpleLock.class );

        String lockKey = distributedSimpleLock.key();
        String lockValue = UUID.randomUUID().toString();

        try {
            boolean acquired = redisSimpleLock.tryLock(
                    lockKey,
                    lockValue,
                    distributedSimpleLock.releaseTime(),
                    distributedSimpleLock.timeUnit()
            );
            if ( !acquired ) {
                throw new BusinessException( OTHER_THREAD_ASSIGNED );
            }
            return joinPoint.proceed();
        } finally {
            redisSimpleLock.releaseLock( lockKey, lockValue );
        }
    }

}
```

`DistributeSimpleLock` 어노테이션을 적용한 메서드에 대해서 동작하도록 한 AOP입니다.
어노테이션을 통해서 등록한 key와 랜덤한 value를 redis template를 통해 기간과 함께 등록합니다.
이때, 동일한 요청(userId가 같은 경우 등)에 대해서는 키가 같으므로 일정 기간이 지나지 않았다면 false를 반환합니다.
메서드가 종료된다면, 락을 해제하여 다른 스레드가 점유할 수 있도록 합니다.

- 순서 : 락 획득 -> 트랜잭션 시작 -> 비즈니스 로직 -> 트랜잭션 종료 -> 락 해제

Redis는 redisTemplate을 사용해 쉽게 등록 및 해제가 가능하도록 빈으로 등록해두었습니다.

```java
@Component
public class RedisSimpleLock {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisSimpleLock(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean tryLock(String key, String value, long leaseTime, TimeUnit timeUnit) {
        Boolean result = redisTemplate
                .opsForValue()
                .setIfAbsent(key, value, leaseTime, timeUnit);
        return result != null && result;
    }

    public boolean releaseLock(String key, String value) {
        String lockValue = redisTemplate.opsForValue().get(key);
        if (value.equals(lockValue)) {
            redisTemplate.delete(key);
            return true;
        }
        return false;
    }

}
```

저는 버스 예약 시 좌석을 점유하는 메서드에 대해서 비관적 락을 걸어놓았으나 여기에 분산락까지 적용시켜 이중락으로써 구성하였습니다.
만약, Redis가 장애로 인해 다운되더라도 분산락으로써 동시성을 보장할 수 있도록 합니다.

아래는 변경된 ReservationService입니다.

```java
public SeatReservationResponse preserveSeat( Long userId, Long routeScheduleId, List<Long> scheduleSeatIds ) {
    validate( routeScheduleId, scheduleSeatIds );
    try {
        Reservation reservation = reservationLockManager.preserveWithLock( userId, scheduleSeatIds );
        return new SeatReservationResponse(
                reservation.getReservationId(),
                reservation.getScheduledSeats().stream()
                        .map(ScheduledSeat::getScheduledSeatId)
                        .toList());
    } catch ( PessimisticLockingFailureException e ) {
        throw new ReservationException( ALREADY_PRESERVED_SEAT );
    }
}
```

여기서 주목할 점은 ReservationService내에서는 AOP를 적용하지 않는다는 점입니다. 그냥 서비스 내에서 하면 클래스 하나 더 만들 필요없지 않냐 생각할 수 있는데 AOP 특성 상
같은 객체 내에서는 메서드를 호출한 경우 AOP가 적용되지 않습니다. 이는, AOP가 프록시 기반이며 메서드 호출 시 원본 객체의 메서드를 호출하기 때문입니다.

따라서, 위와 같이 따로 Lock을 관리하는 Manager를 만들어주었고, 이 객체에게 락과 함께 좌석을 점유하는 책임을 부여해주었습니다.

```java
@Component
public class ReservationLockManager {

    private final ReservationManager reservationManager;

    public ReservationLockManager(ReservationManager reservationManager) {
        this.reservationManager = reservationManager;
    }

    @DistributedSimpleLock(
            key = "reservationUserId:#userId",
            waitTime = 5,
            releaseTime = 10
    )
    public Reservation preserveWithLock( Long userId, List<Long> scheduleSeatIds ) {
        return reservationManager.preserve( userId, scheduleSeatIds );
    }

}
```

ReservationManager에서는 이제 실제로 좌석을 점유하는 트랜잭션을 시작하고 종료합니다. 이로써, 대규모 요청 상황에 있어서도 데이터 정합성을 유지할 수 있고,
Redis를 통해서 다중 서버의 요청에도 빠른 응답시간을 유지할 수 있게 되었습니다.

실제로, 속도 면에서도 이점이 있는 지 확인해보겠습니다.

- 분산락 적용 이전

<img width="967" alt="image" src="https://github.com/user-attachments/assets/f70b2a2a-43af-4a89-83c8-4d9709729380">

- 분산락 적용 이후

<img width="1030" alt="image" src="https://github.com/user-attachments/assets/c91e920e-6550-420c-bf0c-be07966a1cc2">


