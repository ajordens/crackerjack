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
import io.dropwizard.jdbi.DBIFactory
import io.dropwizard.migrations.MigrationsBundle
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import io.dropwizard.views.ViewBundle
import org.skife.jdbi.v2.DBI
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

        try {
            def camelManaged = new CamelManaged()
            camelManaged.bind("sqsClient", configuration.camel.buildSQSClient())
            camelManaged.bind("snsClient", configuration.camel.buildSNSClient())
            camelManaged.addRoutes(new ExampleSqsRouteBuilder("MyTestQueue"))
            camelManaged.addRoutes(new ExampleSnsRouteBuilder("MyTestTopic"))
            environment.lifecycle().manage(camelManaged)
        } catch (RuntimeException e) {
            LOG.error("""
Unable to instantiate AWS components, please create a conf/AwsCredentials.properties file containing the following:
  accessKey=<Your AWS Access Key>
  secretKey=<Your AWS Secret Key>
""".trim(), e)
        }

        environment.jersey().register(new HelloWorldResource())

        def throttle = configuration.throttle.buildThrottle()
        environment.jersey().register(new HelloWorldApiResource(throttle))
        environment.healthChecks().register("example", new ExampleHealthCheck())

        try {
            def factory = new DBIFactory()
            def dbi = factory.build(environment, configuration.database, "database")
            def handle = dbi.open()
            handle.createQuery("SELECT * FROM person").iterator().each { Map<String, Object> result ->
                LOG.debug("Found Person -- ${result}")
            }
        } catch (RuntimeException e) {
            LOG.error("""
Unable to establish connection to database, please ensure that it is running and the migration has been executed.
""".trim(), e)
        }
    }
}
