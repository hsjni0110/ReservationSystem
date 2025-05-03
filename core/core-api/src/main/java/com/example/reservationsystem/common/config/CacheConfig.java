package com.example.reservationsystem.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

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
