package com.littlesquare.crackerjack.services.ui.views

import org.junit.Test

/**
 * @author Adam Jordens (adam@jordens.org)
 */
public class GraphNavigatableMapTest {
    private final GraphNavigatableMap graphNavigatableMap = new GraphNavigatableMap([
            "empty": null,
            "single": "singleValue",
            "nested": [
                    "one": [
                            "two": [
                                    "three": "nestedValue"
                            ]


                    ]
            ]
    ])

    @Test
    void get() {
        assert graphNavigatableMap.get("single") == "singleValue"
        assert graphNavigatableMap.get("nested.one.two.three") == "nestedValue"
        assert graphNavigatableMap.get("nested.one.two.three.missing") == null
        assert graphNavigatableMap.get("nested.one.two.missing") == null
    }
}
