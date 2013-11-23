package com.littlesquare.crackerjack.services.example.resources

import com.wordnik.swagger.annotations.Api
import com.wordnik.swagger.annotations.ApiOperation
import com.wordnik.swagger.annotations.ApiResponse
import com.wordnik.swagger.annotations.ApiResponses

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

/**
 * @author Adam Jordens (adam@jordens.org)
 */
@Path("/api/hello")
@Api(value = "/api/hello", description = "Operations about hello world!")
@Produces(MediaType.APPLICATION_JSON)
class HelloWorldApiResource {
    @GET
    @ApiOperation(value = "Find pet by ID", notes = "More notes about this method", response = String.class)
    @ApiResponses(value = [
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "Pet not found")
    ])
    String get() {
        return "Hello World"
    }
}
