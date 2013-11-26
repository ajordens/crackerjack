package com.littlesquare.crackerjack.services.common.camel

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.sns.AmazonSNS
import com.amazonaws.services.sns.AmazonSNSClient
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
    private String awsFile = "aws.properties"

    @NotNull
    @JsonProperty
    private String awsAccessKey = ""

    @NotNull
    @JsonProperty
    private String awsSecretKey = ""

    AmazonSQS buildSQSClient() {
        return new AmazonSQSClient(getAWSCredentials())
    }

    AmazonSNS buildSNSClient() {
        return new AmazonSNSClient(getAWSCredentials())
    }

    private getAWSCredentials() {
        def aws = this.class.classLoader.getResourceAsStream(awsFile)
        if ((!awsAccessKey || !awsSecretKey) && aws) {
            def properties = new Properties()
            properties.load(aws)

            awsAccessKey = properties.getProperty("awsAccessKey")
            awsSecretKey = properties.getProperty("awsSecretKey")
        }
        return new BasicAWSCredentials(awsAccessKey, awsSecretKey)
    }
}