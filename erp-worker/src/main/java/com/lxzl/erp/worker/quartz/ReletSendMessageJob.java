package com.lxzl.erp.worker.quartz;

import com.lxzl.erp.core.service.reletorder.ReletOrderService;
import com.lxzl.se.core.quartz.job.BaseJob;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component("reletSendMessageJob")
public class ReletSendMessageJob extends BaseJob {

    private static final Logger log = LoggerFactory.getLogger(ReletSendMessageJob.class);

    @Autowired
    private ReletOrderService reletOrderService;

    @Override
    public Object executeJob(JobExecutionContext context) throws JobExecutionException {
        JobKey key = context.getJobDetail().getKey();
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        String jobSays = dataMap.getString("jobSays");
        log.info("Instance {} of ReletSendMessageJob says: {} ", key, jobSays);

        reletOrderService.handleReletSendMessage(new Date());
        return "success";
    }
}
