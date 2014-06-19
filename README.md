# Crackerjack

### What is it?

Crackerjack is a Groovy-based amalgamation of common nice-to-haves when building Dropwizard-based RESTful services.

Includes utilities for both API and UI-based services.

Currently targets Dropwizard 0.7.

##### Usage:
    <repositories>
        <repository>
            <id>crackerjack-mvn-repo</id>
            <name>Crackerjack</name>
            <url>https://raw.github.com/ajordens/crackerjack/mvn-repo/</url>
            <releases>
                <enabled>true</enabled>
            </releases>
            <snapshots>
                <enabled>true</enabled>
                <updatePolicy>always</updatePolicy>
            </snapshots>
        </repository>
    </repositories>

##### Includes:

- Simple Camel and Quartz components.
- Simple Guava RateLimiter-based Throttle.
- BaseViewModel support for asynchronous data retrieval.

        def viewModel = new BaseViewModel("view.ftl", [
            "future1": [call: {
                (1..10).each { Thread.sleep(100) }
                return "future1"
            }] as Callable,
            "future2": [call: {
               (1..2).each { Thread.sleep(400) }
               return "future2"
           }] as Callable
        ])

        long startTime = System.currentTimeMillis()
        assert viewModel.getData().get("future1").toString() == "future1"
        assert viewModel.getData().get("future2").toString() == "future2"
        long endTime = System.currentTimeMillis()

        // expected to be significantly < cumulative sleep time
        assert (endTime - startTime) < 1200
- Codenarc integration.

##### Work In Progress:

- OkHttp integration.

