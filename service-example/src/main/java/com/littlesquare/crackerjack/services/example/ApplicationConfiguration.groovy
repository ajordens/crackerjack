package com.littlesquare.crackerjack.services.example

import com.fasterxml.jackson.annotation.JsonProperty
import com.littlesquare.crackerjack.services.common.ServiceConfiguration
import com.littlesquare.crackerjack.services.common.camel.CamelConfiguration
import com.littlesquare.crackerjack.services.common.throttling.ThrottleConfiguration

import javax.validation.Valid
import javax.validation.constraints.NotNull

/**
 * @author Adam Jordens (adam@jordens.org)
 */
class ApplicationConfiguration extends ServiceConfiguration {
    @Valid
    @NotNull
    @JsonProperty
    CamelConfiguration camel = new CamelConfiguration()

    @Valid
    @NotNull
    @JsonProperty
    ThrottleConfiguration throttle = new ThrottleConfiguration()
}