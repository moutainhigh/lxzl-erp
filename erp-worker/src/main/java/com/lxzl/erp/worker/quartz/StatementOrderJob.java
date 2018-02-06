package com.lxzl.erp.worker.quartz;

import com.lxzl.erp.core.service.statement.StatementService;
import com.lxzl.se.common.util.date.DateUtil;
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

@Component("statementOrderJob")
public class StatementOrderJob extends BaseJob {

    private static final Logger log = LoggerFactory.getLogger(StatementOrderJob.class);

    @Autowired
    private StatementService statementService;

    @Override
    public Object executeJob(JobExecutionContext context) throws JobExecutionException {
        JobKey key = context.getJobDetail().getKey();
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        String jobSays = dataMap.getString("jobSays");
        log.info("Instance {} of statementOrderJob says: {} ", key, jobSays);
        Date currentTime = new Date();
        Date startTime = DateUtil.addMinute(currentTime, -90);
        statementService.handleNoPaidStatementOrder(startTime, currentTime);
        return "success";
    }

}
