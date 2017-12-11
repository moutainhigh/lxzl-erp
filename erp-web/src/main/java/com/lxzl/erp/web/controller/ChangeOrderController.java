package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.changeOrder.AddChangeOrderParam;
import com.lxzl.erp.common.domain.validGroup.changeOrder.ChangeReturnOrderGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.changeOrder.ChangeOrderService;
import com.lxzl.se.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/changeOrder")
@Controller
@ControllerLog
public class ChangeOrderController {

    @Autowired
    private ChangeOrderService changeOrderService;
    @Autowired
    private ResultGenerator resultGenerator;

    /**
     * 创建换货单
     * @param addChangeOrderParam
     * @param validResult
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result add(@RequestBody @Validated(ChangeReturnOrderGroup.class) AddChangeOrderParam addChangeOrderParam, BindingResult validResult) {

        ServiceResult<String, String> serviceResult = changeOrderService.add(addChangeOrderParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

//    /**
//     * 换货备货接口
//     * @param doReturnEquipmentParam
//     * @param validResult
//     * @return
//     */
//    @RequestMapping(value = " stockUpByChange", method = RequestMethod.POST)
//    public Result stockUpByChange(@RequestBody @Validated DoReturnEquipmentParam doReturnEquipmentParam, BindingResult validResult) {
//        ServiceResult<String, ProductEquipment> serviceResult = changeOrderService.stockUpByChange(doReturnEquipmentParam);
//        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
//    }
//    @RequestMapping(value = "doReturnMaterial", method = RequestMethod.POST)
//    public Result doReturnMaterial(@RequestBody @Validated DoReturnMaterialParam doReturnMaterialParam, BindingResult validResult) {
//        ServiceResult<String, Material> serviceResult = returnOrderService.doReturnMaterial(doReturnMaterialParam);
//        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
//    }
//    @RequestMapping(value = "detail", method = RequestMethod.POST)
//    public Result detail(@RequestBody @Validated(IdGroup.class) ReturnOrder returnOrder, BindingResult validResult) {
//        ServiceResult<String, ReturnOrder> serviceResult = returnOrderService.detail(returnOrder);
//        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
//    }
//    @RequestMapping(value = "page", method = RequestMethod.POST)
//    public Result page(@RequestBody ReturnOrderPageParam returnOrderPageParam, BindingResult validResult) {
//        ServiceResult<String, Page<ReturnOrder>> serviceResult = returnOrderService.page(returnOrderPageParam);
//        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
//    }
//    @RequestMapping(value = "end", method = RequestMethod.POST)
//    public Result end(@RequestBody @Validated(ExtendGroup.class)ReturnOrder returnOrder, BindingResult validResult) {
//        ServiceResult<String,String> serviceResult = returnOrderService.end(returnOrder);
//        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
//    }
//    @RequestMapping(value = "cancel", method = RequestMethod.POST)
//    public Result cancel(@RequestBody @Validated(IdGroup.class)ReturnOrder returnOrder, BindingResult validResult) {
//        ServiceResult<String,String> serviceResult = returnOrderService.cancel(returnOrder);
//        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
//    }
//    @RequestMapping(value = "pageReturnEquipment", method = RequestMethod.POST)
//    public Result pageReturnEquipment(@RequestBody @Validated(IdGroup.class)ReturnEquipmentPageParam returnEquipmentPageParam, BindingResult validResult) {
//        ServiceResult<String,Page<ReturnOrderProductEquipment>> serviceResult = returnOrderService.pageReturnEquipment(returnEquipmentPageParam);
//        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
//    }
//    @RequestMapping(value = "pageReturnBulk", method = RequestMethod.POST)
//    public Result pageReturnBulk(@RequestBody @Validated(IdGroup.class)ReturnBulkPageParam returnBulkPageParam, BindingResult validResult) {
//        ServiceResult<String,Page<ReturnOrderMaterialBulk>> serviceResult = returnOrderService.pageReturnBulk(returnBulkPageParam);
//        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
//    }
}
