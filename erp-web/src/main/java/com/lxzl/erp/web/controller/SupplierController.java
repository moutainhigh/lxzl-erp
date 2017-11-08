package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.supplier.SupplierQueryParam;
import com.lxzl.erp.common.domain.supplier.pojo.Supplier;
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

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private ResultGenerator resultGenerator;
}
