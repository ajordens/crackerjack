package com.littlesquare.crackerjack.services.ui.views

import io.dropwizard.views.View
import freemarker.template.SimpleHash

/**
 * @author Adam Jordens (adam@jordens.org)
 */
public class BaseViewModel extends View {
    final SimpleHash data

    public BaseViewModel(String templateName, Map<String, Object> extraData) {
        super(templateName)
        data = new SimpleHash(new GraphNavigatableMap(extraData))
    }
}

/**
 * A simple map implementation that allows for navigation via dot notation from within a Freemarker template.
 *
 * - Only .get() is overridden
 * - Performance is not currently of utmost concern.
 */
public class GraphNavigatableMap extends HashMap<String, Object> {
    @Override
    Object get(Object key) {
        try {
            def result = null
            key.toString().split("\\.").eachWithIndex { String keyComponent, int index ->
                result = (index == 0) ? super.get(keyComponent) : result."${keyComponent}"
            }
            return result
        } catch (MissingPropertyException e) {
            return null
        }

    }
}