package com.littlesquare.crackerjack.services.common

import com.littlesquare.crackerjack.services.common.config.ClasspathPropertiesFileDataSourceFactory
import io.dropwizard.Configuration
import io.dropwizard.db.DataSourceFactory
import com.fasterxml.jackson.annotation.JsonProperty
import io.dropwizard.client.JerseyClientConfiguration

import javax.validation.Valid
import javax.validation.constraints.NotNull

/**
 * @author Adam Jordens (adam@jordens.org)
 */
class ServiceConfiguration extends Configuration {
    @Valid
    @NotNull
    @JsonProperty
    ClasspathPropertiesFileDataSourceFactory database = new ClasspathPropertiesFileDataSourceFactory()

    @Valid
    @NotNull
    @JsonProperty
    JerseyClientConfiguration jerseyClient = new JerseyClientConfiguration()
}