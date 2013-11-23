package com.littlesquare.crackerjack.services.common.views

import io.dropwizard.views.View
import freemarker.template.SimpleHash

/**
 * @author Adam Jordens (adam@jordens.org)
 */
class BaseViewModel extends View {
    final SimpleHash data

    public BaseViewModel(Map defaults, String viewName, Map<String, Object> extraData) {
        super(viewName)
        data = new SimpleHash(defaults)
        data.putAll(extraData)
    }
}
