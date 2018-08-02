package com.lxzl.erp.worker.quartz;

import com.lxzl.erp.core.service.statistics.StatisticsService;
import com.lxzl.se.core.quartz.job.BaseJob;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA
 * User: liuyong
 * Date: 2018/7/20
 * Time: 17:18
 */
@Component("statisticsFinanceDataWeeklyJob")
public class StatisticsFinanceDataWeeklyJob extends BaseJob {

    private static final Logger log = LoggerFactory.getLogger(StatisticsFinanceDataWeeklyJob.class);

    @Autowired
    private StatisticsService statisticsService;

    @Override
    public Object executeJob(JobExecutionContext context) throws JobExecutionException {
        JobKey key = context.getJobDetail().getKey();
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        String jobSays = dataMap.getString("jobSays");
        log.info("Instance {} of statisticsFinanceDataWeeklyJob says: {} ", key, jobSays);
        statisticsService.statisticsFinanceDataWeeklyNow();
        return "success";
    }
}
