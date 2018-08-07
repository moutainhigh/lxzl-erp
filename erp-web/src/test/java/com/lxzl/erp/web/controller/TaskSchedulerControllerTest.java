package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.taskScheduler.TaskSchedulerCommitParam;
import com.lxzl.erp.common.domain.taskScheduler.TriggerCommitParam;
import com.lxzl.erp.common.domain.taskScheduler.pojo.TaskExecutor;
import org.junit.Test;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/7/26
 * @Time : Created in 16:08
 */
public class TaskSchedulerControllerTest  extends ERPUnTransactionalTest {
    @Test
    public void initTask() throws Exception {
        TaskSchedulerCommitParam taskSchedulerCommitParam = new TaskSchedulerCommitParam();
        taskSchedulerCommitParam.setTriggerName("肖弟测试触发器1");
        taskSchedulerCommitParam.setTriggerGroup("测试一组1");
        taskSchedulerCommitParam.setCronExpression("? 35 16 * * ? *");
        TaskExecutor taskExecutor = new TaskExecutor();
        taskExecutor.setRequestUrl("http://192.168.10.93:8080/taskScheduler/test");
        taskExecutor.setSystemType(1);
        taskExecutor.setJobType(1);  //1:rest、2:mq、3:redis
        taskExecutor.setRequestBody("这是请求主体");
        taskSchedulerCommitParam.setTaskExecutor(taskExecutor);
        TestResult jsonTestResult = getJsonTestResult("/taskScheduler/initTask", taskSchedulerCommitParam);
    }

    @Test
    public void updateTaskScheduler() throws Exception {
    }

    @Test
    public void pauseTaskScheduler() throws Exception {
        TriggerCommitParam triggerCommitParam = new TriggerCommitParam();
        triggerCommitParam.setTriggerName("肖弟测试触发器");
        triggerCommitParam.setTriggerGroup("测试一组");
        TestResult jsonTestResult = getJsonTestResult("/taskScheduler/pauseTaskScheduler", triggerCommitParam);
    }

    @Test
    public void resumeTaskTrigger() throws Exception {
    }

    @Test
    public void deleteTrigger() throws Exception {
        TriggerCommitParam triggerCommitParam = new TriggerCommitParam();
        triggerCommitParam.setTriggerName("肖弟测试触发器");
        triggerCommitParam.setTriggerGroup("测试一组");
        TestResult jsonTestResult = getJsonTestResult("/taskScheduler/deleteTrigger", triggerCommitParam);
    }

    @Test
    public void deleteTrigger1() throws Exception {
    }

    @Test
    public void pageTaskExecutor() throws Exception {
    }

}