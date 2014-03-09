package com.littlesquare.crackerjack.services.ui.views

import io.dropwizard.views.View
import freemarker.template.SimpleHash

import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * @author Adam Jordens (adam@jordens.org)
 */
public class BaseViewModel extends View {
    private final ExecutorService executorService
    final SimpleHash data

    public BaseViewModel(String templateName, Map<String, Object> extraData) {
        this(templateName, extraData, Executors.newCachedThreadPool());
    }

    public BaseViewModel(String templateName, Map<String, Object> extraData, ExecutorService executorService) {
        super(templateName)
        this.data = new SimpleHash(new GraphNavigatableMap())
        this.executorService = executorService

        extraData.each { String key, Object value ->
            if (value instanceof Callable) {
                data.put(key, executorService.submit((Callable) value))
            } else {
                data.put(key, value)
            }
        }
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
                if (result instanceof Future) {
                    result = ((Future) result).get()
                }
            }
            if (result instanceof Future) {
                return ((Future) result).get()
            }
            return result
        } catch (MissingPropertyException e) {
            return null
        }

    }
}