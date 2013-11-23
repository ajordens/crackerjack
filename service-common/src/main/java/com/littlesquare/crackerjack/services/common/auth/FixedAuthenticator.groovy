package com.littlesquare.crackerjack.services.common.auth

import com.google.common.base.Optional
import com.littlesquare.crackerjack.services.common.core.Person;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

/**
 * @author Adam Jordens (adam@jordens.org)
 */
public class FixedAuthenticator implements Authenticator<BasicCredentials, Person> {
    private final String fixedSecret

    FixedAuthenticator(String fixedSecret) {
        this.fixedSecret = fixedSecret
    }

    @Override
    public Optional<Person> authenticate(BasicCredentials credentials) throws AuthenticationException {
        if (fixedSecret.equals(credentials.getPassword())) {
            return Optional.of(new Person(name: credentials.getUsername()));
        }
        return Optional.absent();
    }
}
