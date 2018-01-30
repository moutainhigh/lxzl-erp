package com.lxzl.erp.worker.quartz;

import com.lxzl.se.core.quartz.job.BaseJob;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("countAmountJob")
public class CountAmountJob extends BaseJob {
    private static final Logger log = LoggerFactory.getLogger(CountAmountJob.class);

    @Override
    public Object executeJob(JobExecutionContext context) throws JobExecutionException {
        JobKey key = context.getJobDetail().getKey();
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        String jobSays = dataMap.getString("jobSays");
        log.info("Instance {} of countAmountJob says: {} ", key, jobSays);

        return null;
    }

}
