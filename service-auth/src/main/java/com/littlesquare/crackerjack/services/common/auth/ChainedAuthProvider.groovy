package com.littlesquare.crackerjack.services.common.auth

import com.sun.jersey.api.model.Parameter;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;
import io.dropwizard.auth.Auth;

import javax.ws.rs.ext.Provider
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;

@Provider
public class ChainedAuthProvider<T> implements InjectableProvider<Auth, Parameter> {
    private final AbstractHttpContextInjectable[] injectables

    /**
     * Creates a new {@link ChainedAuthProvider}.
     */
    public ChainedAuthProvider(AbstractHttpContextInjectable... injectables) {
        this.injectables = injectables
    }

    @Override
    public ComponentScope getScope() {
        return ComponentScope.PerRequest;
    }

    @Override
    public Injectable<?> getInjectable(ComponentContext ic,
                                       Auth a,
                                       Parameter c) {
        new ChainedAuthInjectable(injectables);
    }
}
