package com.lxzl.erp.web.controller;


import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.customer.pojo.CustomerRiskManagement;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.ExtendGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.customer.CustomerRiskManagementService;
import com.lxzl.se.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/customerRiskManagement")
@Controller
@ControllerLog
public class CustomerRiskManagementController {
    @Autowired
    private CustomerRiskManagementService customerRiskManagementService;
    @Autowired
    private ResultGenerator resultGenerator;

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result add(@RequestBody @Validated(AddGroup.class) CustomerRiskManagement customerRiskManagement, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = customerRiskManagementService.add(customerRiskManagement);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result update(@RequestBody @Validated(UpdateGroup.class) CustomerRiskManagement customerRiskManagement, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = customerRiskManagementService.update(customerRiskManagement);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "detail", method = RequestMethod.POST)
    public Result detail(@RequestBody @Validated(ExtendGroup.class)CustomerRiskManagement customerRiskManagement, BindingResult validResult) {
        ServiceResult<String,CustomerRiskManagement> serviceResult = customerRiskManagementService.detail(customerRiskManagement.getCustomerNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Result delete(@RequestBody @Validated(IdGroup.class)CustomerRiskManagement customerRiskManagement, BindingResult validResult) {
        String errorCode = customerRiskManagementService.delete(customerRiskManagement.getCustomerRiskManagementId());
        return resultGenerator.generate(errorCode);
    }
}
