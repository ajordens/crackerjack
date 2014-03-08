# Crackerjack

### What is it?

Crackerjack is a Groovy-based amalgamation of common nice-to-haves when building Dropwizard-based RESTful services.

Includes utilities for both API and UI-based services.

Currently targets Dropwizard 0.7-rc2.

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
- Codenarc integration.

##### Work In Progress:

- OkHttp integration.

