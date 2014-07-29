package com.littlesquare.crackerjack.services.common.cache.hazelcast

import com.hazelcast.config.Config
import com.hazelcast.core.Hazelcast
import com.hazelcast.core.HazelcastInstance
import io.dropwizard.ConfiguredBundle
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment

/**
 * @author Adam Jordens (adam@jordens.org)
 */
public class HazelcastBundle implements ConfiguredBundle<HazelcastServiceConfiguration> {
    HazelcastInstance hazelcastInstance = null

    @Override
    void initialize(Bootstrap bootstrap) {
        // do nothing
    }

    @Override
    void run(HazelcastServiceConfiguration hazelcastServiceConfiguration, Environment environment) throws Exception {
        def hazelcastConfiguration = hazelcastServiceConfiguration.hazelcastConfiguration

        def config = new Config()

        def networkConfig = config.getNetworkConfig()
        networkConfig.getJoin().getMulticastConfig().setMulticastGroup(hazelcastConfiguration.multicastGroup)
        networkConfig.getInterfaces().setInterfaces(hazelcastConfiguration.interfaces)
        networkConfig.getInterfaces().setEnabled(true)

        hazelcastInstance = Hazelcast.newHazelcastInstance(config)
    }
}
