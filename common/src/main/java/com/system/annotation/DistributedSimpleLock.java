package com.system.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedSimpleLock {
    String key();
    long waitTime() default 5;
    long releaseTime() default 10;
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
