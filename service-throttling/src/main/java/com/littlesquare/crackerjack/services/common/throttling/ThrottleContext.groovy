package com.littlesquare.crackerjack.services.common.throttling

import com.google.common.util.concurrent.RateLimiter

import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

/**
 * @author Adam Jordens (adam@jordens.org)
 */
public class ThrottleContext implements Comparable<ThrottleContext> {
    String username = ".*"
    String servletPath = ".*"
    String method = ".*"
    String ipAddress = ".*"

    RateLimiter rateLimiter = RateLimiter.create(1)
    private Pattern pattern = null

    public void setAllowedThroughput(Double allowedThroughput) {
        this.rateLimiter = RateLimiter.create(allowedThroughput)
    }

    public boolean matches(ThrottleContext throttleContext) {
        if (!pattern) {
            pattern = Pattern.compile(toString())
        }
        return pattern.matcher(throttleContext.toString()).matches()
    }


    @Override
    int compareTo(ThrottleContext o) {
        if ((username == ".*" && o.username != ".*") ||
                (method == ".*" && o.method != ".*") ||
                (servletPath == ".*" && o.servletPath != ".*") ||
                (ipAddress == ".*" && o.ipAddress != ".*")) {
            return -1
        } else if ((username != ".*" && o.username == ".*") ||
                (method != ".*" && o.method == ".*") ||
                (servletPath != ".*" && o.servletPath == ".*") ||
                (ipAddress != ".*" && o.ipAddress == ".*")) {
            return 1
        }

        def compareTo = 0
        ["username", "method", "servletPath", "ipAddress"].each {
            compareTo = compareTo ?: this."${it}" <=> o."${it}"
        }
        return compareTo
    }


    @Override
    public String toString() {
        return [username, method, servletPath, ipAddress].join("__")
    }
}
