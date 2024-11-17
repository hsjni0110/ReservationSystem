package com.example.reservationsystem.reservation.domain.manager;

import com.example.reservationsystem.common.config.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

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
