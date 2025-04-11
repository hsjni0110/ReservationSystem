# 캐시 적용을 통한 성능 개선

## 개요

버스 예약 시스템에서 만석이 아닌 특정 경로의 시간대 검색과 각 시간대에서 각 좌석들의 만료 여부 검색에 Cache를 도입하는 것을 목적으로 합니다.
여기서는 캐시를 도입한 이유, 그리고 과정과 결과에 대해서 설명합니다.

## 캐시 설정 코드 분석

```java
@Configuration
@EnableCaching
public class CacheConfig {

    public static final String ONE_MIN_CACHE = "one-min-cache";
    public static final String FIVE_MIN_CACHE = "five-min-cache";
    private static final long TTL_ONE_MINUTE = 1L;
    private static final long TTL_FIVE_MINUTE = 5L;

    private final ObjectMapper objectMapper;

    public CacheConfig( ObjectMapper objectMapper ) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return builder -> builder
                .withCacheConfiguration(
                        ONE_MIN_CACHE,
                        redisCacheConfigurationByTtl( objectMapper, TTL_ONE_MINUTE )
                )
                .withCacheConfiguration(
                        FIVE_MIN_CACHE,
                        redisCacheConfigurationByTtl( objectMapper, TTL_FIVE_MINUTE )
                );
    }

    private RedisCacheConfiguration redisCacheConfigurationByTtl( ObjectMapper objectMapper, long ttl ) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .computePrefixWith( cacheName -> cacheName + "::" )
                .entryTtl( Duration.ofMinutes( ttl ) )
                .disableCachingNullValues()
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                                new StringRedisSerializer()
                        )
                )
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                                new GenericJackson2JsonRedisSerializer(objectMapper)
                        )
                );
    }

}
```

- `@EnableCaching` : 캐싱 기능을 활성화합니다.
- `RedisCacheManagerBuilderCustomizer` : Bean을 정의해 Redis 캐시 매니저를 커스터마이즈 합니다.
  - 이를 통해서 서로 다른 TTL의 캐시를 설정할 수 있습니다.
  - 원하는 TTL의 캐싱을 `@Cacheable`을 통해서 손쉽게 적용할 수 있게 됩니다.
- `computePrefixWith( cacheName -> cacheName + "::" )` : 접두사를 통해서 네임스페이스를 구분합니다.
- `disableCachingNullValue()` : value값에 null이 들어가는 것을 방지합니다.
- `serializeKeyWith(...)` : 캐시 키를 String으로 직렬화 합니다.
- `serializeValuesWith(...)` : 캐시 값을 Json 형식으로 직렬화합니다.

위와 같은 설정으로, 반환값이 Json DTO인 경우에도 캐싱을 통해 DB 접근을 막을 수 있습니다.

## 캐시 적용 분석

### RouteService에서 캐시 적용

```java
@Cacheable(
        cacheNames = CacheConfig.FIVE_MIN_CACHE,
        key = "'available-route-schedule'",
        condition = "#departure != null && #arrival != null && #scheduleDate != null",
        sync = true
)
public List<RouteScheduleResponse> getAvailableRouteSchedules(String departure, String arrival, LocalDate scheduleDate) {
    Route route = routeRepository.findByDepartureAndArrivalAndScheduleDate(departure, arrival, scheduleDate).orElseThrow(() -> new VehicleException(ROUTE_NOT_FOUND));
    List<RouteSchedule> routeSchedules = routeScheduleRepository.findByRouteTimeSlotIn(route.getRouteTimeSlots());
    return routeSchedules.stream()
            .filter(RouteSchedule::isAvailableSeats)
            .map(routeSchedule -> new RouteScheduleResponse(routeSchedule.getRouteScheduleId(), routeSchedule.getBus().getBusId(), routeSchedule.getTimeSlot()))
            .toList();
}
```

`getAvailableRouteSchedules()`은 버스 예약 시, 특정 경로와 날짜에서 매진이 아닌 시간대들을 반환하는 함수입니다.

해당 함수에서 캐시를 도입한 이유는 다음과 같습니다.

- 버스 예약 시 대부분의 사람들은 시간대를 먼저 확인합니다. 이때, 매진 여부도 함께 확인합니다.
- 이러한 조회는 빈번하게 발생하며 매번 조회하는 것은 시스템 성능을 낮출 수 있습니다.
- TTL을 적당히 5분으로 설정하여 데이터의 일관성은 유지하면서도 성능의 향상을 도모할 수 있습니다.

이때, key는 'available-route-schedule'로 설정합니다.


### ReservationService에서의 캐시 적용

```java
@Cacheable(
        cacheNames = CacheConfig.ONE_MIN_CACHE,
        key = "'route-schedule-' + #routeScheduleId",
        condition = "#routeScheduleId != null",
        sync = true
)
public List<ScheduledSeatResponse> getSeatsByRoute(Long routeScheduleId ) {
    RouteSchedule routeSchedule = routeScheduleRepository.findById( routeScheduleId )
            .orElseThrow(() -> new ReservationException(ROUTE_SCHEDULE_NOT_FOUND));
    List<ScheduledSeat> scheduledSeats = scheduledSeatRepository.findByRouteSchedule( routeSchedule );
    return scheduledSeats.stream()
            .map(scheduledSeat -> new ScheduledSeatResponse( scheduledSeat.getScheduledSeatId(), scheduledSeat.getSeatId(), scheduledSeat.getIsReserved()) )
            .toList();
}
```

ReservationService에서 getSeatsByRoute()는 하나의 시간대에 배정된 버스 내에서의 좌석 매진 여부를 가져오는 함수입니다.

이 함수에 캐시를 적용하게 된 이유는 다음과 같습니다.

- 시간대의 좌석 매진 여부 또한 자주 조회되는 항목 중 하나입니다.
- 마찬가지로 매번 쿼리를 날리는 것보단 캐시를 활용하여 시스템 성능을 향상시킬 수 있습니다.

## 캐시 무효화 분석

```java
@Component
public class ReservationCacheManager {

    @CacheEvict(
            cacheNames = CacheConfig.FIVE_MIN_CACHE,
            key = "'available-route-schedule'"
    )
    public void evictRouteScheduleCache() {}

    @CacheEvict(
            cacheNames = CacheConfig.ONE_MIN_CACHE,
            key = "'route-schedule-' + #routeScheduleId"
    )
    public void evictRouteScheduleCache(Long routeScheduleId) {}

}
```

캐시는 조회 시 성능을 향상 시켜주긴 하나, 가장 최신의 데이터를 가져오지 못할 수 있습니다. 즉 데이터 정합성을 보장하지 못합니다.
이는 현재 구현하고 있는 Cache 기법이 반드시 DB를 거치고 Redis의 값을 설정해주는 Look-Aside 기법이기 때문입니다. (데이터 정합성을 보장하는 것은 Read-Through)
따라서, 데이터가 변경될 때마다 캐시 값을 업데이트 시켜주는 작업이 필요합니다.

캐시에 값을 작성해주는 것은 `@Cacheable`이 대신 해주기 때문에 우리는 최신 데이터가 아닌 Redis 캐시를 지워주기만 하면됩니다.
이 역할은 `@CacheEvict`를 통해 구현할 수 있습니다. 앞서, `@Cacheable`을 통해서 작성해주었던 key값을 적어주면 되죠.

### 언제 캐시를 지워야 할까?

Evict를 적용하는 시점은 크게 두 가지로 고민해 볼 수 있었습니다.
첫 번째는 예약을 하는 시점, 두 번째는 결제를 하는 시점입니다. 처음에는 최종적으로 결제를 하는 시점에 캐시를 지울까 생각했었습니다.
하지만 그렇게 되면 예약하는 시점에는 최신 데이터를 유지하지 못합니다. 이에 따라서 중복 예약 신청을 요청할 가능성이 생겨버립니다. 서버 단에서는 나름대로 검증 로직을 통해서
에러를 반환할테지만 사용자 경험은 그리 좋지 못할 겁니다.

따라서 저는 예약을 하는 시점에 캐시를 지우기로 했습니다.

```java
@Transactional
public Reservation preserve(Long userId, Long routeScheduleId, List<Long> scheduleSeatIds ) {
    User user = userRepository.getByIdOrThrow( userId );

    List<ScheduledSeat> scheduledSeats = scheduleSeatIds.stream()
            .map( scheduledSeatRepository::findByIdWithPessimisticLock )
            .toList();

    scheduledSeats.forEach(
            ScheduledSeat::reserveSeat
    );

    Reservation reservation = Reservation.from( user, scheduledSeats );
    evictCacheFromRouteSchedule(routeScheduleId);
    return reservationRepository.save( reservation );
}

private void evictCacheFromRouteSchedule(Long routeScheduleId) {
    reservationCacheManager.evictRouteScheduleCache();
    reservationCacheManager.evictRouteScheduleCache(routeScheduleId);
}
```

`evictCacheFromRouteSchedule()`는 캐시 매니저를 통해서 앞서 저장해두었던 캐시 값들을 지워줍니다.
이로써, 중요한 상태 변경 시에는 항상 최신 정보를 보장하면서 불필요한 DB 접근을 차단하는 효과를 얻게되었습니다.


## 성능 개선 효과 분석

### 시간대 반환 함수

```
select
    r1_0.route_id,
    r1_0.arrival,
    r1_0.departure,
    r1_0.schedule_date
from
    route r1_0
where
    r1_0.departure=?
  and r1_0.arrival=?
  and r1_0.schedule_date=?
```
  
```
select
    rts1_0.route_route_id,
    rts1_1.route_time_slot_id,
    rts1_1.route_id,
    rts1_1.time_slot
from
    route_route_time_slots rts1_0
        join
    route_time_slot rts1_1
    on rts1_1.route_time_slot_id=rts1_0.route_time_slots_route_time_slot_id
where
    rts1_0.route_route_id=?
```

```    
select
    rs1_0.route_schedule_id,
    rs1_0.available_seats,
    rs1_0.bus,
    rs1_0.created_at,
    rs1_0.route_time_slot,
    rs1_0.sale_status,
    rs1_0.updated_at
from
    route_schedule rs1_0
where
    rs1_0.route_time_slot in (?, ?, ?, ?, ?, ?)
```

위 세개의 Select문은 시간대를 조회하기 위해서 발생하는 쿼리문입니다.

1. 출발지, 도착지, 날짜 별 조회
2. 해당 경로에서 시간대 조회
3. 해당 시간대의 부가정보를 알기 위한 조회

이렇게 3번이나 조회가 발생하는데 캐시를 도입함으로써 데이터베이스 부하를 크게 감소시킬 수 있습니다.

### 좌석 조회

```
select
    rs1_0.route_schedule_id,
    rs1_0.available_seats,
    rs1_0.bus,
    rs1_0.created_at,
    rts1_0.route_time_slot_id,
    rts1_0.route_id,
    rts1_0.time_slot,
    rs1_0.sale_status,
    rs1_0.updated_at 
from
    route_schedule rs1_0 
left join
    route_time_slot rts1_0 
        on rts1_0.route_time_slot_id=rs1_0.route_time_slot 
where
    rs1_0.route_schedule_id=?
```

```
select
    ss1_0.scheduled_seat_id,
    ss1_0.created_at,
    ss1_0.is_reserved,
    ss1_0.reservation_reservation_id,
    ss1_0.route_schedule_route_schedule_id,
    ss1_0.seat_id,
    ss1_0.seat_price,
    ss1_0.updated_at 
from
    scheduled_seat ss1_0 
where
    ss1_0.route_schedule_route_schedule_id=?
```

좌석 조회도 마찬가지로 2번의 쿼리와 조인문 등으로 복잡성이 큽니다.
하지만, 캐시된 데이터를 사용함으로써 데이터베이스 부하를 줄일 수 있습니다.


## 결론

1. 데이터베이스 부하 감소
2. 응답 시간 감축
3. 데이터 일관성 유지
4. 실시간성 유지
