package com.littlesquare.crackerjack.services.common.cache

import com.codahale.metrics.MetricRegistry
import net.sf.cglib.proxy.MethodInterceptor
import net.sf.cglib.proxy.MethodProxy

import java.lang.reflect.Method

/**
 * @author Adam Jordens (adam@jordens.org)
 */
public class CacheableMethodInterceptor implements MethodInterceptor {
    private final MetricRegistry metricRegistry
    private final CacheProvider cacheProvider
    private final Object delegate

    public CacheableMethodInterceptor(CacheProvider cacheProvider, Object delegate, MetricRegistry metricRegistry) {
        this.cacheProvider = cacheProvider
        this.delegate = delegate
        this.metricRegistry = metricRegistry
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
                metricRegistry.meter([method.declaringClass.name, method.name, "hit"].join(".")).mark()
                return cache.get(key)
            }

            metricRegistry.meter([method.declaringClass.name, method.name, "miss"].join(".")).mark()
            def result = method.invoke(delegate, args)
            cache.put(key, result, cacheableAnnotation.timeToLive(), cacheableAnnotation.timeUnit())
            return result
        }
        return method.invoke(delegate, args)
    }
}
