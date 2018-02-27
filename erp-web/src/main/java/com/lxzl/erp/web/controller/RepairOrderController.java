package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.material.pojo.BulkMaterial;
import com.lxzl.erp.common.domain.purchase.pojo.PurchaseReceiveOrder;
import com.lxzl.erp.common.domain.repairOrder.*;
import com.lxzl.erp.common.domain.repairOrder.pojo.RepairOrder;
import com.lxzl.erp.common.domain.repairOrder.pojo.RepairOrderBulkMaterial;
import com.lxzl.erp.common.domain.repairOrder.pojo.RepairOrderEquipment;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.repairOrder.RepairOrderService;
import com.lxzl.se.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 15:40 2017/12/14
 * @Modified By:
 */

@RequestMapping("/repairOrder")
@Controller
@ControllerLog
public class RepairOrderController {

    @Autowired
    private RepairOrderService repairOrderService;
    @Autowired
    private ResultGenerator resultGenerator;

    @RequestMapping(value = "addRepairOrder", method = RequestMethod.POST)
    public Result addRepairOrder(@RequestBody @Validated(AddGroup.class)RepairOrder repairOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = repairOrderService.addRepairOrder(repairOrder);
        return resultGenerator.generate(serviceResult);
    }

    @RequestMapping(value = "commitRepairOrder", method = RequestMethod.POST)
    public Result commitRepairOrder(@RequestBody RepairOrderCommitParam repairOrderCommitParam, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = repairOrderService.commitRepairOrder(repairOrderCommitParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "receiveRepairOrder", method = RequestMethod.POST)
    public Result receiveRepairOrder(@RequestBody @Validated(IdGroup.class)RepairOrder repairOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = repairOrderService.receiveRepairOrder(repairOrder.getRepairOrderNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "cancelRepairOrder", method = RequestMethod.POST)
    public Result cancelRepairOrder(@RequestBody @Validated(IdGroup.class)RepairOrder repairOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = repairOrderService.cancelRepairOrder(repairOrder);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "updateRepairOrder", method = RequestMethod.POST)
    public Result updateRepairOrder(@RequestBody @Validated({IdGroup.class,AddGroup.class})RepairOrder repairOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = repairOrderService.updateRepairOrder(repairOrder);
        return resultGenerator.generate(serviceResult);
    }

    @RequestMapping(value = "queryRepairOrderByNo", method = RequestMethod.POST)
    public Result queryRepairOrderByNo(@RequestBody  @Validated(IdGroup.class)RepairOrder repairOrder, BindingResult validResult) {
        ServiceResult<String, RepairOrder> serviceResult = repairOrderService.queryRepairOrderByNo(repairOrder.getRepairOrderNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "pageRepairOrder", method = RequestMethod.POST)
    public Result pageRepairOrder(@RequestBody RepairOrderQueryParam repairOrderQueryParam, BindingResult validResult) {
        ServiceResult<String, Page<RepairOrder>> serviceResult = repairOrderService.pageRepairOrder(repairOrderQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "pageRepairEquipment", method = RequestMethod.POST)
    public Result pageRepairEquipment(@RequestBody  RepairOrderEquipmentQueryParam repairOrderEquipmentQueryParam, BindingResult validResult) {
        ServiceResult<String, Page<RepairOrderEquipment>> serviceResult = repairOrderService.pageRepairEquipment(repairOrderEquipmentQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "pageRepairBulkMaterial", method = RequestMethod.POST)
    public Result pageRepairBulkMaterial(@RequestBody RepairOrderBulkMaterialQueryParam repairOrderBulkMaterialQueryParam, BindingResult validResult) {
        ServiceResult<String, Page<RepairOrderBulkMaterial>> serviceResult = repairOrderService.pageRepairBulkMaterial(repairOrderBulkMaterialQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "fix", method = RequestMethod.POST)
    public Result fix(@RequestBody FixRepairOrderQueryParam fixRepairOrderQueryParam, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = repairOrderService.fix(fixRepairOrderQueryParam.getRepairOrderEquipmentList(),fixRepairOrderQueryParam.getRepairOrderBulkMaterialList());
        return resultGenerator.generate(serviceResult);
    }

    @RequestMapping(value = "end", method = RequestMethod.POST)
    public Result end(@RequestBody @Validated(IdGroup.class)RepairOrder repairOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = repairOrderService.end(repairOrder);
        return resultGenerator.generate(serviceResult);
    }

}
