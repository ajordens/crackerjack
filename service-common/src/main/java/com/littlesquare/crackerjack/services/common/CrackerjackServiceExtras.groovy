package com.littlesquare.crackerjack.services.common

import io.dropwizard.setup.Environment

/**
 * @author Adam Jordens (adam@jordens.org)
 */
public class CrackerjackServiceExtras {
    public void removeBuiltinTasks(Environment environment) {
        environment.admin().@tasks.@tasks.clear()
    }
}
