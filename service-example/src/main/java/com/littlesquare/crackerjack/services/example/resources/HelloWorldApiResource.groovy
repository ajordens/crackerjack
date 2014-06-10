package com.littlesquare.crackerjack.services.example.resources

import com.littlesquare.crackerjack.services.common.throttling.Throttle
import com.littlesquare.crackerjack.services.common.throttling.ThrottleContext
import org.apache.camel.ProducerTemplate

import javax.servlet.http.HttpServletRequest
import javax.ws.rs.DefaultValue
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType

/**
 * @author Adam Jordens (adam@jordens.org)
 */
@Path("/api/hello")
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldApiResource {
    private final Throttle throttle

    public HelloWorldApiResource(Throttle throttle) {
        this.throttle = throttle
    }

    @GET
    public String get(@DefaultValue("Default Value") @QueryParam("name") String name,
                      @Context HttpServletRequest httpServletRequest) {
        if (!throttle.check(new ThrottleContext(
                servletPath: httpServletRequest.requestURI,
                method: httpServletRequest.method,
                ipAddress: httpServletRequest.remoteAddr
        ))) {
            throw new WebApplicationException(429)
        }

        return "Hello ${name}"
    }
}
