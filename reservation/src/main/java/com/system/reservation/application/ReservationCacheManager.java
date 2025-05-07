package com.system.reservation.application;

import com.system.config.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

@Component
public class ReservationCacheManager {

    @CacheEvict(
            key = "'available-route-schedule'"
    )
    public void evictRouteScheduleCache() {}

    @CacheEvict(
            cacheNames = CacheConfig.ONE_MIN_CACHE,
            key = "'route-schedule-' + #routeScheduleId"
    )
    public void evictRouteScheduleCache(Long routeScheduleId) {}

}
