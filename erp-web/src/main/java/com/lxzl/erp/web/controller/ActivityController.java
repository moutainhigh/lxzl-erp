package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.activity.ActivityOrderParam;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.service.activity.ActivityService;
import com.lxzl.se.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@ControllerLog
@RequestMapping("/activity")
public class ActivityController {
    @Autowired
    ActivityService activityService;

    @RequestMapping(value = "/getActivityOrder", method = RequestMethod.POST)
    public Result getActivityorder(@RequestBody @Validated ActivityOrderParam param, BindingResult validResult) {
        return activityService.getActivityOrder(param).getResult();

    }


}
