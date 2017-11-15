package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.customer.CustomerCompanyQueryParam;
import com.lxzl.erp.common.domain.customer.CustomerPersonQueryParam;
import com.lxzl.erp.common.domain.customer.pojo.Customer;
import com.lxzl.erp.common.domain.customer.pojo.CustomerCompany;
import com.lxzl.erp.common.domain.customer.pojo.CustomerPerson;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.customer.CustomerService;
import com.lxzl.se.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@RequestMapping("/customer")
@Controller
@ControllerLog
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ResultGenerator resultGenerator;

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result add(@RequestBody @Validated(AddGroup.class) Customer customer, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = customerService.add(customer);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result update(@RequestBody @Validated(UpdateGroup.class) Customer customer, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = customerService.update(customer);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
    @RequestMapping(value = "pageCustomerCompany", method = RequestMethod.POST)
    public Result pageCustomerCompany(@RequestBody CustomerCompanyQueryParam customerCompanyQueryParam, BindingResult validResult) {
        ServiceResult<String, Page<CustomerCompany>> serviceResult = customerService.pageCustomerCompany(customerCompanyQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "pageCustomerPerson", method = RequestMethod.POST)
    public Result pageCustomerPerson(@RequestBody CustomerPersonQueryParam customerPersonQueryParam, BindingResult validResult) {
        ServiceResult<String, Page<CustomerPerson>> serviceResult = customerService.pageCustomerPerson(customerPersonQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
}
