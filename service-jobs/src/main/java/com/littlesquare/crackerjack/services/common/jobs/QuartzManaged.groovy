package com.littlesquare.crackerjack.services.common.jobs

import io.dropwizard.lifecycle.Managed
import org.quartz.Scheduler
import org.quartz.impl.StdSchedulerFactory

public class QuartzManaged implements Managed {
    final Scheduler scheduler

    public QuartzManaged() {
        this.scheduler = StdSchedulerFactory.getDefaultScheduler()
    }

    @Override
    public void start() throws Exception {
        scheduler.start()
    }

    @Override
    public void stop() throws Exception {
        scheduler.shutdown()
    }
}