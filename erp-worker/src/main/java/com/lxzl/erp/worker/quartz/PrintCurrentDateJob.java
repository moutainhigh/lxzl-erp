package com.lxzl.erp.worker.quartz;

import com.lxzl.se.core.quartz.job.BaseJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>Description: </p>
 *
 * @Auther: huahongbin
 * @Date: 2018/5/23
 */
@Component("printCurrentDateJob")
public class PrintCurrentDateJob extends BaseJob {
    @Override
    public Object executeJob(JobExecutionContext context) throws JobExecutionException {
        log.info("当前时间为：{}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return null;
    }
}
