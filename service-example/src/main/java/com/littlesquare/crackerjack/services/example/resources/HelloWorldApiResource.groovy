package com.littlesquare.crackerjack.services.example.resources

import com.littlesquare.crackerjack.services.common.cache.Cacheable

import javax.servlet.http.HttpServletRequest
import javax.ws.rs.DefaultValue
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import java.util.concurrent.TimeUnit

/**
 * @author Adam Jordens (adam@jordens.org)
 */
@Path("/api/hello")
@Produces(MediaType.APPLICATION_JSON)
public interface HelloWorldApiResource {
    @GET
    @Cacheable(cacheName = "HelloWorld", cacheKeyRefs = ["GET", "{0}"], timeToLive = 10L, timeUnit = TimeUnit.SECONDS)
    public String get(@DefaultValue("Default Value") @QueryParam("name") String name,
                      @Context HttpServletRequest httpServletRequest)
}

