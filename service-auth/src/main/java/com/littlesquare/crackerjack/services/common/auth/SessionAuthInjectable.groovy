package com.littlesquare.crackerjack.services.common.auth

import com.littlesquare.crackerjack.services.common.auth.dao.PersonDAO
import com.sun.jersey.api.core.HttpContext
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable
import io.dropwizard.auth.AuthenticationException

import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Response
import com.littlesquare.crackerjack.services.common.auth.api.ExternalPerson
import org.eclipse.jetty.server.session.SessionHandler
import javax.servlet.http.HttpSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory

public class SessionAuthInjectable extends AbstractHttpContextInjectable {
    public static final String SESSION_USER = "user"

    static final Logger LOG = LoggerFactory.getLogger(SessionAuthInjectable.class);

    private final SessionHandler sessionHandler
    private final PersonDAO personDAO

    public SessionAuthInjectable(SessionHandler sessionHandler, PersonDAO personDAO) {
        this.sessionHandler = sessionHandler
        this.personDAO = personDAO
    }

    @Override
    public ExternalPerson getValue(HttpContext c) {
        try {
            String sessionId = c.getRequest().getCookieNameValueMap().getFirst("JSESSIONID")

            if (sessionId) {
                HttpSession session = sessionHandler.getSessionManager().getHttpSession(sessionId)
                if (session) {
                    ExternalPerson currentUser = (ExternalPerson) session.getAttribute(AuthHelper.SESSION_USER)
                    if (currentUser) {
                        def uri = c.request.requestUri.toString()
                        if (currentUser.requiresMultiFactorAuth && !currentUser.hasMultiFactorAuth && !uri.endsWith("/google-authenticator")) {
                            throw new WebApplicationException(Response.seeOther(new URI("/auth/login/google-authenticator")).build())
                        }

                        return currentUser
                    }
                }
            }

            return null
        } catch (AuthenticationException e) {
            LOG.warn("Error authenticating credentials", e)
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR)
        }
    }
}


