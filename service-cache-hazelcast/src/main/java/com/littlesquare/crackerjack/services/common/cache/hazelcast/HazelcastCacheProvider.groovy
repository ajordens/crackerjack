package com.littlesquare.crackerjack.services.common.cache.hazelcast

import com.hazelcast.core.HazelcastInstance
import com.littlesquare.crackerjack.services.common.cache.Cache
import com.littlesquare.crackerjack.services.common.cache.CacheProvider

/**
 * @author Adam Jordens (adam@jordens.org)
 */
public class HazelcastCacheProvider implements CacheProvider {
    private final HazelcastInstance hazelcastInstance

    public HazelcastCacheProvider(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance
    }

    @Override
    Cache getCache(String name) {
        return new HazelcastCache(hazelcastInstance.getMap(name))
    }
}
