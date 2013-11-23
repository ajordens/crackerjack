package com.littlesquare.crackerjack.services.common

import io.dropwizard.Configuration
import io.dropwizard.db.DataSourceFactory
import com.fasterxml.jackson.annotation.JsonProperty
import io.dropwizard.client.JerseyClientConfiguration
import io.dropwizard.db.DatabaseConfiguration

import javax.validation.Valid
import javax.validation.constraints.NotNull

/**
 * @author Adam Jordens (adam@jordens.org)
 */
class ServiceConfiguration extends Configuration {
    @Valid
    @NotNull
    @JsonProperty
    DataSourceFactory database = new DataSourceFactory()

    @Valid
    @NotNull
    @JsonProperty
    JerseyClientConfiguration jerseyClient = new JerseyClientConfiguration()

    public DataSourceFactory getDataSourceFactory() {
        return database;
    }
}