package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.material.BulkMaterialQueryParam;
import com.lxzl.erp.common.domain.material.pojo.BulkMaterial;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.material.BulkMaterialService;
import com.lxzl.se.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@RequestMapping("/bulkMaterial")
@Controller
@ControllerLog
public class BulkMaterialController {

    @Autowired
    private BulkMaterialService bulkMaterialService;

    @Autowired
    private ResultGenerator resultGenerator;

    @RequestMapping(value = "dismantleBulkMaterial", method = RequestMethod.POST)
    public Result dismantleBulkMaterial(@RequestBody @Validated(IdGroup.class) BulkMaterial bulkMaterial, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = bulkMaterialService.dismantleBulkMaterial(bulkMaterial);
        return resultGenerator.generate(serviceResult);
    }

    @RequestMapping(value = "installBulkMaterial", method = RequestMethod.POST)
    public Result installBulkMaterial(@RequestBody BulkMaterialQueryParam bulkMaterialQueryParam, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = bulkMaterialService.installBulkMaterial(bulkMaterialQueryParam);
        return resultGenerator.generate(serviceResult);
    }

}
