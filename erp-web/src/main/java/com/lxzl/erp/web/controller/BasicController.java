package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.basic.BrandQueryParam;
import com.lxzl.erp.common.domain.basic.pojo.Brand;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.basic.BasicService;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 描述: 基础信息控制器
 *
 * @author gaochao
 * @date 2017-12-09 13:17
 */
@Controller
@ControllerLog
@RequestMapping("/basic")
public class BasicController extends BaseController {

    @Autowired
    private BasicService basicService;

    @Autowired
    private ResultGenerator resultGenerator;

    @RequestMapping(value = "queryAllBrand", method = RequestMethod.POST)
    public Result queryAllBrand(@RequestBody BrandQueryParam param, BindingResult validResult) {
        ServiceResult<String, Page<Brand>> serviceResult = basicService.queryAllBrand(param);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }


}
