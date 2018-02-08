package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.changeOrder.*;
import com.lxzl.erp.common.domain.changeOrder.pojo.ChangeOrder;
import com.lxzl.erp.common.domain.changeOrder.pojo.ChangeOrderMaterial;
import com.lxzl.erp.common.domain.changeOrder.pojo.ChangeOrderProductEquipment;
import com.lxzl.erp.common.domain.purchase.UpdateReceiveEquipmentRemarkParam;
import com.lxzl.erp.common.domain.purchase.UpdateReceiveMaterialRemarkParam;
import com.lxzl.erp.common.domain.validGroup.ExtendGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.purchaseOrder.UpdateReceiveRemarkGroup;
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
     *
     * @param addChangeOrderParam
     * @param validResult
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result add(@RequestBody @Validated AddChangeOrderParam addChangeOrderParam, BindingResult validResult) {

        ServiceResult<String, String> serviceResult = changeOrderService.add(addChangeOrderParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 修改换货单
     *
     * @param updateChangeOrderParam
     * @param validResult
     * @return
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result update(@RequestBody @Validated UpdateChangeOrderParam updateChangeOrderParam, BindingResult validResult) {

        ServiceResult<String, String> serviceResult = changeOrderService.update(updateChangeOrderParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 换货单提交
     *
     * @param changeOrderCommitParam
     * @param validResult
     * @return
     */
    @RequestMapping(value = "commit", method = RequestMethod.POST)
    public Result commit(@RequestBody @Validated(ExtendGroup.class) ChangeOrderCommitParam changeOrderCommitParam, BindingResult validResult) {
        return resultGenerator.generate(changeOrderService.commit(changeOrderCommitParam));
    }

    /**
     * 换货备货接口
     *
     * @param stockUpForChangeParam
     * @param validResult
     * @return
     */
    @RequestMapping(value = " stockUpForChange", method = RequestMethod.POST)
    public Result stockUpForChange(@RequestBody @Validated StockUpForChangeParam stockUpForChangeParam, BindingResult validResult) {
        return resultGenerator.generate(changeOrderService.stockUpForChange(stockUpForChangeParam));
    }

    /**
     * 换货发货接口
     *
     * @param changeOrder
     * @param validResult
     * @return
     */
    @RequestMapping(value = "delivery", method = RequestMethod.POST)
    public Result delivery(@RequestBody @Validated(IdGroup.class) ChangeOrder changeOrder, BindingResult validResult) {
        return resultGenerator.generate(changeOrderService.delivery(changeOrder));
    }
    /**
     * 换设备接口
     *
     * @param changeOrderProductEquipment
     * @param validResult
     * @return
     */
    @RequestMapping(value = "doChangeEquipment", method = RequestMethod.POST)
    public Result doChangeEquipment(@RequestBody @Validated(ExtendGroup.class) ChangeOrderProductEquipment changeOrderProductEquipment, BindingResult validResult) {
        return resultGenerator.generate(changeOrderService.doChangeEquipment(changeOrderProductEquipment));
    }
    /**
     * 换散料接口
     *
     * @param changeOrderMaterial
     * @param validResult
     * @return
     */
    @RequestMapping(value = "doChangeMaterial", method = RequestMethod.POST)
    public Result doChangeMaterial(@RequestBody @Validated(ExtendGroup.class) ChangeOrderMaterial changeOrderMaterial, BindingResult validResult) {
        return resultGenerator.generate(changeOrderService.doChangeMaterial(changeOrderMaterial));
    }
    /**
     * 换货完成接口
     *
     * @param changeOrder
     * @param validResult
     * @return
     */
    @RequestMapping(value = "end", method = RequestMethod.POST)
    public Result end(@RequestBody @Validated({ExtendGroup.class}) ChangeOrder changeOrder, BindingResult validResult) {
        return resultGenerator.generate(changeOrderService.end(changeOrder));
    }

    /**
     * 换货取消接口
     *
     * @param changeOrder
     * @param validResult
     * @return
     */
    @RequestMapping(value = "cancel", method = RequestMethod.POST)
    public Result cancel(@RequestBody @Validated(IdGroup.class) ChangeOrder changeOrder, BindingResult validResult) {
        return resultGenerator.generate(changeOrderService.cancel(changeOrder));
    }

    /**
     * 换货单详情
     *
     * @param changeOrder
     * @param validResult
     * @return
     */
    @RequestMapping(value = "detail", method = RequestMethod.POST)
    public Result detail(@RequestBody @Validated(IdGroup.class) ChangeOrder changeOrder, BindingResult validResult) {
        return resultGenerator.generate(changeOrderService.detail(changeOrder));
    }

    /**
     * 换货单分页
     *
     * @param changeOrderPageParam
     * @param validResult
     * @return
     */
    @RequestMapping(value = "page", method = RequestMethod.POST)
    public Result page(@RequestBody @Validated ChangeOrderPageParam changeOrderPageParam, BindingResult validResult) {
        return resultGenerator.generate(changeOrderService.page(changeOrderPageParam));
    }

    /**
     * 换货设备项列表
     *
     * @param changeEquipmentPageParam
     * @param validResult
     * @return
     */
    @RequestMapping(value = "pageChangeOrderProductEquipment", method = RequestMethod.POST)
    public Result pageChangeOrderProductEquipment(@RequestBody @Validated ChangeEquipmentPageParam changeEquipmentPageParam, BindingResult validResult) {
        return resultGenerator.generate(changeOrderService.pageChangeOrderProductEquipment(changeEquipmentPageParam));
    }

    /**
     * 换货散料项列表
     *
     * @param changeBulkPageParam
     * @param validResult
     * @return
     */
    @RequestMapping(value = "pageChangeOrderMaterialBulk", method = RequestMethod.POST)
    public Result pageChangeOrderMaterialBulk(@RequestBody @Validated ChangeBulkPageParam changeBulkPageParam, BindingResult validResult) {
        return resultGenerator.generate(changeOrderService.pageChangeOrderMaterialBulk(changeBulkPageParam));
    }

    /**
     * 未换货扫码接口（处理中和待取货状态的换货单可操作）
     *
     * @param processNoChangeEquipmentParam
     * @param validResult
     * @return
     */
    @RequestMapping(value = "processNoChangeEquipment", method = RequestMethod.POST)
    public Result processNoChangeEquipment(@RequestBody @Validated ProcessNoChangeEquipmentParam processNoChangeEquipmentParam, BindingResult validResult) {
        return resultGenerator.generate(changeOrderService.processNoChangeEquipment(processNoChangeEquipmentParam));
    }
















    /**
     * 换货单换回设备备注
     *
     * @param updateChangeEquipmentRemarkParam
     * @param validResult
     * @return
     */
    @RequestMapping(value = "updateChangeEquipmentRemark", method = RequestMethod.POST)
    public Result updateChangeEquipmentRemark(@RequestBody @Validated UpdateChangeEquipmentRemarkParam updateChangeEquipmentRemarkParam, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = changeOrderService.updateChangeEquipmentRemark(updateChangeEquipmentRemarkParam);
        return resultGenerator.generate(serviceResult);
    }
    /**
     * 换货单换回配件项备注
     *
     * @param updateChangeMaterialRemarkParam
     * @param validResult
     * @return
     */
    @RequestMapping(value = "updateChangeMaterialRemark", method = RequestMethod.POST)
    public Result updateChangeMaterialRemark(@RequestBody @Validated UpdateChangeMaterialRemarkParam updateChangeMaterialRemarkParam, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = changeOrderService.updateChangeMaterialRemark(updateChangeMaterialRemarkParam);
        return resultGenerator.generate(serviceResult);
    }
//    /**
//     * 换货单验货完成提交
//     *
//     * @param purchaseReceiveOrder
//     * @param validResult
//     * @return
//     */
//    @RequestMapping(value = "confirmChangeReceiveOrder", method = RequestMethod.POST)
//    public Result commitPurchaseReceiveOrder(@RequestBody @Validated(IdGroup.class) PurchaseReceiveOrder purchaseReceiveOrder, BindingResult validResult) {
//        ServiceResult<String, String> serviceResult = purchaseOrderService.commitPurchaseReceiveOrder(purchaseReceiveOrder);
//        return resultGenerator.generate(serviceResult);
//    }
//
//    /**
//     * 采购收货单商品项设备分页
//     *
//     * @param purchaseReceiveOrderProductEquipmentPageParam
//     * @param validResult
//     * @return
//     */
//    @RequestMapping(value = "pageReceiveOrderProductEquipment", method = RequestMethod.POST)
//    public Result pageReceiveOrderProductEquipment(@RequestBody @Validated PurchaseReceiveOrderProductEquipmentPageParam purchaseReceiveOrderProductEquipmentPageParam, BindingResult validResult) {
//        ServiceResult<String, Page<ProductEquipment>> serviceResult = purchaseOrderService.pageReceiveOrderProductEquipment(purchaseReceiveOrderProductEquipmentPageParam);
//        return resultGenerator.generate(serviceResult);
//    }
//
//    /**
//     * 修改采购收货单设备采购价格
//     *
//     * @param updateChangeEquipmentPriceParam
//     * @param validResult
//     * @return
//     */
//    @RequestMapping(value = "updatePurchaseReceiveEquipmentPrice", method = RequestMethod.POST)
//    public Result updateReceiveEquipmentPrice(@RequestBody @Validated(ExtendGroup.class) UpdateChangeEquipmentPriceParam updateChangeEquipmentPriceParam, BindingResult validResult) {
//        ServiceResult<String, String> serviceResult = purchaseOrderService.updateReceiveEquipmentPrice(updateChangeEquipmentPriceParam);
//        return resultGenerator.generate(serviceResult);
//    }
//
//    /**
//     * 获取采购收货单物料价格
//     *
//     * @param purchaseReceiveOrderMaterial
//     * @param validResult
//     * @return
//     */
//    @RequestMapping(value = "getPurchaseReceiveMaterialPriceList", method = RequestMethod.POST)
//    public Result getPurchaseReceiveMaterialPriceList(@RequestBody @Validated(IdGroup.class) PurchaseReceiveOrderMaterial purchaseReceiveOrderMaterial, BindingResult validResult) {
//        ServiceResult<String, List<PurchaseReceiveOrderMaterialPrice>> serviceResult = purchaseOrderService.getPurchaseReceiveMaterialPriceList(purchaseReceiveOrderMaterial);
//        return resultGenerator.generate(serviceResult);
//    }
//
//    /**
//     * 修改采购收货单物料价格
//     *
//     * @param updateChangeMaterialPriceParam
//     * @param validResult
//     * @return
//     */
//    @RequestMapping(value = "updatePurchaseReceiveMaterialPrice", method = RequestMethod.POST)
//    public Result updatePurchaseReceiveMaterialPrice(@RequestBody @Validated(UpdateGroup.class) UpdateChangeMaterialPriceParam updateChangeMaterialPriceParam, BindingResult validResult) {
//        ServiceResult<String, Integer> serviceResult = purchaseOrderService.updatePurchaseReceiveMaterialPrice(updateChangeMaterialPriceParam);
//        return resultGenerator.generate(serviceResult);
//    }
}
