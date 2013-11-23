package com.littlesquare.crackerjack.services.common.api;

public class User {
    final Long id
    final String email
    boolean requiresMultiFactorAuth = false
    boolean hasMultiFactorAuth = false

    public User(Long id, String email) {
        this.id = id
        this.email = email
    }
}