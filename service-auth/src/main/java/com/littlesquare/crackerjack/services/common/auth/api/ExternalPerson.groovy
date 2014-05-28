package com.littlesquare.crackerjack.services.common.auth.api;

public class ExternalPerson {
    final Long id
    final String email
    final Map extras
    boolean requiresMultiFactorAuth = false
    boolean hasMultiFactorAuth = false

    public ExternalPerson(Long id, String email, Map extras) {
        this.id = id
        this.email = email
        this.extras = extras
    }
}