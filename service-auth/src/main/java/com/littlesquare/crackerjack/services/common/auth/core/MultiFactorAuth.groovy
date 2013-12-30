package com.littlesquare.crackerjack.services.common.auth.core

/**
 * @author Adam Jordens (adam@jordens.org)
 */
class MultiFactorAuth {
    Long id
    Long person_id
    String type
    String secret
    boolean active = false
}
