package com.lxzl.erp.worker.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.lxzl.se.core.quartz.component.QuartzManager;
import com.lxzl.se.core.tbschedule.component.ScheduleManager;
import com.lxzl.se.core.tbschedule.config.ScheduleParameter;
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
		String schedName = "erp-workerScheduler";
		String jobName = "测试订单统计任务";
		String jobGroup = "测试订单任务组";
		String jobClassName = "demoTestJob";
		String description = "测试统计每日的订单总量并且分组";
		boolean isRecovery = false;
		String triggerName = "测试每分钟执行的订单统计";
		String triggerGroup = "测试订单触发器组";
		boolean isCronTrigger = true;
		String expression = "30 * * * * ?";
		Map<String, String> extraInfo = new HashMap<String, String>();
		extraInfo.put("extra", "erp");
		quartzManager.getAllJobs();
		quartzManager.saveOrUpdateJob(schedName, jobName, jobGroup, jobClassName, description, isRecovery, triggerName, triggerGroup, isCronTrigger,
				expression, null, null, extraInfo);

		try {
			Thread.sleep(5000l);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Assert.assertTrue(true);
	}

	@Test
	public void testTbschedule() {
		String scheduleName = "erp-workerScheduler";
		String taskName = "测试日志分析任务";
		String taskBeanName = "logAnalysisTask";
		String strategyName = "测试日志分析任务策略器";
		String extraInfo = null;
		String startTime = "0 36 17 * * ?";
		String endTime = "0 40 17 * * ?";
		//scheduleManager.saveOrUpdateTask(scheduleName, taskName, taskBeanName, strategyName, extraInfo);
		scheduleManager.saveOrUpdateTask(scheduleName, taskName, taskBeanName, strategyName, 20, 20, null, ScheduleParameter.SLEEP_MODEL, startTime, endTime,
				extraInfo);
		try {
			Thread.sleep(5000l);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Assert.assertTrue(true);
	}

}