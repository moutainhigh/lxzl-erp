package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.company.SubCompanyQueryParam;
import com.lxzl.erp.common.domain.company.pojo.Department;
import com.lxzl.erp.common.domain.company.pojo.SubCompany;
import com.lxzl.erp.common.domain.user.DepartmentQueryParam;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.company.CompanyService;
import com.lxzl.se.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@RequestMapping("/company")
@Controller
@ControllerLog
public class CompanyController {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private ResultGenerator resultGenerator;

    @RequestMapping(value = "addSubCompany", method = RequestMethod.POST)
    public Result addSubCompany(@RequestBody @Validated(AddGroup.class) SubCompany subCompany, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = companyService.addSubCompany(subCompany);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
    @RequestMapping(value = "pageSubCompany", method = RequestMethod.POST)
    public Result pageSubCompany(@RequestBody SubCompanyQueryParam subCompanyQueryParam, BindingResult validResult) {
        ServiceResult<String, Page<SubCompany>> serviceResult = companyService.subCompanyPage(subCompanyQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "getDepartmentList", method = RequestMethod.POST)
    public Result getDepartmentList(@RequestBody DepartmentQueryParam departmentQueryParam) {
        ServiceResult<String, List<Department>> serviceResult = companyService.getDepartmentList(departmentQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

}
