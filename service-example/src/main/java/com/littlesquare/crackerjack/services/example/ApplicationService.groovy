package com.littlesquare.crackerjack.services.example

import com.littlesquare.crackerjack.services.common.CrackerjackServiceExtras
import com.littlesquare.crackerjack.services.common.auth.FixedAuthenticator
import com.littlesquare.crackerjack.services.common.camel.CamelManaged
import com.littlesquare.crackerjack.services.example.extras.ExampleSnsRouteBuilder
import com.littlesquare.crackerjack.services.example.extras.ExampleSqsRouteBuilder
import com.littlesquare.crackerjack.services.example.healthchecks.ExampleHealthCheck
import com.littlesquare.crackerjack.services.example.resources.HelloWorldApiResource
import com.wordnik.swagger.config.ConfigFactory
import com.wordnik.swagger.config.ScannerFactory
import com.wordnik.swagger.config.SwaggerConfig
import com.wordnik.swagger.jaxrs.config.DefaultJaxrsScanner
import com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider
import com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON
import com.wordnik.swagger.jaxrs.listing.ResourceListingProvider
import com.wordnik.swagger.jaxrs.reader.DefaultJaxrsApiReader
import com.wordnik.swagger.reader.ClassReaders
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
        new ApplicationService().run(args);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<ApplicationConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle());
        bootstrap.addBundle(new MigrationsBundle<ApplicationConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(ApplicationConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
        bootstrap.addBundle(new ViewBundle());
    }

    @Override
    public void run(ApplicationConfiguration configuration,
                    Environment environment) throws ClassNotFoundException {
        environment.jersey().register(
                new BasicAuthProvider<>(new FixedAuthenticator("fixedSecret"), "Crackerjack Realm")
        );
        extras.removeBuiltinTasks(environment)

//        def executor = Executors.newFixedThreadPool(1)
//        def future = executor.submit({ (1..25000) } as Callable)
//
//        rx.Observable<Iterable<Integer>> o = rx.Observable.from(future, Schedulers.threadPoolForIO())
//        rx.Observable<String> os = o.mapMany({ iterable -> rx.Observable.from(iterable) })
//        os.subscribe({ it -> println(it) }, { -> }, { -> println "Completed" })

        def camelManaged = new CamelManaged()
        camelManaged.bind("sqsClient", configuration.camel.buildSQSClient())
        camelManaged.bind("snsClient", configuration.camel.buildSNSClient())
        camelManaged.addRoutes(new ExampleSqsRouteBuilder("MyTestQueue"))
        camelManaged.addRoutes(new ExampleSnsRouteBuilder("MyTestTopic"))
        environment.lifecycle().manage(camelManaged)

        environment.jersey().register(new HelloWorldApiResource(camelManaged.producerTemplate));
        environment.jersey().register(new ApiListingResourceJSON())
        environment.jersey().register(new ResourceListingProvider())
        environment.jersey().register(new ApiDeclarationProvider())

        environment.healthChecks().register("example", new ExampleHealthCheck())

        ScannerFactory.setScanner(new DefaultJaxrsScanner());
        ClassReaders.setReader(new DefaultJaxrsApiReader());

        SwaggerConfig config = ConfigFactory.config();
        config.setApiVersion("1.0.0");
        config.setBasePath("http://localhost:8080/api-docs/");
    }
}