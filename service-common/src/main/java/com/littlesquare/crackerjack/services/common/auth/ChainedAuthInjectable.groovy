package com.littlesquare.crackerjack.services.common.auth

import com.sun.jersey.api.core.HttpContext
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable
import io.dropwizard.auth.AuthenticationException

import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Response
import com.littlesquare.crackerjack.services.common.api.User
import javax.ws.rs.core.MediaType
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ChainedAuthInjectable extends AbstractHttpContextInjectable {
    static final Logger LOG = LoggerFactory.getLogger(ChainedAuthInjectable.class);

    private static final String PREFIX = "Basic"
    private static final String HEADER_NAME = "WWW-Authenticate"
    private static final String HEADER_VALUE = PREFIX + " realm=\"%s\""

    private final AbstractHttpContextInjectable[] injectables

    ChainedAuthInjectable(AbstractHttpContextInjectable... injectables) {
        this.injectables = injectables
    }

    @Override
    public User getValue(HttpContext c) {
        try {
            for (AbstractHttpContextInjectable injectable : injectables) {
                User user = (User) injectable.getValue(c)
                if (user) {
                    return user
                }
            }

            if (c.getRequest().getAcceptableMediaTypes().contains(MediaType.TEXT_HTML_TYPE)) {
                throw new WebApplicationException(
                        Response.seeOther(new URI(ViewModel.LOGIN + "?successUrl=${c.getRequest().getPath()}")).build()
                )
            }

            throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
                    .header(HEADER_NAME,
                    String.format(HEADER_VALUE, "Example Service"))
                    .entity("Credentials are required to access this resource.")
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build());
        } catch (AuthenticationException e) {
            LOG.warn("Error authenticating credentials", e)
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR)
        }
    }
}

