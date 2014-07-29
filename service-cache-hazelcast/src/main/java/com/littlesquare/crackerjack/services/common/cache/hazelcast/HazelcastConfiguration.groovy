package com.littlesquare.crackerjack.services.common.cache.hazelcast

/**
 * @author Adam Jordens (adam@jordens.org)
 */
public class HazelcastConfiguration {
    String multicastGroup = "224.0.0.1"
    List<String> interfaces = [
            "127.0.0.1"
    ]
}
