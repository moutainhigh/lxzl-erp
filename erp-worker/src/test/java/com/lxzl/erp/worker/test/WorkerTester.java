package com.lxzl.erp.worker.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lxzl.se.core.quartz.config.QuartzParameter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.lxzl.se.core.quartz.component.QuartzManager;
import com.lxzl.se.core.tbschedule.component.ScheduleManager;
import com.lxzl.se.unit.test.BaseUnTransactionalTest;

public class WorkerTester extends BaseUnTransactionalTest {

	@Autowired(required = false)
	private QuartzManager quartzManager;

	@Autowired(required = false)
	private ScheduleManager scheduleManager;

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testQuartz() {
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
		List<QuartzParameter> quartzParameterList = quartzManager.getAllJobs();
		boolean success = quartzManager.saveOrUpdateJob(schedName, jobName, jobGroup, jobClassName, description, isRecovery, triggerName, triggerGroup, isCronTrigger,
				expression, null, null, extraInfo);

		try {
			Thread.sleep(1000l);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Assert.assertTrue(true);
	}

	@Test
	public void testTbschedule() {
		String scheduleName = "demo-workerScheduler";
		String taskName = "测试日志分析任务";
		String taskBeanName = "logAnalysisTask";
		String strategyName = "测试日志分析任务策略器";
		String extraInfo = null;
		String startTime = "0 36 17 * * ?";
		String endTime = "0 40 17 * * ?";
		//scheduleManager.saveOrUpdateTask(scheduleName, taskName, taskBeanName, strategyName, extraInfo);
//		scheduleManager.saveOrUpdateTask(scheduleName, taskName, taskBeanName, strategyName, 20, 20, null, ScheduleParameter.SLEEP_MODEL, startTime, endTime,
//				extraInfo);
		try {
			Thread.sleep(5000l);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Assert.assertTrue(true);
	}

}