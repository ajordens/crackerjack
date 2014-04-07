package com.littlesquare.crackerjack.services.common

import com.littlesquare.crackerjack.services.common.filters.AcceptLanguageFilter
import io.dropwizard.setup.Environment

import javax.servlet.DispatcherType

/**
 * @author Adam Jordens (adam@jordens.org)
 */
public class CrackerjackServiceExtras {
    public void removeBuiltinTasks(Environment environment) {
        environment.admin().@tasks.@tasks.clear()
    }

    public void applyDefaultFilters(Environment environment) {
        environment.servlets().addFilter("AcceptLanguageFilter", AcceptLanguageFilter).addMappingForUrlPatterns(
                EnumSet.allOf(DispatcherType), true, "/*"
        )
    }
}
