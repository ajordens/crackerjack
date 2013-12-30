package com.littlesquare.crackerjack.services.common.auth

import com.sun.jersey.api.core.HttpContext
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable
import io.dropwizard.auth.AuthenticationException

import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Response
import com.littlesquare.crackerjack.services.common.auth.api.ExternalPerson
import javax.ws.rs.core.MediaType
import org.slf4j.Logger
import org.slf4j.LoggerFactory

public class ChainedAuthInjectable extends AbstractHttpContextInjectable {
    static final Logger LOG = LoggerFactory.getLogger(ChainedAuthInjectable.class);

    private static final String PREFIX = "Basic"
    private static final String HEADER_NAME = "WWW-Authenticate"
    private static final String HEADER_VALUE = PREFIX + " realm=\"%s\""

    private final String serviceName
    private final String loginPage
    private final AbstractHttpContextInjectable[] injectables

    public ChainedAuthInjectable(String serviceName, String loginPage, AbstractHttpContextInjectable... injectables) {
        this.serviceName = serviceName
        this.loginPage = loginPage
        this.injectables = injectables
    }

    @Override
    public ExternalPerson getValue(HttpContext c) {
        try {
            for (AbstractHttpContextInjectable injectable : injectables) {
                ExternalPerson user = (ExternalPerson) injectable.getValue(c)
                if (user) {
                    return user
                }
            }

            if (c.getRequest().getAcceptableMediaTypes().contains(MediaType.TEXT_HTML_TYPE)) {
                throw new WebApplicationException(
                        Response.seeOther(new URI(loginPage + "?successUrl=${c.getRequest().getPath()}")).build()
                )
            }

            throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
                    .header(HEADER_NAME, String.format(HEADER_VALUE, serviceName))
                    .entity("Credentials are required to access this resource.")
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build());
        } catch (AuthenticationException e) {
            LOG.warn("Error authenticating credentials", e)
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR)
        }
    }
}

