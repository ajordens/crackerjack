package com.littlesquare.crackerjack.services.common.throttling

/**
 * A Throttle implementation using Guava's RateLimiter.
 * - Calls to check() will *block* until a permit is available.
 *
 * TODO: Add metrics support to keep track of amount of time spent blocking, should we switch to tryAcquire() w/ appropriate timeout
 *
 * @author Adam Jordens (adam@jordens.org)
 */
public class RateLimiterThrottle implements Throttle {
    private final List<ThrottleContext> configuredContexts

    public RateLimiterThrottle(List<ThrottleContext> configuredContexts) {
        this.configuredContexts = new ArrayList<ThrottleContext>(configuredContexts).sort().reverse()
    }

    @Override
    boolean check(ThrottleContext throttleContext) {
        def matchingThrottleContext = findMatchingThrottleContext(throttleContext)
        if (!matchingThrottleContext) {
            return false
        }

        matchingThrottleContext.rateLimiter.acquire(1)
        return true
    }

    private ThrottleContext findMatchingThrottleContext(ThrottleContext throttleContext) {
        return configuredContexts.find {
            it.matches(throttleContext)
        }
    }
}
