package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalNoLoginTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.quartz.QuartzJob;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: </p>
 *
 * @Auther: huahongbin
 * @Date: 2018/5/23
 */
public class QuartzControllerTest extends ERPUnTransactionalNoLoginTest {
    @Test
    public void saveJob() throws Exception {
        QuartzJob quartzJob = buildPrintCurrentDateJob();
        TestResult testResult = getJsonTestResult("/quartz/saveJob", quartzJob);
    }

    @Test
    public void updateJob() throws Exception {
        QuartzJob quartzJob = buildStatementOverdueJob();
        TestResult testResult = getJsonTestResult("/quartz/updateJob", quartzJob);
    }

    @Test
    public void saveOrUpdateJob() throws Exception {
        QuartzJob quartzJob = buildPrintCurrentDateJob();
        TestResult testResult = getJsonTestResult("/quartz/saveOrUpdateJob", quartzJob);
    }

    @Test
    public void pauseTrigger() throws Exception {
    }

    @Test
    public void resumeTrigger() throws Exception {
    }

    @Test
    public void deleteTrigger() throws Exception {
        QuartzJob quartzJob = new QuartzJob();
        quartzJob.setSchedName("workerMonitorScheduler");
        quartzJob.setTriggerName("执行定时打印当前时间任务");
        quartzJob.setTriggerGroup("执行定时打印当前时间任务组");
        getJsonTestResult("/quartz/deleteTrigger", quartzJob);
    }

    @Test
    public void getAllJobs() throws Exception {
        getJsonTestResult("/quartz/getAllJobs", null);
    }

    @Test
    public void getSchedulerJobs() throws Exception {
    }

    @Test
    public void getJob() throws Exception {
    }

    @Test
    public void getJobRunningInfo() throws Exception {
    }

    private QuartzJob buildPrintCurrentDateJob() {
        String schedName = "workerMonitorScheduler";
        String jobName = "定时打印时间任务";
        String jobGroup = "定时打印时间任务组";
        String jobClassName = "printCurrentDateJob";
        String description = "每分钟打印当前时间";
        boolean isRecovery = false;
        String triggerName = "执行定时打印当前时间任务";
        String triggerGroup = "执行定时打印当前时间任务组";
        boolean isCronTrigger = true;
        String expression = "0 */1 * * * ?"; //1分钟執行一次
        Map<String, String> extraInfo = new HashMap<String, String>();
        extraInfo.put("jobSays", "Handle print current data!");

        QuartzJob quartzJob = new QuartzJob();
        quartzJob.setSchedName(schedName);
        quartzJob.setJobName(jobName);
        quartzJob.setJobGroup(jobGroup);
        quartzJob.setJobClassName(jobClassName);
        quartzJob.setDescription(description);
        quartzJob.setRecoveryFlag(isRecovery);
        quartzJob.setTriggerName(triggerName);
        quartzJob.setTriggerGroup(triggerGroup);
        quartzJob.setCronTriggerFlag(isCronTrigger);
        quartzJob.setExpression(expression);
        quartzJob.setExtraInfo(extraInfo);
        quartzJob.setStartAt(null);
        quartzJob.setEndAt(null);
        return quartzJob;
    }

    private QuartzJob buildStatementOverdueJob() {
        String schedName = "workerMonitorScheduler";
        String jobName = "定时检查逾期结算单任务";
        String jobGroup = "定时检查逾期结算单任务组";
        String jobClassName = "statementOverdueJob";
        String description = "每天1点检查前一天逾期情况，如果没交钱，算逾期";
        boolean isRecovery = false;
        String triggerName = "执行定时检查逾期任务";
        String triggerGroup = "执行定时检查逾期任务组";
        boolean isCronTrigger = true;
        String expression = "0 0 1 * * ?"; //1分钟執行一次
        Map<String, String> extraInfo = new HashMap<String, String>();
        extraInfo.put("jobSays", "Handle overdue Statement Order!");

        QuartzJob quartzJob = new QuartzJob();
        quartzJob.setSchedName(schedName);
        quartzJob.setJobName(jobName);
        quartzJob.setJobGroup(jobGroup);
        quartzJob.setJobClassName(jobClassName);
        quartzJob.setDescription(description);
        quartzJob.setRecoveryFlag(isRecovery);
        quartzJob.setTriggerName(triggerName);
        quartzJob.setTriggerGroup(triggerGroup);
        quartzJob.setCronTriggerFlag(isCronTrigger);
        quartzJob.setExpression(expression);
        quartzJob.setExtraInfo(extraInfo);
        quartzJob.setStartAt(null);
        quartzJob.setEndAt(null);
        return quartzJob;
    }

}