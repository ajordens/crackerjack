package com.littlesquare.crackerjack.services.common.cache

/**
 * @author Adam Jordens (adam@jordens.org)
 */
public interface Cache {
    boolean containsKey(String name)
    Object get(String name)
    void put(String name, Object value)
}
