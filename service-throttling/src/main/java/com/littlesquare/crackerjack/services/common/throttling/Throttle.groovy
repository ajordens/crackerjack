package com.littlesquare.crackerjack.services.common.throttling

/**
 * @author Adam Jordens (adam@jordens.org)
 */
public interface Throttle {
    boolean check(ThrottleContext throttleContext)
}
