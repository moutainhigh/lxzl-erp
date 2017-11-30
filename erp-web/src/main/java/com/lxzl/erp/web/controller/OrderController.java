package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.order.OrderQueryParam;
import com.lxzl.erp.common.domain.order.ProcessOrderParam;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.order.OrderService;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.lxzl.se.common.domain.Result;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 描述: 订单控制器
 *
 * @author gaochao
 * @date 2017-11-15 14:16
 */
@Controller
@ControllerLog
@RequestMapping("/order")
public class OrderController extends BaseController {


    @RequestMapping(value = "create", method = RequestMethod.POST)
    public Result create(@RequestBody Order order, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = orderService.createOrder(order);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "commit", method = RequestMethod.POST)
    public Result commit(@RequestBody Order order, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = orderService.commitOrder(order.getOrderNo(), order.getVerifyUser());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryOrderByNo", method = RequestMethod.POST)
    public Result queryOrderByNo(@RequestBody Order order, BindingResult validResult) {
        ServiceResult<String, Order> serviceResult = orderService.queryOrderByNo(order.getOrderNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "cancel", method = RequestMethod.POST)
    public Result cancel(@RequestBody Order order, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = orderService.cancelOrder(order.getOrderNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryAllOrder", method = RequestMethod.POST)
    public Result queryAllOrder(@RequestBody OrderQueryParam param, BindingResult validResult) {
        ServiceResult<String, Page<Order>> serviceResult = orderService.queryAllOrder(param);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "confirm", method = RequestMethod.POST)
    public Result confirm(@RequestBody Order order, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = orderService.confirmOrder(order.getOrderNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "process", method = RequestMethod.POST)
    public Result process(@RequestBody ProcessOrderParam param, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = orderService.processOrder(param);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "delivery", method = RequestMethod.POST)
    public Result delivery(@RequestBody Order order, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = orderService.deliveryOrder(order);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @Autowired
    private ResultGenerator resultGenerator;

    @Autowired
    private OrderService orderService;
}
