package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.taskScheduler.TaskExecutorCommitParam;
import com.lxzl.erp.common.domain.taskScheduler.TaskExecutorQueryParam;
import com.lxzl.erp.common.domain.taskScheduler.TaskSchedulerCommitParam;
import com.lxzl.erp.common.domain.taskScheduler.TriggerCommitParam;
import com.lxzl.erp.common.domain.taskScheduler.pojo.TaskExecutor;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.QueryGroup;
import com.lxzl.erp.common.domain.validGroup.TaskTrigger.DeleteTaskTriggerGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.taskScheduler.TaskSchedulerService;
import com.lxzl.se.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/7/25
 * @Time : Created in 21:32
 */
@RequestMapping("/taskScheduler")
@ControllerLog
@Controller
public class TaskSchedulerController {

    @Autowired
    private TaskSchedulerService taskSchedulerService;

    @Autowired
    private ResultGenerator resultGenerator;

    @RequestMapping(value = "initTaskScheduler", method = RequestMethod.POST)
    public Result initTask(@RequestBody @Validated(AddGroup.class) TaskSchedulerCommitParam taskSchedulerCommitParam, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = taskSchedulerService.initTaskScheduler(taskSchedulerCommitParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "updateTaskScheduler", method = RequestMethod.POST)
    public Result updateTaskScheduler(@RequestBody @Validated(UpdateGroup.class) TaskSchedulerCommitParam taskSchedulerCommitParam, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = taskSchedulerService.updateTaskScheduler(taskSchedulerCommitParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "pauseTaskTrigger", method = RequestMethod.POST)
    public Result pauseTaskTrigger(@RequestBody @Validated(QueryGroup.class) TriggerCommitParam triggerCommitParam, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = taskSchedulerService.pauseTaskTrigger(triggerCommitParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "resumeTaskTrigger", method = RequestMethod.POST)
    public Result resumeTaskTrigger(@RequestBody @Validated(QueryGroup.class) TriggerCommitParam triggerCommitParam, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = taskSchedulerService.resumeTaskTrigger(triggerCommitParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "deleteTaskTrigger", method = RequestMethod.POST)
    public Result deleteTrigger(@RequestBody @Validated(DeleteTaskTriggerGroup.class) TriggerCommitParam triggerCommitParam, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = taskSchedulerService.deleteTaskTrigger(triggerCommitParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "updateTaskExecutor", method = RequestMethod.POST)
    public Result deleteTrigger(@RequestBody @Validated(UpdateGroup.class) TaskExecutorCommitParam taskExecutorCommitParam, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = taskSchedulerService.updateTaskExecutor(taskExecutorCommitParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "pageTaskExecutor", method = RequestMethod.POST)
    public Result pageTaskExecutor(@RequestBody TaskExecutorQueryParam taskExecutorQueryParam, BindingResult validResult) {
        ServiceResult<String, Page<TaskExecutor>> serviceResult = taskSchedulerService.pageTaskExecutor(taskExecutorQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "detailTaskExecutor", method = RequestMethod.POST)
    public Result detailTaskExecutor(@RequestBody @Validated(QueryGroup.class) TaskExecutorQueryParam taskExecutorQueryParam, BindingResult validResult) {
        ServiceResult<String, TaskExecutor> serviceResult = taskSchedulerService.detailTaskExecutor(taskExecutorQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
}
