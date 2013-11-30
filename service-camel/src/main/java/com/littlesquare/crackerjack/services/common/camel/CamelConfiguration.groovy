package com.littlesquare.crackerjack.services.common.camel

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider
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

    private AWSCredentials getAWSCredentials() {
        if (!awsAccessKey || !awsSecretKey) {
            def credentialsProvider = new ClasspathPropertiesFileCredentialsProvider()
            def credentials = credentialsProvider.getCredentials()

            awsAccessKey = credentials.getAWSAccessKeyId()
            awsSecretKey = credentials.getAWSSecretKey()
        }
        return new BasicAWSCredentials(awsAccessKey, awsSecretKey)
    }
}