package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.material.BulkMaterialQueryParam;
import com.lxzl.erp.common.domain.material.MaterialQueryParam;
import com.lxzl.erp.common.domain.material.pojo.BulkMaterial;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.material.MaterialService;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-09 20:58
 */
@Controller
@ControllerLog
@RequestMapping("/material")
public class MaterialController extends BaseController {


    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result add(@RequestBody Material material, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = materialService.addMaterial(material);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result update(@RequestBody Material material, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = materialService.addMaterial(material);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryAllMaterial", method = RequestMethod.POST)
    public Result queryAllMaterial(@RequestBody MaterialQueryParam materialQueryParam, BindingResult validResult) {
        ServiceResult<String, Page<Material>> serviceResult = materialService.queryAllMaterial(materialQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryBulkMaterialByMaterialId", method = RequestMethod.POST)
    public Result queryBulkMaterialByMaterialId(@RequestBody BulkMaterialQueryParam bulkMaterialQueryParam, BindingResult validResult) {
        ServiceResult<String, Page<BulkMaterial>> serviceResult = materialService.queryBulkMaterialByMaterialId(bulkMaterialQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }


    @Autowired
    private MaterialService materialService;

    @Autowired
    private ResultGenerator resultGenerator;

}
