package com.littlesquare.crackerjack.services.example.extras

import org.apache.camel.builder.RouteBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Adam Jordens (adam@jordens.org)
 */
class ExampleSnsRouteBuilder extends RouteBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(ExampleSnsRouteBuilder)
    private final String topicName

    public ExampleSnsRouteBuilder(String topicName) {
        this.topicName = topicName
    }

    @Override
    void configure() throws Exception {
        from("direct:aws-sns-example")
        .to("aws-sns://${topicName}?amazonSNSClient=#snsClient")
    }
}