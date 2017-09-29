package com.lxzl.erp.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lxzl.se.common.domain.Result;
import com.lxzl.se.common.domain.ResultCode;
import com.lxzl.se.common.util.validate.ValidateUtil;
import com.lxzl.se.core.tbschedule.component.ScheduleManager;
import com.lxzl.se.core.tbschedule.config.ScheduleParameter;
import com.lxzl.se.web.controller.BaseController;

@RequestMapping("/schedule")
@Controller
public class ScheduleController extends BaseController {

	@Autowired(required = false)
	private ScheduleManager scheduleManager;

	@RequestMapping(value = "/addSchedule")
	public Result addSchedule(HttpServletRequest request, HttpServletResponse response, Model model) {
		String scheduleName = getStringParameter("scheduleName");
		String taskName = getStringParameter("taskName");
		String taskBeanName = getStringParameter("taskBeanName");
		String strategyName = getStringParameter("strategyName");
		Integer fetchDataNumber = getIntegerParameter("fetchDataNumber", 100);
		Integer executeNumber = getIntegerParameter("executeNumber", 100);

		ValidateUtil.isNotBlank(scheduleName, "scheduleName不能为空!");
		ValidateUtil.isNotBlank(taskName, "taskName不能为空!");
		ValidateUtil.isNotBlank(taskBeanName, "taskBeanName不能为空!");
		ValidateUtil.isNotBlank(strategyName, "strategyName不能为空!");

		String extraInfo = null;
		scheduleManager.saveOrUpdateTask(scheduleName, taskName, taskBeanName, strategyName, fetchDataNumber, executeNumber, null, ScheduleParameter.SLEEP_MODEL, extraInfo);
		Result result = new Result(ResultCode.COMMON_SUCCESS, true);
		return result;
	}

	@RequestMapping(value = "/addTimeSchedule")
	public Result addTimeSchedule(HttpServletRequest request, HttpServletResponse response, Model model) {
		String scheduleName = getStringParameter("scheduleName");
		String taskName = getStringParameter("taskName");
		String taskBeanName = getStringParameter("taskBeanName");
		String strategyName = getStringParameter("strategyName");
		String startTime = getStringParameter("startTime");
		String endTime = getStringParameter("endTime");
		Integer fetchDataNumber = getIntegerParameter("fetchDataNumber", 100);
		Integer executeNumber = getIntegerParameter("executeNumber", 100);

		ValidateUtil.isNotBlank(scheduleName, "scheduleName不能为空!");
		ValidateUtil.isNotBlank(taskName, "taskName不能为空!");
		ValidateUtil.isNotBlank(taskBeanName, "taskBeanName不能为空!");
		ValidateUtil.isNotBlank(strategyName, "strategyName不能为空!");
		ValidateUtil.isNotBlank(startTime, "startTime不能为空!");
		ValidateUtil.isNotBlank(endTime, "endTime不能为空!");

		String extraInfo = null;
		scheduleManager.saveOrUpdateTask(scheduleName, taskName, taskBeanName, strategyName, fetchDataNumber, executeNumber, null, ScheduleParameter.SLEEP_MODEL, startTime, endTime,
				extraInfo);
		Result result = new Result(ResultCode.COMMON_SUCCESS, true);
		return result;
	}

}
