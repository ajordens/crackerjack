package com.littlesquare.crackerjack.services.example.resources

import com.littlesquare.crackerjack.services.ui.views.BaseViewModel
import io.dropwizard.views.View

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * @author Adam Jordens (adam@jordens.org)
 */
@Path("/hello")
@Produces(MediaType.TEXT_HTML)
public class HelloWorldResource {
    private final ExecutorService executorService = Executors.newCachedThreadPool()

    public HelloWorldApiResource() {
    }

    @GET
    public View get(@QueryParam("name") String name) {
        return new BaseViewModel("/ftl/helloworld.ftl", [
                name: name ?: "World",
                nested: [
                        value: "Example of a nested value."
                ],
                "future1": [call: {
                    (1..10).each { Thread.sleep(100) }
                    return "future1"
                }] as Callable,
                "future2": [call: {
                    (1..2).each { Thread.sleep(400) }
                    return "future2"
                }] as Callable
        ], executorService)
    }
}
