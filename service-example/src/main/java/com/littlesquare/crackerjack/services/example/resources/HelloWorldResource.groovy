package com.littlesquare.crackerjack.services.example.resources

import com.littlesquare.crackerjack.services.ui.views.BaseViewModel
import io.dropwizard.views.View

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType

/**
 * @author Adam Jordens (adam@jordens.org)
 */
@Path("/hello")
@Produces(MediaType.TEXT_HTML)
public class HelloWorldResource {
    public HelloWorldApiResource() {
    }

    @GET
    public View get(@QueryParam("name") String name) {
        return new BaseViewModel("/ftl/helloworld.ftl", [
                name: name ?: "World",
                nested: [
                        value: "Example of a nested value."
                ]
        ])
    }
}
