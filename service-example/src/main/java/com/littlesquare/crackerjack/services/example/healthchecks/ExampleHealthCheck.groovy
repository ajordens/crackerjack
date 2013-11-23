package com.littlesquare.crackerjack.services.example.healthchecks

import com.codahale.metrics.health.HealthCheck
import static com.codahale.metrics.health.HealthCheck.*

/**
 * @author Adam Jordens (adam@jordens.org)
 */
class ExampleHealthCheck extends HealthCheck {
    @Override
    protected Result check() throws Exception {
        return Result.healthy()
    }
}
