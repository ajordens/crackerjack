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
        Config config = new Config()
        config.getNetworkConfig().getJoin().getMulticastConfig().setMulticastGroup("224.0.0.1")
        config.getNetworkConfig().getInterfaces().clear()
        config.getNetworkConfig().getInterfaces().addInterface("127.0.0.1")
        config.getNetworkConfig().getInterfaces().setEnabled(true)

        hazelcastInstance = Hazelcast.newHazelcastInstance(config)
    }
}
