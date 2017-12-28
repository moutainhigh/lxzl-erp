package com.lxzl.erp.web.controller;


import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.jointProduct.pojo.JointProduct;
import com.lxzl.erp.common.domain.jointProduct.JointProductQueryParam;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.jointProduct.JointProductService;
import com.lxzl.se.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@RequestMapping("jointProduct")
@Controller
@ControllerLog
public class JointProductController {
    @Autowired
    private JointProductService jointProductService;

    @Autowired
    private ResultGenerator resultGenerator;

    /**
     * 增加组合商品
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result addJointProduct(@RequestBody @Validated(AddGroup.class) JointProduct jointProduct, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = jointProductService.addJointProduct(jointProduct);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 修改组合商品
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result updateJointProduct(@RequestBody @Validated(UpdateGroup.class) JointProduct jointProduct, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = jointProductService.updateJointProduct(jointProduct);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 删除组合商品
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Result deleteJointProduct(@RequestBody @Validated(IdGroup.class) JointProduct jointProduct, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = jointProductService.deleteJointProduct(jointProduct);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 查询商品详情
     * */
    @RequestMapping(value = "query", method = RequestMethod.POST)
    public Result queryJointProductByJointProductId(@RequestBody @Validated(IdGroup.class) JointProduct jointProduct, BindingResult validResult) {
        ServiceResult<String, JointProduct> serviceResult = jointProductService.queryJointProductByJointProductId(jointProduct.getJointProductId());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 分页查询组合商品
     */
    @RequestMapping(value = "page", method = RequestMethod.POST)
    public Result pageJointProduct(@RequestBody JointProductQueryParam jointProductQueryParam) {
        ServiceResult<String, Page<JointProduct>> serviceResult = jointProductService.pageJointProduct(jointProductQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

}
