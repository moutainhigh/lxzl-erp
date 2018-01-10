package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.supplier.SupplierQueryParam;
import com.lxzl.erp.common.domain.supplier.pojo.Supplier;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.supplier.SupplierService;
import com.lxzl.se.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-08 20:54
 */
@Controller
@ControllerLog
@RequestMapping("/supplier")
public class SupplierController {


    @RequestMapping(value = "getSupplier", method = RequestMethod.POST)
    public Result getSupplier(@RequestBody SupplierQueryParam supplierQueryParam, BindingResult validResult) {
        ServiceResult<String, Page<Supplier>> serviceResult = supplierService.getSupplier(supplierQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "getSupplierByNo", method = RequestMethod.POST)
    public Result getSupplierByNo(@RequestBody SupplierQueryParam supplierQueryParam, BindingResult validResult) {
        ServiceResult<String, Supplier> serviceResult = supplierService.getSupplierByNo(supplierQueryParam.getSupplierNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result add(@RequestBody @Validated({AddGroup.class})Supplier supplier, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = supplierService.addSupplier(supplier);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result update(@RequestBody @Validated({UpdateGroup.class})Supplier supplier, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = supplierService.updateSupplier(supplier);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Result delete(@RequestBody Supplier supplier, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = supplierService.deleteSupplier(supplier.getSupplierNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private ResultGenerator resultGenerator;
}
