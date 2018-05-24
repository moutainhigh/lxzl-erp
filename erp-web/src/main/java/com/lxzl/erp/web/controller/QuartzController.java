package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.quartz.QuartzJob;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.quartz.QuartzService;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * <p>Description: </p>
 *
 * @Auther: huahongbin
 * @Date: 2018/5/23
 */
@Controller
@ControllerLog
@RequestMapping("/quartz")
public class QuartzController extends BaseController {

    @RequestMapping(value = "saveJob", method = RequestMethod.POST)
    public Result saveJob(@RequestBody QuartzJob quartzJob, BindingResult bindingResult) {
        return resultGenerator.generate(quartzService.saveJob(quartzJob));
    }

    @RequestMapping(value = "updateJob", method = RequestMethod.POST)
    public Result updateJob(@RequestBody QuartzJob quartzJob, BindingResult bindingResult) {
        return resultGenerator.generate(quartzService.updateJob(quartzJob));
    }

    @RequestMapping(value = "saveOrUpdateJob", method = RequestMethod.POST)
    public Result saveOrUpdateJob(@RequestBody QuartzJob quartzJob, BindingResult bindingResult) {
        return resultGenerator.generate(quartzService.saveOrUpdateJob(quartzJob));
    }

    @RequestMapping(value = "pauseTrigger", method = RequestMethod.POST)
    public Result pauseTrigger(@RequestBody QuartzJob quartzJob, BindingResult bindingResult) {
        return resultGenerator.generate(quartzService.pauseTrigger(quartzJob));
    }

    @RequestMapping(value = "resumeTrigger", method = RequestMethod.POST)
    public Result resumeTrigger(@RequestBody QuartzJob quartzJob, BindingResult bindingResult) {
        return resultGenerator.generate(quartzService.resumeTrigger(quartzJob));
    }

    @RequestMapping(value = "deleteTrigger", method = RequestMethod.POST)
    public Result deleteTrigger(@RequestBody QuartzJob quartzJob, BindingResult bindingResult) {
        return resultGenerator.generate(quartzService.deleteTrigger(quartzJob));
    }

    @RequestMapping(value = "getAllJobs", method = RequestMethod.POST)
    public Result getAllJobs() {
        return resultGenerator.generate(quartzService.getAllJobs());
    }

    @RequestMapping(value = "getSchedulerJobs", method = RequestMethod.POST)
    public Result getSchedulerJobs(@RequestBody QuartzJob quartzJob, BindingResult bindingResult) {
        return resultGenerator.generate(quartzService.getSchedulerJobs(quartzJob.getSchedName()));
    }

    @RequestMapping(value = "getJob", method = RequestMethod.POST)
    public Result getJob(@RequestBody QuartzJob quartzJob, BindingResult bindingResult) {
        return resultGenerator.generate(quartzService.getJob(quartzJob));
    }

    @RequestMapping(value = "getJobRunningInfo", method = RequestMethod.POST)
    public Result getJobRunningInfo(@RequestBody QuartzJob quartzJob, BindingResult bindingResult) {
        return resultGenerator.generate(quartzService.getJobRunningInfo(quartzJob));
    }

    @Autowired
    private QuartzService quartzService;

    @Autowired
    private ResultGenerator resultGenerator;
}
