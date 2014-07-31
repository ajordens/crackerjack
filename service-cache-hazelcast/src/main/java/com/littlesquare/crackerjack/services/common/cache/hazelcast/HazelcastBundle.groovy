package com.littlesquare.crackerjack.services.common.cache.hazelcast

import com.hazelcast.config.Config
import com.hazelcast.core.Hazelcast
import com.hazelcast.core.HazelcastInstance
import io.dropwizard.ConfiguredBundle
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment

/**
 * The HazelcastBundle manages the configuration and creation of a HazelcastInstance within the normal lifecycle of a
 * Dropwizard bundle.
 *
 * @author Adam Jordens (adam@jordens.org)
 */
public class HazelcastBundle implements ConfiguredBundle<HazelcastServiceConfiguration> {
    final Config configuration = new Config()
    HazelcastInstance hazelcastInstance = null

    @Override
    public void initialize(Bootstrap bootstrap) {
        // do nothing
    }

    @Override
    public void run(HazelcastServiceConfiguration hazelcastServiceConfiguration, Environment environment) throws Exception {
        def hazelcastConfiguration = hazelcastServiceConfiguration.hazelcastConfiguration

        def networkConfig = configuration.getNetworkConfig()
        networkConfig.getJoin().getMulticastConfig().setMulticastGroup(hazelcastConfiguration.multicastGroup)
        networkConfig.getInterfaces().setInterfaces(hazelcastConfiguration.interfaces)
        networkConfig.getInterfaces().setEnabled(true)
    }

    /**
     * The current HazelcastInstance (if available), otherwise one will be created using the current 'configuration'.
     *
     * <pre>
     * {@code
     * HazelcastBundle hazelcastBundle = new HazelcastBundle()
     * hazelcastBundle.run(configuration, environment)
     * hazelcastBundle.configuration.getMapConfig("HelloWorld").setTimeToLiveSeconds(5)
     *
     * HazelcastInstance hazelcastInstance = hazelcastBundle.getHazelcastInstance()
     * }
     * </pre>
     *
     * @return HazelcastInstance
     */
    public HazelcastInstance getHazelcastInstance() {
        hazelcastInstance = hazelcastInstance ?: Hazelcast.newHazelcastInstance(configuration)
        return hazelcastInstance
    }
}
