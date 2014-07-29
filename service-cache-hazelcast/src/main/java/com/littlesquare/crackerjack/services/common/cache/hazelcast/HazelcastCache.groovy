package com.littlesquare.crackerjack.services.common.cache.hazelcast

import com.hazelcast.core.IMap
import com.littlesquare.crackerjack.services.common.cache.Cache

import java.util.concurrent.TimeUnit

/**
 * @author Adam Jordens (adam@jordens.org)
 */
public class HazelcastCache implements Cache {
    private final IMap delegate

    public HazelcastCache(IMap delegate) {
        this.delegate = delegate
    }

    @Override
    public boolean containsKey(String name) {
        return delegate.containsKey(name)
    }

    @Override
    public Object get(String name) {
        return delegate.get(name)
    }

    @Override
    void put(String name, Object value) {
        delegate.put(name, value)
    }

    @Override
    void put(String name, Object value, long timeToLive, TimeUnit timeUnit) {
        delegate.put(name, value, timeToLive, timeUnit)
    }
}
