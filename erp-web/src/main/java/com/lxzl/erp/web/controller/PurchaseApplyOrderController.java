package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.purchaseApply.PurchaseApplyOrderCommitParam;
import com.lxzl.erp.common.domain.purchaseApply.PurchaseApplyOrderPageParam;
import com.lxzl.erp.common.domain.purchaseApply.pojo.PurchaseApplyOrder;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.ExtendGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.purchaseApply.PurchaseApplyOrderService;
import com.lxzl.se.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@RequestMapping("/purchaseApplyOrder")
@Controller
@ControllerLog
public class PurchaseApplyOrderController {
    @Autowired
    private PurchaseApplyOrderService purchaseApplyOrderService;
    @Autowired
    private ResultGenerator resultGenerator;

    /**
     * 新增采购申请单
     *
     * @param purchaseApplyOrder
     * @param validResult
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result add(@RequestBody @Validated(AddGroup.class) PurchaseApplyOrder purchaseApplyOrder, BindingResult validResult) {
        return resultGenerator.generate(purchaseApplyOrderService.add(purchaseApplyOrder));
    }

    /**
     * 修改采购申请单
     *
     * @param purchaseApplyOrder
     * @param validResult
     * @return
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result update(@RequestBody @Validated(UpdateGroup.class) PurchaseApplyOrder purchaseApplyOrder, BindingResult validResult) {
        return resultGenerator.generate(purchaseApplyOrderService.update(purchaseApplyOrder));
    }

    /**
     * 取消采购申请单
     *
     * @param purchaseApplyOrder
     * @param validResult
     * @return
     */
    @RequestMapping(value = "cancel", method = RequestMethod.POST)
    public Result deletePurchaseOrder(@RequestBody @Validated(IdGroup.class) PurchaseApplyOrder purchaseApplyOrder, BindingResult validResult) {
        return resultGenerator.generate(purchaseApplyOrderService.cancel(purchaseApplyOrder.getPurchaseApplyOrderNo()));
    }

    /**
     * 采购申请单提交审核
     *
     * @param purchaseApplyOrderCommitParam
     * @param validResult
     * @return
     */
    @RequestMapping(value = "commit", method = RequestMethod.POST)
    public Result commit(@RequestBody @Validated(ExtendGroup.class) PurchaseApplyOrderCommitParam purchaseApplyOrderCommitParam, BindingResult validResult) {
        return resultGenerator.generate(purchaseApplyOrderService.commit(purchaseApplyOrderCommitParam));
    }

    /**
     * 采购申请单分页
     *
     * @param purchaseApplyOrderPageParam
     * @param validResult
     * @return
     */
    @RequestMapping(value = "queryAll", method = RequestMethod.POST)
    public Result queryAll(@RequestBody PurchaseApplyOrderPageParam purchaseApplyOrderPageParam, BindingResult validResult) {
        return resultGenerator.generate(purchaseApplyOrderService.queryAll(purchaseApplyOrderPageParam));
    }

    /**
     * 采购申请单详情
     *
     * @param purchaseApplyOrder
     * @param validResult
     * @return
     */
    @RequestMapping(value = "queryByNo", method = RequestMethod.POST)
    public Result queryByNo(@RequestBody @Validated(IdGroup.class) PurchaseApplyOrder purchaseApplyOrder, BindingResult validResult) {
        return resultGenerator.generate(purchaseApplyOrderService.queryByNo(purchaseApplyOrder.getPurchaseApplyOrderNo()));
    }

}
