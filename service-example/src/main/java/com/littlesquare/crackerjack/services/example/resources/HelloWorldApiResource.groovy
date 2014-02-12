package com.littlesquare.crackerjack.services.example.resources

import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.apache.camel.ProducerTemplate

import javax.ws.rs.DefaultValue
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
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldApiResource {
    private final ProducerTemplate producerTemplate

    public HelloWorldApiResource(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate
    }

    @GET
    public String get(@DefaultValue("Default Value") @QueryParam("name") String name) {
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
