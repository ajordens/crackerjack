package com.littlesquare.crackerjack.services.common.cache

import org.junit.Test

/**
 * @author Adam Jordens (adam@jordens.org)
 */
public class CacheableMethodInterceptorTest {
    @Test
    void annotationParsing() {
        def string = "{90}"
        def matcher = (string =~ /\{(\d+)\}/)
        println matcher.matches()
        println matcher[0][1]
        assert matcher.matches()
    }
}
