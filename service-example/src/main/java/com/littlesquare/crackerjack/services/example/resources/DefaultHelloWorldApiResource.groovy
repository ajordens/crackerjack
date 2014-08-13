package com.littlesquare.crackerjack.services.example.resources

import com.littlesquare.crackerjack.services.common.throttling.Throttle
import com.littlesquare.crackerjack.services.common.throttling.ThrottleContext
import com.littlesquare.crackerjack.services.example.core.HelloWorld

import javax.servlet.http.HttpServletRequest
import javax.ws.rs.WebApplicationException

/**
 * @author Adam Jordens (adam@jordens.org)
 */
public class DefaultHelloWorldApiResource implements HelloWorldApiResource {
    private final Throttle throttle

    public DefaultHelloWorldApiResource(Throttle throttle) {
        this.throttle = throttle
    }

    @Override
    public HelloWorld get(String name, HttpServletRequest httpServletRequest) {
        if (!throttle.check(new ThrottleContext(
                servletPath: httpServletRequest.requestURI,
                method: httpServletRequest.method,
                ipAddress: httpServletRequest.remoteAddr
        ))) {
            throw new WebApplicationException(429)
        }

        return new HelloWorld(message: "Hello ${name}")
    }
}
