package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.material.BulkMaterialQueryParam;
import com.lxzl.erp.common.domain.material.MaterialModelQueryParam;
import com.lxzl.erp.common.domain.material.MaterialQueryParam;
import com.lxzl.erp.common.domain.material.pojo.BulkMaterial;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.material.pojo.MaterialImg;
import com.lxzl.erp.common.domain.material.pojo.MaterialModel;
import com.lxzl.erp.common.domain.system.pojo.Image;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    @RequestMapping(value = "uploadImage", method = RequestMethod.POST)
    public Result uploadImage(@RequestParam("file") MultipartFile[] file, HttpServletRequest request) {
        if (file.length == 0) {
            return new Result(ErrorCode.SYSTEM_ERROR, ErrorCode.getMessage(ErrorCode.SYSTEM_ERROR), false);
        }
        ServiceResult<String, List<MaterialImg>> serviceResult = materialService.uploadImage(file);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "deleteImage", method = RequestMethod.POST)
    public Result deleteImage(@RequestBody Image image, HttpServletRequest request) {
        ServiceResult<String, Integer> serviceResult = materialService.deleteImage(image.getImgId());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result add(@RequestBody Material material, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = materialService.addMaterial(material);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result update(@RequestBody Material material, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = materialService.updateMaterial(material);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Result delete(@RequestBody Material material, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = materialService.deleteMaterial(material.getMaterialNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryAllMaterial", method = RequestMethod.POST)
    public Result queryAllMaterial(@RequestBody MaterialQueryParam materialQueryParam, BindingResult validResult) {
        ServiceResult<String, Page<Material>> serviceResult = materialService.queryAllMaterial(materialQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryMaterialByNo", method = RequestMethod.POST)
    public Result queryMaterialByNo(@RequestBody MaterialQueryParam materialQueryParam, BindingResult validResult) {
        ServiceResult<String, Material> serviceResult = materialService.queryMaterialByNo(materialQueryParam.getMaterialNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryAllBulkMaterial", method = RequestMethod.POST)
    public Result queryAllBulkMaterial(@RequestBody BulkMaterialQueryParam bulkMaterialQueryParam, BindingResult validResult) {
        ServiceResult<String, Page<BulkMaterial>> serviceResult = materialService.queryAllBulkMaterial(bulkMaterialQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "addModel", method = RequestMethod.POST)
    public Result addModel(@RequestBody MaterialModel materialModel, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = materialService.addMaterialModel(materialModel);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "updateModel", method = RequestMethod.POST)
    public Result updateModel(@RequestBody MaterialModel materialModel, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = materialService.updateMaterialModel(materialModel);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryModel", method = RequestMethod.POST)
    public Result queryModel(@RequestBody MaterialModelQueryParam materialModelQueryParam, BindingResult validResult) {
        ServiceResult<String, Page<MaterialModel>> serviceResult = materialService.queryMaterialModel(materialModelQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }


    @Autowired
    private MaterialService materialService;

    @Autowired
    private ResultGenerator resultGenerator;

}
