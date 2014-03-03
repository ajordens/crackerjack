package com.littlesquare.crackerjack.services.common.throttling

import com.fasterxml.jackson.annotation.JsonProperty

import javax.validation.constraints.NotNull

/**
 * @author Adam Jordens (adam@jordens.org)
 */
public class ThrottleConfiguration {
    @NotNull
    @JsonProperty
    String type = RateLimiterThrottle.class.simpleName

    @NotNull
    @JsonProperty
    List<ThrottleContext> contexts

    public Throttle buildThrottle() {
        switch (type) {
            case RateLimiterThrottle.class.simpleName:
                return new RateLimiterThrottle(contexts)
            default:
                throw new IllegalArgumentException("Unsupported throttle type (${type})")
        }
    }
}
