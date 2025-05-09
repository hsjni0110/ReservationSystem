# API 부하 테스트 분석과 성능 개선

<br />

k6는 주로 **부하 테스트**와 **성능 테스트**에 사용되며, 운영 환경을 시뮬레이션하여 대규모 트래픽을 처리하는 시스템의 성능을 측정하고 최적화하는 데 중점을 둡니다. 하지만, 성능 개선 과정에서 발생하는 다양한 요청에 대한 **API 호출 응답 시간 단축** 문제 해결에도 효과적으로 활용될 수 있습니다.

<br />

## 잔액 조회 API 테스트

<br />

```js
export default function () {  
    const token = accessTokens[Math.floor(Math.random() * accessTokens.length)];  
  
    const res = http.get(`${BASE_URL}/account`, {  
        headers: {  
            Authorization: `Bearer ${token}`,  
        },  
    });  
  
    check(res, {  
        'Account request returned 200': (r) => r.status === 200,  
    });  
  
    sleep(1);  
}
```

<br />

![Image](https://github.com/user-attachments/assets/645ddcc5-e450-4884-8732-854b5c1bf676)

<br />

### 지표 분석

<br />

- 처리량
    - 전체 처리량 : 177,982건
    - 초당 처리량 : 1957 TPS (Transactions Per Second)
- 평균 응답 시간 : 95.96ms
- 최소 응답 시간 : 879µs (마이크로초)
- 중앙값 응답 시간 : 23.22ms - 이는 대부분의 요청이 매우 빠르게 처리되었음을 나타냅니다.
- P90 응답 시간 : 274.52ms - 전체 요청의 90%가 이 시간 내에 완료되어, 대부분의 요청이 양호한 지연 시간 내에 처리되었음을 의미합니다.
- HTTP 실패율 : 0% (177,982건 중 실패 건수 0건) - 이는 시스템이 매우 안정적으로 작동하고 있음을 보여줍니다.

<br />

### 분석 결론

<br />

- 시스템은 높은 처리량에도 불구하고 실패율이 매우 낮으며, 대규모 사용자를 효과적으로 처리할 수 있습니다.
- 대부분의 요청이 350ms 이내에 처리되므로 응답 시간은 사용자 경험을 저해하지 않는 수용 가능한 수준입니다.

<br />

## 예약 가능 좌석 조회 API 테스트

### 캐시 적용 이전

<br />

![Image](https://github.com/user-attachments/assets/3d8e8a9c-0072-4d53-ae08-d4a1e8e46670)

<br />

### 캐시 적용 이후

<br />

![Image](https://github.com/user-attachments/assets/5522ae20-2b53-4d6b-9b99-9f8b05dd6065)

<br />

### 지표 분석

<br />

- 평균 응답 시간: 캐시 적용 전 274.69ms에서 적용 후 63.48ms로 **약 76.9% 감소**
- 최소 응답 시간: 캐시 적용 전 1.17ms에서 적용 후 146µs로 **약 87.5% 감소**
- P90 응답 시간: 캐시 적용 전 625.27ms에서 적용 후 14.93ms로 **97.6% 감소**

<br />

**결론**: 캐시 적용은 API 응답 시간을 크게 단축시켜 사용자 체감 성능을 향상시키는 데 매우 효과적입니다.

<br />

## 예약 요청 API 테스트

<br />

예약 요청 API 성능 분석 결과, Redis를 이용한 분산락과 비관적 락의 이중 락 구조가 성능에 미치는 영향을 확인하고자 했습니다.

<br />

![Image](https://github.com/user-attachments/assets/00d927bd-98c6-4baa-a347-9c0fca4616cb)

<br />

테스트는 12,000개 경로에 대해 각 0~20개 좌석의 예약을 시도하는 방식으로 진행되었으며, 중복 예약 발생을 고려하여 성공적인 예약 요청에 대한 메트릭을 중심으로 분석했습니다.

<br />

이중 락 구조로 인해 응답 속도는 예상대로 느리게 나타났습니다.
- 평균 응답 시간: 2.2초
- P90 응답 시간: 3.88초

<br />

또한, 전체 처리량과 `dropped_iteration` 지표를 통해 평균 TPS가 낮음을 확인했습니다.

<br />

![Image](https://github.com/user-attachments/assets/561427bb-db4a-40a2-a1a6-03cf42a27d63)

<br />

로그 분석 결과, 실제 비즈니스 로직(ReservationManager.preserveSeat 메서드) 자체의 처리 시간은 길지 않았습니다. 따라서, 서버 자원 부족 또는 커넥션 풀 관련 문제일 가능성을 염두에 두고 분석을 진행했습니다.

<br />

예약 요청은 최대 초당 500회로 설정되었습니다.

<br />

![Image](https://github.com/user-attachments/assets/17b0ee02-875f-4881-b5e9-d7fb8bc620d6)

<br />

서버 사양은 다음과 같습니다.
- CPU 코어: 8
- Tomcat Thread Pool: 200 (기본 설정)
- HikariCP Max Pool Size: 10 (기본 설정)

<br />

단일 예약 요청에 대한 응답 시간은 약 80ms로 측정되었으며, 로깅 결과 비즈니스 로직 및 데이터베이스 작업 완료에 약 70ms가 소요되는 것으로 확인되었습니다.

<br />

이론적으로, 단일 스레드는 초당 약 12.5건의 요청을 처리할 수 있습니다 (1000ms / 80ms). Tomcat의 기본 스레드 풀 크기가 200이므로, 초당 약 2500건의 요청을 처리할 수 있습니다. 따라서 Tomcat Thread Pool 자체는 병목 지점이 아닐 가능성이 높습니다.

<br />

HikariCP 커넥션 풀의 경우, 트랜잭션 처리 시간이 건당 약 70ms이므로 커넥션당 초당 약 14.3건을 처리할 수 있습니다 (1000ms / 70ms). 10개의 커넥션을 통해 초당 총 143건의 요청을 처리할 수 있습니다.

결론적으로, 초당 143건 이상의 예약 요청이 발생할 경우 커넥션 풀이 병목 지점이 될 수 있습니다.

<br />

### 커넥션 풀 및 스레드 풀 튜닝

<br />

초기 커넥션 풀 크기(maximum-pool-size: 10)는 요청량에 비해 부족하여 대기열(pending)이 189까지 증가했습니다.

이로 인해 예약 요청 성공 여부와 관계없이 **커넥션 획득 지연으로 인해 응답 시간이 수 초 이상 지연**되는 현상이 발생했습니다. 따라서, 커넥션 풀 크기를 조정하고 각 스레드가 하나의 커넥션을 사용하여 작업을 처리하도록 최적화할 필요가 있었습니다.

기존 예약 로직은 다음과 같은 구조로 이루어져 있었습니다.

<br />

- `ReservationService.preserveSeat(…)`
    - 입력 유효성 검증 및 DTO 생성을 담당하며, `ReservationLockManager.preserveWithLock()` 메서드를 호출합니다.
- `ReservationLockManager.preserveWithLock(…)`
    - 분산 락 획득 로직을 담당하며, AOP를 통해 추상화되어 있습니다. `ReservationManager.preserve(…)` 메서드를 호출합니다.
- `ReservationManager.preserve(…)`
    - 실제 예약 비즈니스 로직이 구현되어 있으며, 비관적 락을 통해 이중 락 구조를 구현했습니다. `@Transactional` 어노테이션을 사용하여 데이터베이스 커넥션을 획득합니다.

<br />

기존 구조에서는 검증 로직과 비즈니스 로직이 분리되어 불필요하게 커넥션을 두 번 획득하는 문제가 있었습니다.

이를 확인하기 위해, 각 로직에서 사용되는 커넥션 정보를 로깅했습니다.

<br />

검증 로직에서의 커넥션 정보:

<br />

```java
public SeatReservationResponse preserveSeat( Long userId, Long routeScheduleId, List<Long> scheduleSeatIds ) {  
    validate( routeScheduleId, scheduleSeatIds );  
    printConnectionInfo("ReservationService - after validation");  
  
    Reservation reservation = reservationLockManager.preserveWithLock(userId, routeScheduleId, scheduleSeatIds);  
	...
}
```

<br />

![Image](https://github.com/user-attachments/assets/357f72cb-5ef4-4567-9daf-00405150df39)

<br />

비즈니스 로직에서의 커넥션 정보:

<br />

```java
@Transactional  
public Reservation preserve(Long userId, Long routeScheduleId, List<Long> scheduleSeatIds ) {  
    printConnectionInfo("ReservationManager - inside @Transactional");
    ...
}
```

<br />

![Image](https://github.com/user-attachments/assets/dd0c3122-9fda-4b9a-8fe5-b8fe0e32099e)

<br />

로그 분석 결과, 검증 로직과 비즈니스 로직에서 서로 다른 커넥션을 사용하는 것을 확인했습니다. 이는 불필요한 자원 낭비를 초래합니다.

따라서, 검증 로직과 비즈니스 로직이 하나의 트랜잭션 내에서 실행되도록 코드를 수정했습니다.

<br />

```java
@Transactional  
public Reservation preserve(Long userId, Long routeScheduleId, List<Long> scheduleSeatIds ) {  
    validate( routeScheduleId, scheduleSeatIds );  
    printConnectionInfo("ReservationService - after validation");  
    ...
    printConnectionInfo("ReservationManager - inside @Transactional");  
    return reservationRepository.save(reservation);  
}
```

<br />

![Image](https://github.com/user-attachments/assets/75a82c68-1715-430d-b163-1457e64cb511)

<br />

![Image](https://github.com/user-attachments/assets/8eb44788-50a8-46bf-bc52-37d9a11fb549)

<br />

수정 후, 검증 로직과 비즈니스 로직이 동일한 커넥션을 사용하는 것을 확인했습니다.
부하 테스트 결과, 응답 속도가 개선되었습니다.

<br />

![Image](https://github.com/user-attachments/assets/c775f4e9-fb04-48bb-9eb7-ae7469a5585d)

<br />

다음으로, 커넥션 풀 크기를 늘려 pending connection을 줄이고 응답 속도를 더욱 향상시키고자 했습니다.

<br />

![Image](https://github.com/user-attachments/assets/9f4b329c-54fe-4e8c-8bce-b39c1cd92a27)

<br />

그러나, 평균 응답 속도는 크게 개선되지 않았습니다.
모니터링 결과, GC 병목 현상이나 다른 특별한 원인은 발견되지 않았지만, 부하 테스트 중 모든 스레드가 busy 상태인 것을 확인했습니다.

<br />

![Image](https://github.com/user-attachments/assets/648528f8-b0c1-4234-b81c-3d2a361ded73)

<br />

다음과 같은 가정을 설정했습니다.
- 쿼리당 평균 응답 시간이 80ms이므로 이론상 초당 2500건의 요청을 처리할 수 있지만, 실제로는 GC, 컨텍스트 스위칭, I/O 지연 등의 요인으로 인해 최대 TPS가 800~1000건보다 낮을 수 있습니다.
- 커넥션 풀 크기를 늘려도 스레드 부족 문제가 해결되지 않으면 성능 개선 효과가 미미할 수 있습니다.

<br />

따라서, 더 나은 TPS를 확보하기 위해 Tomcat Thread Pool 크기를 늘렸습니다.

<br />

```yml
server:  
  tomcat:  
    threads:  
      max: 400  
    accept-count: 500
```

<br />

스레드 풀 크기를 늘리고 HikariCP 커넥션 풀 크기(50)는 유지했습니다.

<br />

![Image](https://github.com/user-attachments/assets/990709cc-cae3-4fee-a22a-65c48c93b792)

<br />

그 결과, 평균 응답 속도가 소폭 개선되었습니다.
최초 설정과 비교했을 때, 다음과 같은 성능 개선 효과를 얻었습니다.

<br />

- 평균 응답 시간 (avg): 2.2s → 1.47s (33.6% 감소)
- 중앙값 응답 시간 (med): 1.76s → 947.47ms (46.4% 감소)
- 최대 응답 시간 (max): 4.95s → 3.84s (22.5% 감소)
- P90 응답 시간: 3.88s → 2.7s (30.5% 감소)
- P95 응답 시간: 4.96s → 2.81s (30.7% 감소)
- TPS: 649 → 763 (약 26.8% 증가)