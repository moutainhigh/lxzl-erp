package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.customer.CustomerCompanyQueryParam;
import com.lxzl.erp.common.domain.customer.CustomerConsignInfoQueryParam;
import com.lxzl.erp.common.domain.customer.CustomerPersonQueryParam;
import com.lxzl.erp.common.domain.customer.CustomerQueryParam;
import com.lxzl.erp.common.domain.customer.pojo.*;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import com.lxzl.erp.common.domain.validGroup.customer.UpdateCustomerCompanyGroup;
import com.lxzl.erp.common.domain.validGroup.customer.UpdateCustomerPersonGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.customer.CustomerService;
import com.lxzl.erp.common.domain.validGroup.customer.AddCustomerCompanyGroup;
import com.lxzl.erp.common.domain.validGroup.customer.AddCustomerPersonGroup;
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

    @RequestMapping(value = "addCompany", method = RequestMethod.POST)
    public Result addCompany(@RequestBody @Validated(AddCustomerCompanyGroup.class) Customer customer, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = customerService.addCompany(customer);
        return resultGenerator.generate(serviceResult);
    }

    @RequestMapping(value = "addPerson", method = RequestMethod.POST)
    public Result addPerson(@RequestBody @Validated(AddCustomerPersonGroup.class) Customer customer, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = customerService.addPerson(customer);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "updateCompany", method = RequestMethod.POST)
    public Result updateCompany(@RequestBody @Validated(UpdateCustomerCompanyGroup.class) Customer customer, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = customerService.updateCompany(customer);
        return resultGenerator.generate(serviceResult);
    }

    @RequestMapping(value = "updatePerson", method = RequestMethod.POST)
    public Result updatePerson(@RequestBody @Validated(UpdateCustomerPersonGroup.class) Customer customer, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = customerService.updatePerson(customer);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "commitCustomer", method = RequestMethod.POST)
    public Result commitCustomer(@RequestBody @Validated(UpdateCustomerPersonGroup.class) Customer customer, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = customerService.commitCustomer(customer.getCustomerNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "verifyCustomer", method = RequestMethod.POST)
    public Result verifyCustomer(@RequestBody @Validated(UpdateCustomerPersonGroup.class) Customer customer, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = customerService.verifyCustomer(customer.getCustomerNo(), customer.getCustomerStatus(), customer.getVerifyRemark());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "pageCustomerCompany", method = RequestMethod.POST)
    public Result pageCustomerCompany(@RequestBody CustomerCompanyQueryParam customerCompanyQueryParam, BindingResult validResult) {
        ServiceResult<String, Page<Customer>> serviceResult = customerService.pageCustomerCompany(customerCompanyQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "detailCustomer", method = RequestMethod.POST)
    public Result detailCustomer(@RequestBody CustomerQueryParam customerQueryParam, BindingResult validResult) {
        ServiceResult<String, Customer> serviceResult = customerService.detailCustomer(customerQueryParam.getCustomerNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "pageCustomerPerson", method = RequestMethod.POST)
    public Result pageCustomerPerson(@RequestBody CustomerPersonQueryParam customerPersonQueryParam, BindingResult validResult) {
        ServiceResult<String, Page<Customer>> serviceResult = customerService.pageCustomerPerson(customerPersonQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "detailCustomerCompany", method = RequestMethod.POST)
    public Result detailCustomerCompany(@RequestBody @Validated(IdGroup.class) Customer customer, BindingResult validResult) {
        ServiceResult<String, Customer> serviceResult = customerService.detailCustomerCompany(customer);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "detailCustomerPerson", method = RequestMethod.POST)
    public Result detailCustomerPerson(@RequestBody @Validated(IdGroup.class) Customer customer, BindingResult validResult) {
        ServiceResult<String, Customer> serviceResult = customerService.detailCustomerPerson(customer);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "updateRisk", method = RequestMethod.POST)
    public Result update(@RequestBody @Validated(UpdateGroup.class) CustomerRiskManagement customerRiskManagement, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = customerService.updateRisk(customerRiskManagement);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "addCustomerConsignInfo", method = RequestMethod.POST)
    public Result addCustomerConsignInfo(@RequestBody @Validated(AddGroup.class) CustomerConsignInfo customerConsignInfo, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = customerService.addCustomerConsignInfo(customerConsignInfo);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "updateCustomerConsignInfo", method = RequestMethod.POST)
    public Result updateCustomerConsignInfo(@RequestBody @Validated(UpdateGroup.class) CustomerConsignInfo customerConsignInfo, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = customerService.updateCustomerConsignInfo(customerConsignInfo);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "deleteCustomerConsignInfo", method = RequestMethod.POST)
    public Result deleteCustomerConsignInfo(@RequestBody @Validated(IdGroup.class) CustomerConsignInfo customerConsignInfo, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = customerService.deleteCustomerConsignInfo(customerConsignInfo);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "detailCustomerConsignInfo", method = RequestMethod.POST)
    public Result detailCustomerConsignInfo(@RequestBody @Validated(IdGroup.class) CustomerConsignInfo customerConsignInfo, BindingResult validResult) {
        ServiceResult<String, CustomerConsignInfo> serviceResult = customerService.detailCustomerConsignInfo(customerConsignInfo);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "pageCustomerConsignInfo", method = RequestMethod.POST)
    public Result pageCustomerConsignInfo(@RequestBody @Validated(IdGroup.class) CustomerConsignInfoQueryParam customerConsignInfoQueryParam, BindingResult validResult) {
        ServiceResult<String, Page<CustomerConsignInfo>> serviceResult = customerService.pageCustomerConsignInfo(customerConsignInfoQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "updateAddressIsMain", method = RequestMethod.POST)
    public Result updateAddressIsMain(@RequestBody @Validated(IdGroup.class) CustomerConsignInfo customerConsignInfo, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = customerService.updateAddressIsMain(customerConsignInfo);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

}
