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
        assert matcher[0][1] == "90"
        assert matcher.matches()
    }
}
