package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.product.pojo.ProductEquipment;
import com.lxzl.erp.common.domain.returnOrder.*;
import com.lxzl.erp.common.domain.returnOrder.pojo.ReturnOrder;
import com.lxzl.erp.common.domain.returnOrder.pojo.ReturnOrderMaterialBulk;
import com.lxzl.erp.common.domain.returnOrder.pojo.ReturnOrderProductEquipment;
import com.lxzl.erp.common.domain.validGroup.ExtendGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.returnOrder.ReturnOrderService;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 描述: 租赁退还订单相关接口
 *
 * @author liuke
 * @date 2017-11-27 10:06
 */
@Controller
@ControllerLog
@RequestMapping("/returnOrder")
public class ReturnOrderController extends BaseController {


    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result add(@RequestBody @Validated AddReturnOrderParam addReturnOrderParam, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = returnOrderService.add(addReturnOrderParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result update(@RequestBody @Validated UpdateReturnOrderParam updateReturnOrderParam, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = returnOrderService.update(updateReturnOrderParam);
        return resultGenerator.generate(serviceResult);
    }

    @RequestMapping(value = "commit", method = RequestMethod.POST)
    public Result commit(@RequestBody @Validated ReturnOrderCommitParam returnOrderCommitParam, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = returnOrderService.commit(returnOrderCommitParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "doReturnEquipment", method = RequestMethod.POST)
    public Result doReturnEquipment(@RequestBody @Validated DoReturnEquipmentParam doReturnEquipmentParam, BindingResult validResult) {
        ServiceResult<String, ProductEquipment> serviceResult = returnOrderService.doReturnEquipment(doReturnEquipmentParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "doReturnMaterial", method = RequestMethod.POST)
    public Result doReturnMaterial(@RequestBody @Validated DoReturnMaterialParam doReturnMaterialParam, BindingResult validResult) {
        ServiceResult<String, Material> serviceResult = returnOrderService.doReturnMaterial(doReturnMaterialParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "detail", method = RequestMethod.POST)
    public Result detail(@RequestBody @Validated(IdGroup.class) ReturnOrder returnOrder, BindingResult validResult) {
        ServiceResult<String, ReturnOrder> serviceResult = returnOrderService.detail(returnOrder);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "page", method = RequestMethod.POST)
    public Result page(@RequestBody ReturnOrderPageParam returnOrderPageParam, BindingResult validResult) {
        ServiceResult<String, Page<ReturnOrder>> serviceResult = returnOrderService.page(returnOrderPageParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "end", method = RequestMethod.POST)
    public Result end(@RequestBody @Validated(ExtendGroup.class) ReturnOrder returnOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = returnOrderService.end(returnOrder);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "cancel", method = RequestMethod.POST)
    public Result cancel(@RequestBody @Validated(IdGroup.class) ReturnOrder returnOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = returnOrderService.cancel(returnOrder);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "pageReturnEquipment", method = RequestMethod.POST)
    public Result pageReturnEquipment(@RequestBody @Validated(IdGroup.class) ReturnEquipmentPageParam returnEquipmentPageParam, BindingResult validResult) {
        ServiceResult<String, Page<ReturnOrderProductEquipment>> serviceResult = returnOrderService.pageReturnEquipment(returnEquipmentPageParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "pageReturnBulk", method = RequestMethod.POST)
    public Result pageReturnBulk(@RequestBody @Validated(IdGroup.class) ReturnBulkPageParam returnBulkPageParam, BindingResult validResult) {
        ServiceResult<String, Page<ReturnOrderMaterialBulk>> serviceResult = returnOrderService.pageReturnBulk(returnBulkPageParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @Autowired
    private ResultGenerator resultGenerator;

    @Autowired
    private ReturnOrderService returnOrderService;
}
