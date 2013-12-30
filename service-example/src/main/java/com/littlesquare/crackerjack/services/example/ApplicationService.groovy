package com.littlesquare.crackerjack.services.example

import com.littlesquare.crackerjack.services.common.CrackerjackServiceExtras
import com.littlesquare.crackerjack.services.common.auth.FixedAuthenticator
import com.littlesquare.crackerjack.services.common.camel.CamelManaged
import com.littlesquare.crackerjack.services.example.extras.ExampleSnsRouteBuilder
import com.littlesquare.crackerjack.services.example.extras.ExampleSqsRouteBuilder
import com.littlesquare.crackerjack.services.example.healthchecks.ExampleHealthCheck
import com.littlesquare.crackerjack.services.example.resources.HelloWorldApiResource
import com.littlesquare.crackerjack.services.example.resources.HelloWorldResource
import io.dropwizard.Application
import io.dropwizard.assets.AssetsBundle
import io.dropwizard.auth.basic.BasicAuthProvider
import io.dropwizard.db.DataSourceFactory
import io.dropwizard.migrations.MigrationsBundle
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import io.dropwizard.views.ViewBundle
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Adam Jordens (adam@jordens.org)
 */
class ApplicationService extends Application<ApplicationConfiguration> {
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationService)
    private static final CrackerjackServiceExtras extras = new CrackerjackServiceExtras()

    public static void main(String[] args) throws Exception {
        new ApplicationService().run(args)
    }

    @Override
    public String getName() {
        return "hello-world"
    }

    @Override
    public void initialize(Bootstrap<ApplicationConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle())
        bootstrap.addBundle(new MigrationsBundle<ApplicationConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(ApplicationConfiguration configuration) {
                return configuration.database
            }
        })
        bootstrap.addBundle(new ViewBundle())
    }

    @Override
    public void run(ApplicationConfiguration configuration,
                    Environment environment) throws ClassNotFoundException {
        environment.jersey().register(
                new BasicAuthProvider<>(new FixedAuthenticator("fixedSecret"), "Crackerjack Realm")
        )
        extras.removeBuiltinTasks(environment)

        def camelManaged = new CamelManaged()
        camelManaged.bind("sqsClient", configuration.camel.buildSQSClient())
        camelManaged.bind("snsClient", configuration.camel.buildSNSClient())
        camelManaged.addRoutes(new ExampleSqsRouteBuilder("MyTestQueue"))
        camelManaged.addRoutes(new ExampleSnsRouteBuilder("MyTestTopic"))
        environment.lifecycle().manage(camelManaged)

        environment.jersey().register(new HelloWorldResource())
        environment.jersey().register(new HelloWorldApiResource(camelManaged.producerTemplate))
        environment.healthChecks().register("example", new ExampleHealthCheck())
    }
}