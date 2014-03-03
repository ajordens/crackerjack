package com.littlesquare.crackerjack.services.common.throttling

/**
 * A Throttle implementation using Guava's RateLimiter
 * - Calls to check() will return immediately and not *block* until a permit is available.
 *
 * @author Adam Jordens (adam@jordens.org)
 */
public class RateLimiterThrottle implements Throttle {
    private final List<ThrottleContext> configuredContexts

    public RateLimiterThrottle(List<ThrottleContext> configuredContexts) {
        this.configuredContexts = new ArrayList<ThrottleContext>(configuredContexts).sort().reverse()
    }

    @Override
    public boolean check(ThrottleContext throttleContext) {
        def matchingThrottleContext = findMatchingThrottleContext(throttleContext)
        if (!matchingThrottleContext) {
            return false
        }

        return matchingThrottleContext.rateLimiter.tryAcquire(1)
    }

    ThrottleContext findMatchingThrottleContext(ThrottleContext throttleContext) {
        return configuredContexts.find {
            it.matches(throttleContext)
        }
    }
}
