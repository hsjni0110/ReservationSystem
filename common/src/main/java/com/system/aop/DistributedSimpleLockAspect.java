package com.system.aop;

import com.system.annotation.DistributedSimpleLock;
import com.system.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;

import static com.system.exception.BusinessExceptionType.OTHER_THREAD_ASSIGNED;

@Aspect
@Component
@Slf4j
public class DistributedSimpleLockAspect {

    private final RedisSimpleLock redisSimpleLock;
    private final SpelExpressionParser parser = new SpelExpressionParser();
    private final ParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    public DistributedSimpleLockAspect(RedisSimpleLock redisSimpleLock) {
        this.redisSimpleLock = redisSimpleLock;
    }

    @Around("@annotation(distributedSimpleLock)")
    public Object around(ProceedingJoinPoint joinPoint, DistributedSimpleLock distributedSimpleLock) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        EvaluationContext context = new StandardEvaluationContext();
        String[] paramNames = nameDiscoverer.getParameterNames(method);
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < Objects.requireNonNull(paramNames).length; i++) {
            context.setVariable(paramNames[i], args[i]);
        }

        String evaluatedKey = parser.parseExpression(distributedSimpleLock.key()).getValue(context, String.class);

        assert evaluatedKey != null;
        if (evaluatedKey.contains("[") && evaluatedKey.contains("]")) {
            return proceedWithMultiLock(evaluatedKey, distributedSimpleLock, joinPoint);
        } else {
            String lockValue = UUID.randomUUID().toString();
            try {
                boolean acquired = redisSimpleLock.tryLock(
                        evaluatedKey,
                        lockValue,
                        distributedSimpleLock.releaseTime(),
                        distributedSimpleLock.timeUnit()
                );
                if (!acquired) {
                    throw new BusinessException( OTHER_THREAD_ASSIGNED );
                }
                return joinPoint.proceed();
            } finally {
                redisSimpleLock.releaseLock(evaluatedKey, lockValue);
            }
        }
    }

    private Object proceedWithMultiLock(String evaluatedKey, DistributedSimpleLock lockAnnotation, ProceedingJoinPoint joinPoint) throws Throwable {
        // 예: reservation:1234:[1,2,3]
        // → prefix = reservation:1234
        // → ids = [1,2,3]

        int listStart = evaluatedKey.indexOf('[');
        String prefix = evaluatedKey.substring(0, listStart);
        String rawList = evaluatedKey.substring(listStart)
                .replace("[", "")
                .replace("]", "")
                .replace(" ", "");
        List<String> seatIds = Arrays.asList(rawList.split(","));

        List<String> lockKeys = seatIds.stream()
                .map(id -> prefix + ":" + id)
                .sorted()
                .toList();

        String lockValue = UUID.randomUUID().toString();
        List<String> acquiredKeys = new ArrayList<>();

        try {
            for (String key : lockKeys) {
                boolean success = redisSimpleLock.tryLock(
                        key,
                        lockValue,
                        lockAnnotation.releaseTime(),
                        lockAnnotation.timeUnit()
                );
                if (!success) {
                    throw new BusinessException( OTHER_THREAD_ASSIGNED );
                }
                acquiredKeys.add(key);
            }
            return joinPoint.proceed();
        } finally {
            long releaseStart = System.currentTimeMillis();
            for (String key : acquiredKeys) {
                redisSimpleLock.releaseLock(key, lockValue);
                log.info("Lock released: {}", key);
            }
            log.info("🔓 Lock release took {} ms", System.currentTimeMillis() - releaseStart);
        }
    }
}