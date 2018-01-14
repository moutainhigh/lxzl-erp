package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.transferOrder.*;
import com.lxzl.erp.common.domain.transferOrder.pojo.TransferOrder;
import com.lxzl.erp.common.domain.transferOrder.pojo.TransferOrderMaterialBulk;
import com.lxzl.erp.common.domain.transferOrder.pojo.TransferOrderProductEquipment;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.QueryGroup;
import com.lxzl.erp.common.domain.validGroup.TransferOrder.*;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.transferOrder.TransferOrderService;
import com.lxzl.se.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 15:34 2018/1/3
 * @Modified By:
 */

@RequestMapping("transferOrder")
@Controller
@ControllerLog
public class TransferOrderController {

    @Autowired
    private TransferOrderService transferOrderService;

    @Autowired
    private ResultGenerator resultGenerator;

    /**
     * 创建转入的转移单
     */
    @RequestMapping(value = "createTransferOrderInto", method = RequestMethod.POST)
    public Result createTransferOrderInto(@RequestBody @Validated(AddGroup.class) TransferOrder transferOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = transferOrderService.createTransferOrderInto(transferOrder);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 创建转出的转移单
     */
    @RequestMapping(value = "createTransferOrderOut", method = RequestMethod.POST)
    public Result createTransferOrderOut(@RequestBody @Validated(TransferOrderOutGroup.class) TransferOrder transferOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = transferOrderService.createTransferOrderOut(transferOrder);
        return resultGenerator.generate(serviceResult);
    }

    /**
     * 修改转入转移单
     */
    @RequestMapping(value = "updateTransferOrderInto", method = RequestMethod.POST)
    public Result updateTransferOrderInto(@RequestBody  @Validated(UpdateTransferOrderIntoGroup.class) TransferOrder transferOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = transferOrderService.updateTransferOrderInto(transferOrder);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 修改转出转移单
     */
    @RequestMapping(value = "updateTransferOrderOut", method = RequestMethod.POST)
    public Result updateTransferOrderOut(@RequestBody  @Validated(UpdateTransferOrderOutGroup.class) TransferOrder transferOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = transferOrderService.updateTransferOrderOut(transferOrder);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 商品设备转出备货
     */
    @RequestMapping(value = "transferOrderProductEquipmentOut", method = RequestMethod.POST)
    public Result transferOrderProductEquipmentOut(@RequestBody  @Validated(TransferOrderProductEquipmentOutGroup.class)TransferOrderProductEquipmentOutParam transferOrderProductEquipmentOutParam, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = transferOrderService.transferOrderProductEquipmentOut(transferOrderProductEquipmentOutParam);
        return resultGenerator.generate(serviceResult);
    }

    /**
     * 商品设备转出清货
     */
    @RequestMapping(value = "dumpTransferOrderProductEquipmentOut", method = RequestMethod.POST)
    public Result dumpTransferOrderProductEquipmentOut(@RequestBody  @Validated(DumpTransferOrderProductEquipmentOutGroup.class)TransferOrderProductEquipmentOutParam transferOrderProductEquipmentOutParam, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = transferOrderService.dumpTransferOrderProductEquipmentOut(transferOrderProductEquipmentOutParam);
        return resultGenerator.generate(serviceResult);
    }

    /**
     * 物料转出备货
     */
    @RequestMapping(value = "transferOrderMaterialOut", method = RequestMethod.POST)
    public Result transferOrderMaterialOut(@RequestBody @Validated(TramsferOrderMaterialOutGroup.class) TransferOrderMaterialOutParam transferOrderMaterialOutParam, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = transferOrderService.transferOrderMaterialOut(transferOrderMaterialOutParam);
        return resultGenerator.generate(serviceResult);
    }

    /**
     * 物料转出清货
     */
    @RequestMapping(value = "dumpTransferOrderMaterialOut", method = RequestMethod.POST)
    public Result dumpTransferOrderMaterialOut(@RequestBody @Validated(DumpTransferOrderMaterialOutGroup.class)TransferOrderMaterialOutParam transferOrderMaterialOutParam, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = transferOrderService.dumpTransferOrderMaterialOut(transferOrderMaterialOutParam);
        return resultGenerator.generate(serviceResult);
    }

    /**
     * 取消转移单
     */
    @RequestMapping(value = "cancelTransferOrder", method = RequestMethod.POST)
    public Result cancelTransferOrder(@RequestBody @Validated(IdGroup.class) TransferOrder transferOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = transferOrderService.cancelTransferOrder(transferOrder);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 提交审核转移单
     */
    @RequestMapping(value = "commitTransferOrder", method = RequestMethod.POST)
    public Result commitTransferOrder(@RequestBody  @Validated(IdGroup.class) TransferOrderCommitParam transferOrderCommitParam, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = transferOrderService.commitTransferOrder(transferOrderCommitParam.getTransferOrderNo(), transferOrderCommitParam.getVerifyUserId(), transferOrderCommitParam.getRemark());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 结束转移
     */
    @RequestMapping(value = "endTransferOrder", method = RequestMethod.POST)
    public Result endTransferOrder(@RequestBody @Validated(IdGroup.class)TransferOrder transferOrder, BindingResult validResult) {
        ServiceResult<String,String> serviceResult = transferOrderService.endTransferOrder(transferOrder);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 分页展示
     */
    @RequestMapping(value = "pageTransferOrder", method = RequestMethod.POST)
    public Result pageTransferOrder(@RequestBody TransferOrderQueryParam transferOrderQueryParam, BindingResult validResult) {
        ServiceResult<String, Page<TransferOrder>> serviceResult = transferOrderService.pageTransferOrder(transferOrderQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 转移单详情
     */
    @RequestMapping(value = "detailTransferOrderByNo", method = RequestMethod.POST)
    public Result detailTransferOrderByNo(@RequestBody @Validated(IdGroup.class) TransferOrder transferOrder, BindingResult validResult) {
        ServiceResult<String, TransferOrder> serviceResult = transferOrderService.detailTransferOrderByNo(transferOrder.getTransferOrderNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 转移单商品设备详情
     */
    @RequestMapping(value = "detailTransferOrderProductEquipmentById", method = RequestMethod.POST)
    public Result detailTransferOrderProductEquipmentById(@RequestBody @Validated(QueryGroup.class) TransferOrderProductEquipmentQueryParam transferOrderProductEquipmentQueryParam, BindingResult validResult) {
        ServiceResult<String,  Page<TransferOrderProductEquipment>> serviceResult = transferOrderService.detailTransferOrderProductEquipmentById(transferOrderProductEquipmentQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 转移单配件散料详情
     */
    @RequestMapping(value = "detailTransferOrderMaterialBulkById", method = RequestMethod.POST)
    public Result detailTransferOrderMaterialBulkById(@RequestBody @Validated(QueryGroup.class)TransferOrderMaterialBulkQueryParam transferOrderMaterialBulkQueryParam, BindingResult validResult) {
        ServiceResult<String,Page<TransferOrderMaterialBulk>> serviceResult = transferOrderService.detailTransferOrderMaterialBulkById(transferOrderMaterialBulkQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
}
