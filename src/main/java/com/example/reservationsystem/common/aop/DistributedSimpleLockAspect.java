package com.example.reservationsystem.common.aop;

import com.example.reservationsystem.common.annotation.DistributedSimpleLock;
import com.example.reservationsystem.common.exception.BusinessException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.UUID;

import static com.example.reservationsystem.common.exception.BusinessExceptionType.OTHER_THREAD_ASSIGNED;

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
