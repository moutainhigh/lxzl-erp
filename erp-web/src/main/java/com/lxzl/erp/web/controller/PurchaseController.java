package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.company.pojo.SubCompany;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.se.common.domain.Result;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@RequestMapping("/purchase")
@Controller
@ControllerLog
public class PurchaseController {
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result addSubCompany(@RequestBody @Validated(AddGroup.class) SubCompany subCompany, BindingResult validResult) {
//        ServiceResult<String, Integer> serviceResult = companyService.addSubCompany(subCompany);
//        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
        return null;
    }
}
