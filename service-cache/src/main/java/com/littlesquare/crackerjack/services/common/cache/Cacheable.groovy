package com.littlesquare.crackerjack.services.common.cache

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target
import java.util.concurrent.TimeUnit

/**
 * @author Adam Jordens (adam@jordens.org)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Cacheable {
    String cacheName()
    long timeToLive() default 0L
    TimeUnit timeUnit() default TimeUnit.SECONDS
    String[] cacheKeyRefs()
}
