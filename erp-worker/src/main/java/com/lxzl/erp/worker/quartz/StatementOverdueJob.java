package com.lxzl.erp.worker.quartz;

import com.lxzl.erp.common.util.DateUtil;
import com.lxzl.erp.core.service.statement.StatementService;
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

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2018-02-01 19:57
 */
@Component("statementOverdueJob")
public class StatementOverdueJob extends BaseJob {

    private static final Logger log = LoggerFactory.getLogger(StatementOrderJob.class);

    @Autowired
    private StatementService statementService;

    @Override
    public Object executeJob(JobExecutionContext context) throws JobExecutionException {
        JobKey key = context.getJobDetail().getKey();
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        String jobSays = dataMap.getString("jobSays");
        log.info("Instance {} of StatementOverdueJob says: {} ", key, jobSays);
        Date currentTime = new Date();
        // 每天1点执行，前一天以前的数据
        currentTime = com.lxzl.se.common.util.date.DateUtil.dateInterval(currentTime, -1);
        currentTime = com.lxzl.se.common.util.date.DateUtil.getEndOfDay(currentTime);
        statementService.handleOverdueStatementOrder(null, currentTime);
        return "success";
    }

}
