package com.littlesquare.crackerjack.services.example.resources

import com.wordnik.swagger.annotations.Api
import com.wordnik.swagger.annotations.ApiOperation
import com.wordnik.swagger.annotations.ApiParam
import com.wordnik.swagger.annotations.ApiResponse
import com.wordnik.swagger.annotations.ApiResponses
import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.apache.camel.ProducerTemplate

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.MediaType

/**
 * @author Adam Jordens (adam@jordens.org)
 */
@Path("/api/hello")
@Api(value = "/api/hello", description = "Helllooooo World!")
@Produces(MediaType.APPLICATION_JSON)
class HelloWorldApiResource {
    private final ProducerTemplate producerTemplate

    HelloWorldApiResource(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate
    }

    @GET
    @ApiOperation(value = "Hello world", notes = "Hey! It's the hello world endpoint.", response = String.class)
    @ApiResponses(value = [
    @ApiResponse(code = 400, message = "Invalid 'name' supplied")
    ])
    String get(
            @ApiParam(value = "Name", required = true) @QueryParam("name") String name
    ) {
        if (!name.equalsIgnoreCase("Bob")) {
            producerTemplate.send("direct:aws-sns-example", new Processor() {
                public void process(Exchange outExchange) {
                    outExchange.getIn().setBody("Hello ${name} (SNS)")
                }
            })
            producerTemplate.send("direct:aws-sqs-example", new Processor() {
                public void process(Exchange outExchange) {
                    outExchange.getIn().setBody("Hello ${name} (SQS)")
                }
            })
            return "Hello ${name}"
        }

        throw new WebApplicationException(400)
    }
}
