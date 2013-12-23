package com.littlesquare.crackerjack.services.common.auth.core

/**
 * @author Adam Jordens (adam@jordens.org)
 */
class Oauth {
    Long id
    Long person_id
    String type
    String access_token
    String raw
    boolean active = true
}
