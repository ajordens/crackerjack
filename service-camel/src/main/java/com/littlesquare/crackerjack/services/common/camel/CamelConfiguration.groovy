package com.littlesquare.crackerjack.services.common.camel

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSClient
import com.fasterxml.jackson.annotation.JsonProperty

import javax.validation.constraints.NotNull

/**
 * @author Adam Jordens (adam@jordens.org)
 */
class CamelConfiguration {
    @NotNull
    @JsonProperty
    private String awsAccessKey = ""

    @NotNull
    @JsonProperty
    private String awsSecretKey = ""

    AmazonSQS buildSQSClient() {
        return new AmazonSQSClient(new BasicAWSCredentials(awsAccessKey, awsSecretKey))
    }
}