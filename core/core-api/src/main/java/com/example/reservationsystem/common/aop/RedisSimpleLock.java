package com.example.reservationsystem.common.aop;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

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
