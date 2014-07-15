package com.littlesquare.crackerjack.services.example.cli

import com.littlesquare.crackerjack.services.common.cache.hazelcast.HazelcastBundle
import com.littlesquare.crackerjack.services.example.ApplicationConfiguration
import io.dropwizard.cli.ConfiguredCommand
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import net.sourceforge.argparse4j.inf.Namespace
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Adam Jordens (adam@jordens.org)
 */
public class CacheCheckCommand extends ConfiguredCommand<ApplicationConfiguration> {
    private static final Logger LOG = LoggerFactory.getLogger(CacheCheckCommand.class);

    public CacheCheckCommand() {
        super("cache-check", "Connect to and verify Hazelcast cache configuration")
    }

    @Override
    protected void run(Bootstrap<ApplicationConfiguration> bootstrap,
                       Namespace namespace,
                       ApplicationConfiguration configuration) throws Exception {
        Environment environment = new Environment(bootstrap.getApplication().getName(),
                bootstrap.getObjectMapper(),
                bootstrap.getValidatorFactory().getValidator(),
                bootstrap.getMetricRegistry(),
                bootstrap.getClassLoader())

        def hazelcastBundle = new HazelcastBundle()
        hazelcastBundle.run(configuration, environment)

        def hazelcastInstance = hazelcastBundle.hazelcastInstance
        println hazelcastInstance.getMap("HelloWorld")

        System.exit(0)
    }
}