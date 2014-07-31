package com.littlesquare.crackerjack.services.example

import com.littlesquare.crackerjack.services.common.CrackerjackServiceExtras
import com.littlesquare.crackerjack.services.common.auth.FixedAuthenticator
import com.littlesquare.crackerjack.services.common.cache.CacheableMethodInterceptor
import com.littlesquare.crackerjack.services.common.cache.hazelcast.HazelcastCacheProvider
import com.littlesquare.crackerjack.services.common.camel.CamelManaged
import com.littlesquare.crackerjack.services.common.cache.hazelcast.HazelcastBundle
import com.littlesquare.crackerjack.services.example.cli.CacheCheckCommand
import com.littlesquare.crackerjack.services.example.extras.ExampleSnsRouteBuilder
import com.littlesquare.crackerjack.services.example.extras.ExampleSqsRouteBuilder
import com.littlesquare.crackerjack.services.example.healthchecks.ExampleHealthCheck
import com.littlesquare.crackerjack.services.example.resources.DefaultHelloWorldApiResource
import com.littlesquare.crackerjack.services.example.resources.HelloWorldApiResource
import com.littlesquare.crackerjack.services.example.resources.HelloWorldResource
import io.dropwizard.Application
import io.dropwizard.assets.AssetsBundle
import io.dropwizard.auth.basic.BasicAuthProvider
import io.dropwizard.db.DataSourceFactory
import io.dropwizard.db.DatabaseConfiguration
import io.dropwizard.jdbi.DBIFactory
import io.dropwizard.migrations.DbMigrateCommand
import io.dropwizard.migrations.MigrationsBundle
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import io.dropwizard.views.ViewBundle
import net.sf.cglib.proxy.Enhancer
import net.sourceforge.argparse4j.inf.Namespace
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Adam Jordens (adam@jordens.org)
 */
class ApplicationService extends Application<ApplicationConfiguration> {
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationService)
    private static final CrackerjackServiceExtras extras = new CrackerjackServiceExtras()

    private final HazelcastBundle hazelcastBundle = new HazelcastBundle()

    public static void main(String[] args) throws Exception {
        new ApplicationService().run(args)
    }

    @Override
    public String getName() {
        return "example-service"
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
        bootstrap.addBundle(hazelcastBundle)
        bootstrap.addBundle(new ViewBundle())
        bootstrap.addCommand(new CacheCheckCommand())
    }

    @Override
    public void run(ApplicationConfiguration configuration,
                    Environment environment) throws ClassNotFoundException {
        environment.healthChecks().register("example", new ExampleHealthCheck())
        environment.jersey().register(
                new BasicAuthProvider<>(new FixedAuthenticator("fixedSecret"), "Crackerjack Realm")
        )
        environment.jersey().register(new HelloWorldResource())
        environment.jersey().register(Enhancer.create(HelloWorldApiResource.class, new CacheableMethodInterceptor(
                new HazelcastCacheProvider(hazelcastBundle.hazelcastInstance),
                new DefaultHelloWorldApiResource(configuration.throttle.buildThrottle()),
                environment.metrics()
        )))

        extras.removeBuiltinTasks(environment)
        setupCamel(configuration, environment)
        migrateAndTestDB(configuration, environment)
    }

    /**
     * An example of programmatically running a DbMigrateCommand, not something you would ever normally do in a real
     * service.
     *
     * @param configuration Application configuration.
     * @param environment Dropwizard environment.
     */
    private static void migrateAndTestDB(ApplicationConfiguration configuration, Environment environment) {
        try {
            def migrateCommand = new DbMigrateCommand<ApplicationConfiguration>(
                    new DatabaseConfiguration<ApplicationConfiguration>() {
                        @Override
                        DataSourceFactory getDataSourceFactory(ApplicationConfiguration cfg) {
                            return cfg.database
                        }
                    }, ApplicationConfiguration
            )
            migrateCommand.run(null, new Namespace(["dry-run": false]), configuration)

            def factory = new DBIFactory()
            def dbi = factory.build(environment, configuration.database, "database")
            def handle = dbi.open()
            handle.createQuery("SELECT * FROM person").iterator().each { Map<String, Object> result ->
                LOG.info("Found Person -- ${result}")
            }
        } catch (RuntimeException e) {
            LOG.error("""
Unable to establish connection to database, please ensure that it is running and the migration has been executed.
""".trim(), e)
        }
    }

    /**
     * Configure a managed camel component with a couple example SQS/SNS routes.
     *
     * @param configuration Application configuration.
     * @param environment Dropwizard environment.
     */
    private static void setupCamel(ApplicationConfiguration configuration, Environment environment) {
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
    }
}