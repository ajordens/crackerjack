package com.littlesquare.crackerjack.services.common.auth

import com.google.common.base.Optional;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import org.eclipse.jetty.util.B64Code;
import org.eclipse.jetty.util.StringUtil;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import io.dropwizard.auth.basic.BasicCredentials
import org.slf4j.Logger
import org.slf4j.LoggerFactory;

class BasicAuthInjectable<T> extends AbstractHttpContextInjectable<T> {
    static final Logger LOG = LoggerFactory.getLogger(BasicAuthInjectable.class);
    private static final String PREFIX = "Basic";

    private final Authenticator<BasicCredentials, T> authenticator;

    public BasicAuthInjectable(Authenticator<BasicCredentials, T> authenticator) {
        this.authenticator = authenticator;
    }

    @Override
    public T getValue(HttpContext c) {
        final String header = c.getRequest().getHeaderValue(HttpHeaders.AUTHORIZATION);
        try {
            if (header != null) {
                final int space = header.indexOf(' ');
                if (space > 0) {
                    final String method = header.substring(0, space);
                    if (PREFIX.equalsIgnoreCase(method)) {
                        final String decoded = B64Code.decode(header.substring(space + 1),
                                StringUtil.__ISO_8859_1);
                        final int i = decoded.indexOf(':');
                        if (i > 0) {
                            final String username = decoded.substring(0, i);
                            final String password = decoded.substring(i + 1);
                            final BasicCredentials credentials = new BasicCredentials(username,
                                    password);
                            final Optional<T> result = authenticator.authenticate(credentials);
                            if (result.isPresent()) {
                                return result.get();
                            }
                        }
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            LOG.debug("Error decoding credentials", e);
        } catch (IllegalArgumentException e) {
            LOG.debug("Error decoding credentials", e);
        } catch (AuthenticationException e) {
            LOG.warn("Error authenticating credentials", e);
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }

        return null;
    }
}

