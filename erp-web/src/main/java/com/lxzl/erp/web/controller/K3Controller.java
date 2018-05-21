package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.k3.*;
import com.lxzl.erp.common.domain.k3.pojo.K3ChangeOrder;
import com.lxzl.erp.common.domain.k3.pojo.K3ChangeOrderDetail;
import com.lxzl.erp.common.domain.k3.pojo.K3SendRecord;
import com.lxzl.erp.common.domain.k3.pojo.changeOrder.K3ChangeOrderQueryParam;
import com.lxzl.erp.common.domain.k3.pojo.order.Order;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrder;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderDetail;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderQueryParam;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.k3.K3ChangeOrderService;
import com.lxzl.erp.core.service.k3.K3ReturnOrderService;
import com.lxzl.erp.core.service.k3.K3Service;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
@ControllerLog
@RequestMapping("k3")
public class K3Controller extends BaseController {

    @RequestMapping(value = "queryOrder", method = RequestMethod.POST)
    public Result queryOrder(@RequestBody K3OrderQueryParam param, BindingResult validResult) {
        ServiceResult<String, Page<Order>> serviceResult = k3Service.queryAllOrder(param);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 创建退货单时查询自己的订单列表展示
     * @param param
     * @param validResult
     * @return
     */
    @RequestMapping(value = "queryOrderForReturn", method = RequestMethod.POST)
    public Result queryOrderForReturn(@RequestBody OrderForReturnQueryParam param, BindingResult validResult) {
        ServiceResult<String, Page<Order>> serviceResult = k3ReturnOrderService.queryOrderForReturn(param);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryOrderByNo", method = RequestMethod.POST)
    public Result queryOrderByNo(@RequestBody K3OrderQueryParam param, BindingResult validResult) {
        ServiceResult<String, Order> serviceResult = k3Service.queryOrder(param.getOrderNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 创建退货单时从ERP获取订单数据
     * @param k3ReturnOrder
     * @param validResult
     * @return
     */
    @RequestMapping(value = "createReturnOrderFromERP", method = RequestMethod.POST)
    public Result createReturnOrderFromERP(@RequestBody @Validated(AddGroup.class) K3ReturnOrder k3ReturnOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = k3ReturnOrderService.createReturnOrderFromERP(k3ReturnOrder);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
    /**
     * 修改退货单时从ERP获取订单数据
     * @param k3ReturnOrder
     * @param validResult
     * @return
     */
    @RequestMapping(value = "updateReturnOrderFromERP", method = RequestMethod.POST)
    public Result updateReturnOrderFromERP(@RequestBody @Validated(UpdateGroup.class) K3ReturnOrder k3ReturnOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = k3ReturnOrderService.updateReturnOrderFromERP(k3ReturnOrder);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
    @RequestMapping(value = "createReturnOrder", method = RequestMethod.POST)
    public Result createReturnOrder(@RequestBody @Validated(AddGroup.class) K3ReturnOrder k3ReturnOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = k3ReturnOrderService.createReturnOrder(k3ReturnOrder);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "updateReturnOrder", method = RequestMethod.POST)
    public Result updateReturnOrder(@RequestBody @Validated(UpdateGroup.class) K3ReturnOrder k3ReturnOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = k3ReturnOrderService.updateReturnOrder(k3ReturnOrder);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "addReturnOrder", method = RequestMethod.POST)
    public Result addReturnOrder(@RequestBody K3ReturnOrder k3ReturnOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = k3ReturnOrderService.addReturnOrder(k3ReturnOrder);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "deleteReturnOrder", method = RequestMethod.POST)
    public Result deleteReturnOrder(@RequestBody K3ReturnOrderDetail param, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = k3ReturnOrderService.deleteReturnOrder(param.getK3ReturnOrderDetailId());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "revokeReturnOrder", method = RequestMethod.POST)
    public Result revokeReturnOrder(@RequestBody @Validated(IdGroup.class) K3ReturnOrder k3ReturnOrder, BindingResult validResult) {
        return resultGenerator.generate(k3ReturnOrderService.revokeReturnOrder(k3ReturnOrder.getReturnOrderNo()));
    }
    @RequestMapping(value = "queryReturnOrder", method = RequestMethod.POST)
    public Result queryReturnOrder(@RequestBody K3ReturnOrderQueryParam param, BindingResult validResult) {
        ServiceResult<String, Page<K3ReturnOrder>> serviceResult = k3ReturnOrderService.queryReturnOrder(param);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryReturnOrderByNo", method = RequestMethod.POST)
    public Result queryReturnOrderByNo(@RequestBody K3ReturnOrderQueryParam param, BindingResult validResult) {
        ServiceResult<String, K3ReturnOrder> serviceResult = k3ReturnOrderService.queryReturnOrderByNo(param.getReturnOrderNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "cancelK3ReturnOrder", method = RequestMethod.POST)
    public Result cancelK3ReturnOrder(@RequestBody K3ReturnOrder k3ReturnOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = k3ReturnOrderService.cancelK3ReturnOrder(k3ReturnOrder);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "commitK3ReturnOrder", method = RequestMethod.POST)
    public Result commitK3ReturnOrder(@RequestBody K3ReturnOrderCommitParam k3ReturnOrderCommitParam, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = k3ReturnOrderService.commitK3ReturnOrder(k3ReturnOrderCommitParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "sendToK3", method = RequestMethod.POST)
    public Result sendToK3(@RequestBody K3ReturnOrder param, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = k3ReturnOrderService.sendReturnOrderToK3(param.getReturnOrderNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "strongCancelReturnOrder", method = RequestMethod.POST)
    public Result strongCancelReturnOrder(@RequestBody @Validated(IdGroup.class) K3ReturnOrder k3ReturnOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = k3ReturnOrderService.strongCancelReturnOrder(k3ReturnOrder.getReturnOrderNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /***
     * 创建K3换货单
     * @param k3ChangeOrder
     * @param validResult
     * @return
     */

    @RequestMapping(value = "createChangeOrder", method = RequestMethod.POST)
    public Result createChangeOrder(@RequestBody K3ChangeOrder k3ChangeOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = k3ChangeOrderService.createChangeOrder(k3ChangeOrder);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "updateChangeOrder", method = RequestMethod.POST)
    public Result updateChangeOrder(@RequestBody K3ChangeOrder k3ChangeOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = k3ChangeOrderService.updateChangeOrder(k3ChangeOrder);
        return resultGenerator.generate(serviceResult);
    }

    @RequestMapping(value = "addChangeOrder", method = RequestMethod.POST)
    public Result addChangeOrder(@RequestBody K3ChangeOrder k3ChangeOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = k3ChangeOrderService.addChangeOrder(k3ChangeOrder);
        return resultGenerator.generate(serviceResult);
    }

    @RequestMapping(value = "deleteChangeOrder", method = RequestMethod.POST)
    public Result deleteChangeOrder(@RequestBody K3ChangeOrderDetail param, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = k3ChangeOrderService.deleteChangeOrder(param.getK3ChangeOrderDetailId());
        return resultGenerator.generate(serviceResult);
    }

    @RequestMapping(value = "queryChangeOrder", method = RequestMethod.POST)
    public Result queryChangeOrder(@RequestBody K3ChangeOrderQueryParam param, BindingResult validResult) {
        ServiceResult<String, Page<K3ChangeOrder>> serviceResult = k3ChangeOrderService.queryChangeOrder(param);
        return resultGenerator.generate(serviceResult);
    }

    @RequestMapping(value = "queryChangeOrderByNo", method = RequestMethod.POST)
    public Result queryChangeOrderByNo(@RequestBody K3ChangeOrderQueryParam param, BindingResult validResult) {
        ServiceResult<String, K3ChangeOrder> serviceResult = k3ChangeOrderService.queryChangeOrderByNo(param.getChangeOrderNo());
        return resultGenerator.generate(serviceResult);
    }
    @RequestMapping(value = "sendChangeOrderToK3", method = RequestMethod.POST)
    public Result sendChangeOrderToK3(@RequestBody K3ChangeOrder k3ChangeOrder) {
        ServiceResult<String, String> serviceResult = k3ChangeOrderService.sendChangeOrderToK3(k3ChangeOrder.getChangeOrderNo());
        return resultGenerator.generate(serviceResult);
    }

    @RequestMapping(value = "cancelK3ChangeOrder", method = RequestMethod.POST)
    public Result cancelK3ChangeOrder(@RequestBody K3ChangeOrder k3ChangeOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = k3ChangeOrderService.cancelK3ChangeOrder(k3ChangeOrder);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "commitK3ChangeOrder", method = RequestMethod.POST)
    public Result commitK3ChangeOrder(@RequestBody K3ChangeOrderCommitParam k3ChangeOrderCommitParam, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = k3ChangeOrderService.commitK3ChangeOrder(k3ChangeOrderCommitParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * K3数据发送记录表
     *
     * @param k3SendRecordParam
     * @param validResult
     * @return
     */
    @RequestMapping(value = "queryK3SendRecord", method = RequestMethod.POST)
    public Result queryK3SendRecord(@RequestBody K3SendRecordParam k3SendRecordParam, BindingResult validResult) {
        ServiceResult<String, Page<K3SendRecord>> serviceResult = k3Service.queryK3SendRecord(k3SendRecordParam);
        return resultGenerator.generate(serviceResult);
    }

    @RequestMapping(value = "seedAgainK3SendRecord", method = RequestMethod.POST)
    public Result seedAgainK3SendRecord(@RequestBody @Validated(IdGroup.class)K3SendRecord k3SendRecord, BindingResult validResult) {
        ServiceResult<String,Integer> serviceResult = k3Service.sendAgainK3SendRecord(k3SendRecord);
        return resultGenerator.generate(serviceResult);
    }

    @RequestMapping(value = "batchSendDataToK3", method = RequestMethod.POST)
    public Result batchSendDataToK3(@RequestBody K3SendRecordBatchParam k3SendRecordBatchParam, BindingResult validResult) {
        ServiceResult<String, Map<String, String>> serviceResult = k3Service.batchSendDataToK3(k3SendRecordBatchParam);
        return resultGenerator.generate(serviceResult);
    }

    @RequestMapping(value = "transferOrder", method = RequestMethod.POST)
    public Result transferOrder(@RequestBody K3OrderQueryParam param, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = k3Service.transferOrder(param);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * <p>
     * 导入k3历史退货单
     * </p>
     * <pre>
     *     所需参数示例及其说明
     *     参数名称 : 示例值 : 说明 : 是否必须
     *     pageNo : 1 : 当前导入的页数 : 是
     *     pageSize : 10 : 每页显示数量 : 是
     * </pre>
     * @author daiqi
     * @date 2018/4/18 17:53
     * @param  param
     * @param validResult
     * @return com.lxzl.se.common.domain.Result
     */
    @RequestMapping(value = "importK3HistoricalRefundList", method = RequestMethod.POST)
    public Result importK3HistoricalRefundList(@RequestBody K3ReturnOrderQueryParam param, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = k3ReturnOrderService.importK3HistoricalRefundList(param);
        return resultGenerator.generate(serviceResult);
    }


    @RequestMapping(value = "batchImportK3HistoricalRefundList", method = RequestMethod.POST)
    public Result batchImportK3HistoricalRefundList(@RequestBody BatchImportK3HistoricalRefundListParam batchImportK3HistoricalRefundListParam) {
        return resultGenerator.generate( k3ReturnOrderService.batchImportK3HistoricalRefundList(batchImportK3HistoricalRefundListParam.getStartPage()));
    }
    @Autowired
    private ResultGenerator resultGenerator;

    @Autowired
    private K3Service k3Service;

    @Autowired
    private K3ReturnOrderService k3ReturnOrderService;

    @Autowired
    private K3ChangeOrderService k3ChangeOrderService;
}
