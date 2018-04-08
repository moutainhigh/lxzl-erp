package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.interfaceSwitch.SwitchQueryParam;
import com.lxzl.erp.common.domain.interfaceSwitch.pojo.Switch;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.interfaceSwitch.SwitchService;
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
@RequestMapping("/interfaceSwitch")
public class InterfaceSwitchController {

    @Autowired
    SwitchService switchService;

    @Autowired
    ResultGenerator resultGenerator;

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result add(@RequestBody @Validated(AddGroup.class) Switch interfaceSwitch, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = switchService.add(interfaceSwitch);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result update(@RequestBody @Validated(UpdateGroup.class) Switch interfaceSwitch, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = switchService.update(interfaceSwitch);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "page", method = RequestMethod.POST)
    public Result page(@RequestBody SwitchQueryParam switchQueryParam, BindingResult validResult) {
        ServiceResult<String, Page<Switch>> serviceResult = switchService.page(switchQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Result delete(@RequestBody @Validated(IdGroup.class) Switch interfaceSwitch, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = switchService.delete(interfaceSwitch);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

}
