package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.functionSwitch.SwitchQueryParam;
import com.lxzl.erp.common.domain.functionSwitch.pojo.Switch;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.functionSwitch.SwitchService;
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
 * @Date : Created in 2018/4/4
 * @Time : Created in 16:14
 */
@Controller
@ControllerLog
@RequestMapping("/functionSwitch")
public class FunctionSwitchController {

    @Autowired
    SwitchService switchService;

    @Autowired
    ResultGenerator resultGenerator;

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result add(@RequestBody @Validated(AddGroup.class) Switch functionSwitch, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = switchService.add(functionSwitch);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result update(@RequestBody @Validated(UpdateGroup.class) Switch functionSwitch, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = switchService.update(functionSwitch);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "page", method = RequestMethod.POST)
    public Result page(@RequestBody SwitchQueryParam switchQueryParam, BindingResult validResult) {
        ServiceResult<String, Page<Switch>> serviceResult = switchService.page(switchQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Result delete(@RequestBody @Validated(IdGroup.class) Switch functionSwitch, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = switchService.delete(functionSwitch);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

}
