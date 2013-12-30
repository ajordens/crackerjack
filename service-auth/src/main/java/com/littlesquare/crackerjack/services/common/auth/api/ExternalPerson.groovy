package com.littlesquare.crackerjack.services.common.auth.api;

public class ExternalPerson {
    final Long id
    final String email
    boolean requiresMultiFactorAuth = false
    boolean hasMultiFactorAuth = false

    public ExternalPerson(Long id, String email) {
        this.id = id
        this.email = email
    }
}