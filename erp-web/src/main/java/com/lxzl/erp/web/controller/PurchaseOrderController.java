package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.purchase.PurchaseOrderCommitParam;
import com.lxzl.erp.common.domain.purchase.pojo.PurchaseOrder;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.ExtendGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.purchase.PurchaseOrderService;
import com.lxzl.se.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@RequestMapping("/purchaseOrder")
@Controller
@ControllerLog
public class PurchaseOrderController {
    @Autowired
    private PurchaseOrderService purchaseOrderService;
    @Autowired
    private ResultGenerator resultGenerator;

    /**
     * 新增采购单
     * @param purchaseOrder
     * @param validResult
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result addPurchaseOrder(@RequestBody @Validated(AddGroup.class) PurchaseOrder purchaseOrder, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = purchaseOrderService.add(purchaseOrder);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
    /**
     * 修改采购单
     * @param purchaseOrder
     * @param validResult
     * @return
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result updatePurchaseOrder(@RequestBody @Validated(UpdateGroup.class) PurchaseOrder purchaseOrder, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = purchaseOrderService.update(purchaseOrder);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
    /**
     * 采购单提交审核
     * @param purchaseOrderCommitParam
     * @param validResult
     * @return
     */
    @RequestMapping(value = "commit", method = RequestMethod.POST)
    public Result commit(@RequestBody @Validated(ExtendGroup.class) PurchaseOrderCommitParam purchaseOrderCommitParam, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = purchaseOrderService.commit(purchaseOrderCommitParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
}
