package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.changeOrder.*;
import com.lxzl.erp.common.domain.changeOrder.pojo.ChangeOrder;
import com.lxzl.erp.common.domain.changeOrder.pojo.ChangeOrderMaterial;
import com.lxzl.erp.common.domain.changeOrder.pojo.ChangeOrderMaterialBulk;
import com.lxzl.erp.common.domain.changeOrder.pojo.ChangeOrderProductEquipment;
import com.lxzl.erp.common.domain.validGroup.ExtendGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.changeOrder.AddChangeOrderGroup;
import com.lxzl.erp.common.domain.validGroup.changeOrder.UpdateChangeOrderGroup;
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
    public Result add(@RequestBody @Validated(AddChangeOrderGroup.class) AddChangeOrderParam addChangeOrderParam, BindingResult validResult) {

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
    public Result update(@RequestBody @Validated(UpdateChangeOrderGroup.class) UpdateChangeOrderParam updateChangeOrderParam, BindingResult validResult) {

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
     * @param stockUpByChangeParam
     * @param validResult
     * @return
     */
    @RequestMapping(value = " stockUpByChange", method = RequestMethod.POST)
    public Result stockUpByChange(@RequestBody @Validated StockUpByChangeParam stockUpByChangeParam, BindingResult validResult) {
        return resultGenerator.generate(changeOrderService.stockUpByChange(stockUpByChangeParam));
    }

    /**
     * 换货发货接口
     *
     * @param changeOrder
     * @param validResult
     * @return
     */
    @RequestMapping(value = "delivery", method = RequestMethod.POST)
    public Result delivery(@RequestBody @Validated ChangeOrder changeOrder, BindingResult validResult) {
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
    public Result doChangeEquipment(@RequestBody @Validated ChangeOrderProductEquipment changeOrderProductEquipment, BindingResult validResult) {
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
    public Result doChangeMaterial(@RequestBody @Validated ChangeOrderMaterial changeOrderMaterial, BindingResult validResult) {
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
    public Result end(@RequestBody @Validated ChangeOrder changeOrder, BindingResult validResult) {
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
    public Result cancel(@RequestBody @Validated ChangeOrder changeOrder, BindingResult validResult) {
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
     * 换货单列表
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
     * @param changeOrder
     * @param validResult
     * @return
     */
    @RequestMapping(value = "pageChangeOrderProductEquipment", method = RequestMethod.POST)
    public Result pageChangeOrderProductEquipment(@RequestBody @Validated ChangeOrder changeOrder, BindingResult validResult) {
        return resultGenerator.generate(changeOrderService.pageChangeOrderProductEquipment(changeOrder));
    }

    /**
     * 换货散料项列表
     *
     * @param changeOrder
     * @param validResult
     * @return
     */
    @RequestMapping(value = "pageChangeOrderMaterialBulk", method = RequestMethod.POST)
    public Result pageChangeOrderMaterialBulk(@RequestBody @Validated ChangeOrder changeOrder, BindingResult validResult) {
        return resultGenerator.generate(changeOrderService.pageChangeOrderMaterialBulk(changeOrder));
    }

    /**
     * 删除换货设备项接口（处理中和待取货状态的换货单可修改）
     *
     * @param changeOrderProductEquipment
     * @param validResult
     * @return
     */
    @RequestMapping(value = "deleteChangeOrderProductEquipment", method = RequestMethod.POST)
    public Result deleteChangeOrderProductEquipment(@RequestBody @Validated ChangeOrderProductEquipment changeOrderProductEquipment, BindingResult validResult) {
        return resultGenerator.generate(changeOrderService.deleteChangeOrderProductEquipment(changeOrderProductEquipment));
    }

    /**
     * 删除换货散料项接口（处理中和待取货状态的换货单可修改）
     *
     * @param changeOrderMaterialBulk
     * @param validResult
     * @return
     */
    @RequestMapping(value = "deleteChangeOrderMaterialBulk", method = RequestMethod.POST)
    public Result deleteChangeOrderMaterialBulk(@RequestBody @Validated ChangeOrderMaterialBulk changeOrderMaterialBulk, BindingResult validResult) {
        return resultGenerator.generate(changeOrderService.deleteChangeOrderMaterialBulk(changeOrderMaterialBulk));
    }
}
