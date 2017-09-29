package com.lxzl.erp.web.test;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.lxzl.se.core.tbschedule.component.ScheduleManager;
import com.lxzl.se.core.tbschedule.config.ScheduleParameter;
import com.lxzl.se.core.tbschedule.config.TaskRunningInfo;
import com.lxzl.se.unit.test.BaseUnTransactionalTest;

public class ScheduleTester extends BaseUnTransactionalTest {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired(required = false)
	private ScheduleManager scheduleManager;

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {

	}

	@SuppressWarnings("unused")
	@Test
	public void testTbschedule() {
		String scheduleName = "erp-workerScheduler";
		String taskName = "测试日志分析任务";
		String taskBeanName = "logAnalysisTask";
		String strategyName = "测试日志分析任务策略器";

		String taskName1 = "测试日志分析任务A";
		String strategyName1 = "测试日志分析任务策略器A";

		String extraInfo = null;
		String startTime = "0 25 17 * * ?";
		String endTime = "0 30 17 * * ?";
		//scheduleManager.pauseTask(scheduleName, taskName, strategyName);
		scheduleManager.saveOrUpdateTask(scheduleName, taskName, taskBeanName, strategyName, 1600, 1600, null, ScheduleParameter.SLEEP_MODEL, extraInfo);
		//scheduleManager.pauseTask(scheduleName, taskName, strategyName);
		//scheduleManager.resumeTask(scheduleName, taskName, strategyName);
		//scheduleManager.deleteTask(scheduleName, taskName, strategyName);
		//scheduleManager.saveOrUpdateTask(scheduleName, taskName, taskBeanName, strategyName, 100, 100, null, ScheduleParameter.SLEEP_MODEL, startTime, endTime,
		//		extraInfo);
		//scheduleManager.saveOrUpdateTask(scheduleName, taskName, taskBeanName, strategyName, 300, 300, null, ScheduleParameter.SLEEP_MODEL, extraInfo);
		//scheduleManager.saveOrUpdateTask(scheduleName, taskName, taskBeanName, strategyName, extraInfo);
		ScheduleParameter task = scheduleManager.getTask(scheduleName, taskName, strategyName);
		//scheduleManager.resumeTask(scheduleName, taskName, strategyName);
		
		try {
			Thread.sleep(2000l);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		List<ScheduleParameter> tasks = scheduleManager.getSchedulerTasks(scheduleName);

		List<ScheduleParameter> allTasks1 = scheduleManager.getAllTasks();
		List<ScheduleParameter> allTasks2 = scheduleManager.getAllTasks();
		List<ScheduleParameter> allTasks3 = scheduleManager.getAllTasks();
		log.info("allTasks1:");

		for (ScheduleParameter scheduleParameter : allTasks1) {
			log.info(scheduleParameter.toString());
		}

		log.info("allTasks2:");

		for (ScheduleParameter scheduleParameter : allTasks2) {
			log.info(scheduleParameter.toString());
		}

		log.info("allTasks3:");

		for (ScheduleParameter scheduleParameter : allTasks3) {
			log.info(scheduleParameter.toString());
		}

		List<TaskRunningInfo> taskRunningInfo = scheduleManager.getTaskRunningInfoList(scheduleName, taskName, strategyName);
		Assert.assertTrue(true);
	}

}