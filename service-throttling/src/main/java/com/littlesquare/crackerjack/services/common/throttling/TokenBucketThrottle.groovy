package com.littlesquare.crackerjack.services.common.throttling

/**
 * @author Adam Jordens (adam@jordens.org)
 */
public class TokenBucketThrottle implements Throttle {
    private final List<ThrottleContext> configuredContexts

    public TokenBucketThrottle(List<ThrottleContext> configuredContexts) {
        this.configuredContexts = new ArrayList<ThrottleContext>(configuredContexts).sort().reverse()
    }

    @Override
    boolean check(ThrottleContext throttleContext) {
        def matchingThrottleContext = findMatchingThrottleContext(throttleContext)
        if (!matchingThrottleContext) {
            return false
        }

        // TODO: Introduce a rateLimiter interface and use that in ThrottleContext... specify it in the constructor.

        matchingThrottleContext.rateLimiter.acquire(1)
        return true
    }

    private ThrottleContext findMatchingThrottleContext(ThrottleContext throttleContext) {
        return configuredContexts.find {
            it.matches(throttleContext)
        }
    }
}
