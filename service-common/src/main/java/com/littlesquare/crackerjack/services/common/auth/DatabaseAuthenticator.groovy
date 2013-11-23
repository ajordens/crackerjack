package com.littlesquare.crackerjack.services.common.auth

import com.littlesquare.crackerjack.services.common.api.User
import com.google.common.base.Optional
import com.littlesquare.crackerjack.services.common.db.AuthDAO
import io.dropwizard.auth.basic.BasicCredentials
import io.dropwizard.auth.AuthenticationException
import org.mindrot.jbcrypt.BCrypt
import io.dropwizard.auth.Authenticator
import com.littlesquare.crackerjack.services.common.db.PersonDAO
import com.littlesquare.crackerjack.services.common.core.Person
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * An authenticator that will verify a username and password against the database.
 *
 * Passwords are expected to be encrypted w/ bcrypt.
 */
class DatabaseAuthenticator implements Authenticator<BasicCredentials, User> {
    static final Logger LOG = LoggerFactory.getLogger(DatabaseAuthenticator.class);
    private final PersonDAO personDAO
    private final AuthDAO authDAO

    DatabaseAuthenticator(PersonDAO personDAO, AuthDAO authDAO) {
        this.personDAO = personDAO
        this.authDAO = authDAO
    }

    @Override
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {
        Person person = personDAO.findByEmail(credentials.getUsername())

        try {
            if (person && BCrypt.checkpw(credentials.getPassword(), person.password)) {
                def user = new User(person.id, credentials.getUsername())
                def multiFactorAuth = authDAO.findMultiFactorAuthByPersonId(person.id, "google-authenticator")
                if (multiFactorAuth) {
                    user.requiresMultiFactorAuth = multiFactorAuth.active
                }
                return Optional.of(user)
            }
        } catch (Exception e) {
            LOG.warn("Failed authentication (${person.id}): ${e.getMessage()}")
        }

        return Optional.absent()
    }
}
