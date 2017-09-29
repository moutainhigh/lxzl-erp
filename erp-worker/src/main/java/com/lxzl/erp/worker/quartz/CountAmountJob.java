package com.lxzl.erp.worker.quartz;

import com.lxzl.se.core.quartz.job.BaseJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("countAmountJob")
public class CountAmountJob extends BaseJob {

    private static final Logger log = LoggerFactory.getLogger(CountAmountJob.class);

    @Override
    public Object executeJob(JobExecutionContext context) throws JobExecutionException {
        log.info("execute countAmountJob");

        return null;
    }

}
