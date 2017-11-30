package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.product.pojo.ProductEquipment;
import com.lxzl.erp.common.domain.returnOrder.AddReturnOrderParam;
import com.lxzl.erp.common.domain.returnOrder.DoReturnEquipmentParam;
import com.lxzl.erp.common.domain.validGroup.returnOrder.AddReturnOrderGroup;
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
    public Result add(@RequestBody @Validated(AddReturnOrderGroup.class) AddReturnOrderParam addReturnOrderParam, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = returnOrderService.create(addReturnOrderParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
    @RequestMapping(value = "doReturnEquipment", method = RequestMethod.POST)
    public Result doReturnEquipment(@RequestBody @Validated DoReturnEquipmentParam doReturnEquipmentParam, BindingResult validResult) {
        ServiceResult<String, ProductEquipment> serviceResult = returnOrderService.doReturnEquipment(doReturnEquipmentParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @Autowired
    private ResultGenerator resultGenerator;

    @Autowired
    private ReturnOrderService returnOrderService;
}