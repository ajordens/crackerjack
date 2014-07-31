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

        def hazelcastInstance = hazelcastBundle.getHazelcastInstance()

        def cacheMap = hazelcastInstance.getMap("Demo")
        def random = new Random(System.currentTimeMillis())
        (1..25).each {
            cacheMap.put(random.nextInt(10000), random.nextInt(10000))
            Thread.sleep(1000)
            LOG.info("Iteration #${it} of adding to the shared hazelcast map ('Demo')")
        }

        LOG.info("""
Cache      : {}
Cache Size : {}
... state will be backed up when multiple cache check commands are running simultaneously
""", cacheMap.entrySet(), cacheMap.size())
    }
}