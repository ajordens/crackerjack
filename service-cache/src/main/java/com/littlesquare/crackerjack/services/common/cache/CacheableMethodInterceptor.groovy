package com.littlesquare.crackerjack.services.common.cache

import net.sf.cglib.proxy.MethodInterceptor
import net.sf.cglib.proxy.MethodProxy

import java.lang.reflect.Method

/**
 * @author Adam Jordens (adam@jordens.org)
 */
public class CacheableMethodInterceptor implements MethodInterceptor {
    private final CacheProvider cacheProvider
    private final Object delegate

    public CacheableMethodInterceptor(CacheProvider cacheProvider, Object delegate) {
        this.cacheProvider = cacheProvider
        this.delegate = delegate
    }

    @Override
    public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        def cacheableAnnotation = method.getAnnotation(Cacheable)
        if (cacheableAnnotation) {
            def key = cacheableAnnotation.cacheKeyRefs().collect { String cacheKeyRef ->
                def matcher = (cacheKeyRef =~ /\{(\d+)\}/)
                if (matcher.matches()) {
                    def argIndex = matcher[0][1] as int
                    if (argIndex < args.length) {
                        return args[argIndex].toString()
                    }
                }
                return cacheKeyRef
            }.join("_").replaceAll("\\s", "_")

            def cache = cacheProvider.getCache(cacheableAnnotation.cacheName())
            if (cache.containsKey(key)) {
                println "!Cache Hit!"
                return cache.get(key)
            }

            println "!Cache Miss!"
            def result = method.invoke(delegate, args)
            cache.put(key, result)
            return result
        }
        return method.invoke(delegate, args)
    }
}
