package com.example.reservationsystem.common.infra.logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Aspect
@Component
public class MethodExecutionLogger {

    private static final Logger log = LoggerFactory.getLogger(MethodExecutionLogger.class);
    private final ThreadLocal<Integer> depth = ThreadLocal.withInitial(() -> 0);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Around("execution(* com.example.reservationsystem.reservation..*(..))")
    public Object logExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        int currentDepth = depth.get();
        depth.set(currentDepth + 1);

        String indent = "  ".repeat(currentDepth);
        String methodName = joinPoint.getSignature().toShortString();
        String threadName = Thread.currentThread().getName();
        String startTime = LocalDateTime.now().format(formatter);

        // 시작 로그
        log.info("");
        log.info("{}━━━━━━━━━━ START BLOCK ━━━━━━━━━━", indent);
        log.info("[{}] [{}] {}▶ START --- {}", startTime, threadName, indent, methodName);

        long start = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {
            long duration = System.currentTimeMillis() - start;
            String endTime = LocalDateTime.now().format(formatter);

            log.info("[{}] [{}] {}◀ END   --- {} took {} ms", endTime, threadName, indent, methodName, duration);
            log.info("{}━━━━━━━━━━ END BLOCK ━━━━━━━━━━", indent);
            log.info("");

            depth.set(currentDepth);
        }
    }

}

