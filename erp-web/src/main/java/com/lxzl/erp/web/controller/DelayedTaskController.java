package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.delayedTask.DelayedTaskQueryParam;
import com.lxzl.erp.common.domain.delayedTask.pojo.DelayedTask;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.delayedTask.DelayedTaskService;
import com.lxzl.se.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author: Sunzhipeng
 * @Description:
 * @Date: Created in 2018\7\27 0027 14:36
 */
@RequestMapping("/delayedTask")
@Controller
@ControllerLog
public class DelayedTaskController {
    @Autowired
    private DelayedTaskService delayedTaskService;
    @Autowired
    private ResultGenerator resultGenerator;

    /**
     * 添加延迟任务列表
     * @param DelayedTask
     * @param validResult
     * @return
     */
    @RequestMapping(value = "addDelayedTask",method = RequestMethod.POST)
    public Result addDelayedTask(@RequestBody @Validated(AddGroup.class)DelayedTask DelayedTask , BindingResult validResult) throws Exception {
        ServiceResult<String, DelayedTask> serviceResult = delayedTaskService.addDelayedTask(DelayedTask);
        return resultGenerator.generate(serviceResult);

    }

    /**
     * 查询延迟任务列表
     * @param DelayedTaskQueryParam
     * @param validResult
     * @return
     */
    @RequestMapping(value = "pageDelayedTask",method = RequestMethod.POST)
    public Result pageDelayedTask(@RequestBody @Validated(AddGroup.class)DelayedTaskQueryParam DelayedTaskQueryParam , BindingResult validResult) {
        ServiceResult<String, Page<DelayedTask>> serviceResult = delayedTaskService.pageDelayedTask(DelayedTaskQueryParam);
        return resultGenerator.generate(serviceResult);
    }
}
