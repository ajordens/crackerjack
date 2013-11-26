package com.littlesquare.crackerjack.services.example.extras

import org.apache.camel.Exchange
import org.apache.camel.LoggingLevel
import org.apache.camel.Processor
import org.apache.camel.builder.RouteBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Adam Jordens (adam@jordens.org)
 */
class ExampleSqsRouteBuilder extends RouteBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(ExampleSqsRouteBuilder)
    private final String queueName

    public ExampleSqsRouteBuilder(String queueName) {
        this.queueName = queueName
    }

    @Override
    void configure() throws Exception {
        errorHandler(
                deadLetterChannel("aws-sqs://DLQ${queueName}?amazonSQSClient=#sqsClient")
                        .maximumRedeliveries(3)
                        .redeliveryDelay(5000)
                        .retryAttemptedLogLevel(LoggingLevel.ERROR)
                        .onRedelivery(new Processor() {
                    @Override
                    void process(Exchange exchange) throws Exception {
                        LOG.error("Re-processing message: ${exchange.getIn().getBody()}")
                    }
                })
        )

        from("aws-sqs://${queueName}?amazonSQSClient=#sqsClient&deleteAfterRead=true&maxMessagesPerPoll=5")
                .process(new Processor() {
                    @Override
                    void process(Exchange exchange) throws Exception {
                        LOG.info("Received Message: ${exchange.getIn().getBody()}")
                    }
                })

        from("direct:aws-sqs-example")
                .to("aws-sqs://${queueName}?amazonSQSClient=#sqsClient")
    }
}