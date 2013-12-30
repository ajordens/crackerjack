package com.littlesquare.crackerjack.services.common.camel

import io.dropwizard.lifecycle.Managed
import org.apache.camel.CamelContext
import org.apache.camel.ProducerTemplate
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.impl.DefaultCamelContext
import org.apache.camel.impl.DefaultProducerTemplate
import org.apache.camel.util.jndi.JndiContext

/**
 * @author Adam Jordens (adam@jordens.org)
 */
class CamelManaged implements Managed {
    private final JndiContext jndiContext = new JndiContext();
    private final CamelContext context = new DefaultCamelContext(jndiContext);
    final ProducerTemplate producerTemplate = new DefaultProducerTemplate(context)

    @Override
    void start() throws Exception {
        context.start()
        producerTemplate.start()
    }

    @Override
    void stop() throws Exception {
        producerTemplate.stop()
        context.stop()
    }

    void addRoutes(RouteBuilder routeBuilder) {
        context.addRoutes(routeBuilder)
    }

    void bind(String key, Object obj) {
        jndiContext.bind(key, obj)
    }
}
