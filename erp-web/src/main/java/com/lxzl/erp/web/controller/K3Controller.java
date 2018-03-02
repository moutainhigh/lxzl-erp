package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.k3.K3ChangeOrderCommitParam;
import com.lxzl.erp.common.domain.k3.K3OrderQueryParam;
import com.lxzl.erp.common.domain.k3.K3ReturnOrderCommitParam;
import com.lxzl.erp.common.domain.k3.pojo.K3ChangeOrder;
import com.lxzl.erp.common.domain.k3.pojo.K3ChangeOrderDetail;
import com.lxzl.erp.common.domain.k3.pojo.changeOrder.K3ChangeOrderQueryParam;
import com.lxzl.erp.common.domain.k3.pojo.order.Order;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrder;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderDetail;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderQueryParam;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.k3.K3Service;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@ControllerLog
@RequestMapping("k3")
public class K3Controller extends BaseController {

    @RequestMapping(value = "queryOrder", method = RequestMethod.POST)
    public Result queryOrder(@RequestBody K3OrderQueryParam param, BindingResult validResult) {
        ServiceResult<String, Page<Order>> serviceResult = k3Service.queryAllOrder(param);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryOrderByNo", method = RequestMethod.POST)
    public Result queryOrderByNo(@RequestBody K3OrderQueryParam param, BindingResult validResult) {
        ServiceResult<String, Order> serviceResult = k3Service.queryOrder(param.getOrderNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "createReturnOrder", method = RequestMethod.POST)
    public Result createReturnOrder(@RequestBody K3ReturnOrder k3ReturnOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = k3Service.createReturnOrder(k3ReturnOrder);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "updateReturnOrder", method = RequestMethod.POST)
    public Result updateReturnOrder(@RequestBody K3ReturnOrder k3ReturnOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = k3Service.updateReturnOrder(k3ReturnOrder);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "addReturnOrder", method = RequestMethod.POST)
    public Result addReturnOrder(@RequestBody K3ReturnOrder k3ReturnOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = k3Service.addReturnOrder(k3ReturnOrder);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "deleteReturnOrder", method = RequestMethod.POST)
    public Result deleteReturnOrder(@RequestBody K3ReturnOrderDetail param, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = k3Service.deleteReturnOrder(param.getK3ReturnOrderDetailId());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryReturnOrder", method = RequestMethod.POST)
    public Result queryReturnOrder(@RequestBody K3ReturnOrderQueryParam param, BindingResult validResult) {
        ServiceResult<String, Page<K3ReturnOrder>> serviceResult = k3Service.queryReturnOrder(param);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryReturnOrderByNo", method = RequestMethod.POST)
    public Result queryReturnOrderByNo(@RequestBody K3ReturnOrderQueryParam param, BindingResult validResult) {
        ServiceResult<String, K3ReturnOrder> serviceResult = k3Service.queryReturnOrderByNo(param.getReturnOrderNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "cancelK3ReturnOrder", method = RequestMethod.POST)
    public Result cancelK3ReturnOrder(@RequestBody K3ReturnOrder k3ReturnOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = k3Service.cancelK3ReturnOrder(k3ReturnOrder);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "commitK3ReturnOrder", method = RequestMethod.POST)
    public Result commitK3ReturnOrder(@RequestBody K3ReturnOrderCommitParam k3ReturnOrderCommitParam, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = k3Service.commitK3ReturnOrder(k3ReturnOrderCommitParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "sendToK3", method = RequestMethod.POST)
    public Result sendToK3(@RequestBody K3ReturnOrder param, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = k3Service.sendToK3(param.getReturnOrderNo());
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
        ServiceResult<String, String> serviceResult = k3Service.createChangeOrder(k3ChangeOrder);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "updateChangeOrder", method = RequestMethod.POST)
    public Result updateChangeOrder(@RequestBody K3ChangeOrder k3ChangeOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = k3Service.updateChangeOrder(k3ChangeOrder);
        return resultGenerator.generate(serviceResult);
    }

    @RequestMapping(value = "addChangeOrder", method = RequestMethod.POST)
    public Result addChangeOrder(@RequestBody K3ChangeOrder k3ChangeOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = k3Service.addChangeOrder(k3ChangeOrder);
        return resultGenerator.generate(serviceResult);
    }

    @RequestMapping(value = "deleteChangeOrder", method = RequestMethod.POST)
    public Result deleteChangeOrder(@RequestBody K3ChangeOrderDetail param, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = k3Service.deleteChangeOrder(param.getK3ChangeOrderDetailId());
        return resultGenerator.generate(serviceResult);
    }

    @RequestMapping(value = "queryChangeOrder", method = RequestMethod.POST)
    public Result queryChangeOrder(@RequestBody K3ChangeOrderQueryParam param, BindingResult validResult) {
        ServiceResult<String, Page<K3ChangeOrder>> serviceResult = k3Service.queryChangeOrder(param);
        return resultGenerator.generate(serviceResult);
    }

    @RequestMapping(value = "queryChangeOrderByNo", method = RequestMethod.POST)
    public Result queryChangeOrderByNo(@RequestBody K3ChangeOrderQueryParam param, BindingResult validResult) {
        ServiceResult<String, K3ChangeOrder> serviceResult = k3Service.queryChangeOrderByNo(param.getChangeOrderNo());
        return resultGenerator.generate(serviceResult);
    }
    @RequestMapping(value = "sendChangeOrderToK3", method = RequestMethod.POST)
    public Result sendChangeOrderToK3(@RequestBody K3ChangeOrder k3ChangeOrder) {
        ServiceResult<String, String> serviceResult = k3Service.sendChangeOrderToK3(k3ChangeOrder.getChangeOrderNo());
        return resultGenerator.generate(serviceResult);
    }

    @RequestMapping(value = "cancelK3ChangeOrder", method = RequestMethod.POST)
    public Result cancelK3ChangeOrder(@RequestBody K3ChangeOrder k3ChangeOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = k3Service.cancelK3ChangeOrder(k3ChangeOrder);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "commitK3ChangeOrder", method = RequestMethod.POST)
    public Result commitK3ChangeOrder(@RequestBody K3ChangeOrderCommitParam k3ChangeOrderCommitParam, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = k3Service.commitK3ChangeOrder(k3ChangeOrderCommitParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @Autowired
    private ResultGenerator resultGenerator;

    @Autowired
    private K3Service k3Service;
}
