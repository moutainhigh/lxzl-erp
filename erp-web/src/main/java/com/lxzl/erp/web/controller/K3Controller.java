package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.k3.K3OrderQueryParam;
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
    public Result addReturnOrder(@RequestBody K3ReturnOrderDetail k3ReturnOrderDetail, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = k3Service.addReturnOrder(k3ReturnOrderDetail);
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

    @RequestMapping(value = "sendToK3", method = RequestMethod.POST)
    public Result sendToK3(@RequestBody K3ReturnOrder param, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = k3Service.sendToK3(param.getReturnOrderNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @Autowired
    private ResultGenerator resultGenerator;

    @Autowired
    private K3Service k3Service;
}
