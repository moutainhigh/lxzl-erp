package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.purchase.PurchaseDeliveryOrderQueryParam;
import com.lxzl.erp.common.domain.purchase.PurchaseOrderCommitParam;
import com.lxzl.erp.common.domain.purchase.PurchaseOrderQueryParam;
import com.lxzl.erp.common.domain.purchase.pojo.PurchaseDeliveryOrder;
import com.lxzl.erp.common.domain.purchase.pojo.PurchaseOrder;
import com.lxzl.erp.common.domain.purchase.pojo.PurchaseReceiveOrder;
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
        ServiceResult<String, String> serviceResult = purchaseOrderService.add(purchaseOrder);
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
        ServiceResult<String, String> serviceResult = purchaseOrderService.update(purchaseOrder);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 删除采购单
     * @param purchaseOrder
     * @param validResult
     * @return
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Result deletePurchaseOrder(@RequestBody @Validated(IdGroup.class) PurchaseOrder purchaseOrder, BindingResult validResult) {
        String errorCode = purchaseOrderService.delete(purchaseOrder);
        return resultGenerator.generate(errorCode);
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

    /**
     * 采购单分页
     * @param purchaseOrderQueryParam
     * @param validResult
     * @return
     */
    @RequestMapping(value = "page", method = RequestMethod.POST)
    public Result pagePurchaseOrder(@RequestBody PurchaseOrderQueryParam purchaseOrderQueryParam, BindingResult validResult) {
        ServiceResult<String, Page<PurchaseOrder>> serviceResult = purchaseOrderService.page(purchaseOrderQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 采购单详情
     * @param purchaseOrder
     * @param validResult
     * @return
     */
    @RequestMapping(value = "queryPurchaseOrderByNo", method = RequestMethod.POST)
    public Result queryPurchaseOrderByNo(@RequestBody @Validated(IdGroup.class)PurchaseOrder purchaseOrder, BindingResult validResult) {
        ServiceResult<String, PurchaseOrder> serviceResult = purchaseOrderService.queryPurchaseOrderByNo(purchaseOrder.getPurchaseNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 发货通知单分页
     * @param purchaseDeliveryOrderQueryParam
     * @param validResult
     * @return
     */
    @RequestMapping(value = "pagePurchaseDelivery", method = RequestMethod.POST)
    public Result pagePurchaseDelivery(@RequestBody PurchaseDeliveryOrderQueryParam purchaseDeliveryOrderQueryParam, BindingResult validResult) {
        ServiceResult<String, Page<PurchaseDeliveryOrder>> serviceResult = purchaseOrderService.pagePurchaseDelivery(purchaseDeliveryOrderQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 发货通知单详情
     * @param purchaseDeliveryOrder
     * @param validResult
     * @return
     */
    @RequestMapping(value = "queryPurchaseDeliveryOrderByNo", method = RequestMethod.POST)
    public Result queryPurchaseDeliveryOrderByNo(@RequestBody @Validated(IdGroup.class)PurchaseDeliveryOrder purchaseDeliveryOrder, BindingResult validResult) {
        ServiceResult<String, PurchaseDeliveryOrder> serviceResult = purchaseOrderService.queryPurchaseDeliveryOrderByNo(purchaseDeliveryOrder);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 修改收货单
     * @param purchaseReceiveOrder
     * @param validResult
     * @return
     */
    @RequestMapping(value = "updatePurchaseReceiveOrder", method = RequestMethod.POST)
    public Result updatePurchaseReceiveOrder(@RequestBody @Validated(UpdateGroup.class) PurchaseReceiveOrder purchaseReceiveOrder, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = purchaseOrderService.updatePurchaseReceiveOrder(purchaseReceiveOrder);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

}
