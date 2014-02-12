package com.littlesquare.crackerjack.services.common.throttling

import org.junit.Test

import static groovyx.gpars.GParsPool.withPool

/**
 * @author Adam Jordens (adam@jordens.org)
 */
public class RateLimiterThrottleTest {
    private final List<ThrottleContext> throttleContexts = [
            new ThrottleContext(username: ".*", servletPath: ".*", ipAddress: ".*", allowedThroughput: 1.0),
            new ThrottleContext(username: "a3", servletPath: "/path", ipAddress: "127.0.0.1", allowedThroughput: 10.0),
            new ThrottleContext(username: "a1", servletPath: ".*", ipAddress: ".*", allowedThroughput: 10.0),
            new ThrottleContext(username: "a2", servletPath: "/path", ipAddress: ".*", allowedThroughput: 10.0)
    ]
    private final RateLimiterThrottle throttle = new RateLimiterThrottle(throttleContexts)

    /**
     * The RateLimiterThrottle does not actually reject requests, it simply blocks until a permit becomes available.
     */
    @Test
    void check() {
        assert throttle.check(throttleContexts[0])
        assert throttle.check(throttleContexts[0])
        assert throttle.check(throttleContexts[0])

        withPool {
            (1..3).eachParallel {
                assert throttle.check(throttleContexts[1])
            }
        }
    }

    @Test
    void findMatchingThrottleContext() {
        assert throttle.findMatchingThrottleContext(new ThrottleContext(
                username: "a1", servletPath: "/path", ipAddress: "0.0.0.0"
        )) == throttleContexts[2]

        assert throttle.findMatchingThrottleContext(new ThrottleContext(
                username: "missing", servletPath: "missing", ipAddress: "missing"
        )) == throttleContexts[0]

        assert throttle.findMatchingThrottleContext(new ThrottleContext()) == throttleContexts[0]

        throttleContexts.each {
            assert throttle.findMatchingThrottleContext(it) == it
        }
    }

    @Test
    void throttleContext__Sort() {
        (1..50).each {
            def shuffledContexts = new ArrayList<ThrottleContext>(throttleContexts)
            Collections.shuffle(shuffledContexts)
            assert shuffledContexts.sort().reverse()*.username == [
                    "a3", "a2", "a1", ".*"
            ]
        }
    }

    @Test
    void throttleContext__CompareTo() {
        def t1 = new ThrottleContext(username: "a3", servletPath: "/path", ipAddress: "0.0.0.0")
        def t2 = new ThrottleContext(username: "a3", servletPath: "/path", ipAddress: "0.0.0.0")
        assert t1.compareTo(t2) == 0

        t1 = new ThrottleContext(username: "a4", servletPath: "/path", ipAddress: "0.0.0.0")
        t2 = new ThrottleContext(username: "a3", servletPath: "/path", ipAddress: "0.0.0.0")
        assert t1.compareTo(t2) == 1
        assert [t1,t2].sort().reverse()*.username == ["a4", "a3"]

        t1 = new ThrottleContext(username: "a3", servletPath: "/pat", ipAddress: "0.0.0.0")
        t2 = new ThrottleContext(username: "a3", servletPath: "/path", ipAddress: "0.0.0.0")
        assert t1.compareTo(t2) == -1
        assert [t1,t2].sort().reverse()*.servletPath == ["/path", "/pat"]

        t1 = new ThrottleContext(username: "a3", servletPath: "/path", ipAddress: "0.0.0.1")
        t2 = new ThrottleContext(username: "a3", servletPath: "/path", ipAddress: "1.1.1.1")
        assert t1.compareTo(t2) == -1
        assert [t1,t2].sort().reverse()*.ipAddress == ["1.1.1.1", "0.0.0.1"]
    }
}
