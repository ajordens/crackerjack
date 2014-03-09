package com.littlesquare.crackerjack.services.ui.views

import org.junit.Test

import java.util.concurrent.Callable
import java.util.concurrent.Executors

/**
 * @author Adam Jordens (adam@jordens.org)
 */
public class BaseViewModelTest {
    private final Callable callable1 = [call: {
        (1..10).each { Thread.sleep(100) }
        return [ "nested": "future1" ]
    }] as Callable

    private final Callable callable2 = [call: {
        (1..2).each { Thread.sleep(400) }
        return "future2"
    }] as Callable

    @Test
    void baseViewModel__ParallelFutures() {
        def viewModel = new BaseViewModel("n/a", ["future1": callable1, "future2": callable2])

        long startTime = System.currentTimeMillis()
        assert viewModel.getData().get("future1.nested").toString() == "future1"
        assert viewModel.getData().get("future2").toString() == "future2"
        long endTime = System.currentTimeMillis()

        // expected to be significantly < cumulative sleep time
        assert (endTime - startTime) < 1200
    }

    @Test
    void baseViewModel__SingleThreadedFutures() {
        def viewModel = new BaseViewModel("n/a", ["future1": callable1, "future2": callable2], Executors.newSingleThreadExecutor())

        long startTime = System.currentTimeMillis()
        assert viewModel.getData().get("future1.nested").toString() == "future1"
        assert viewModel.getData().get("future2").toString() == "future2"
        long endTime = System.currentTimeMillis()

        // expected to be > cumulative sleep time
        assert (endTime - startTime) >= 1800
    }
}
