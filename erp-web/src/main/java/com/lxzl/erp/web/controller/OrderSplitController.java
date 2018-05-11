package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.order.OrderSplitQueryParam;
import com.lxzl.erp.common.domain.order.pojo.OrderSplit;
import com.lxzl.erp.common.domain.validGroup.order.OrderSplitQueryByTypeAndIdGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.order.OrderSplitDetailService;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 描述: 订单拆单控制器
 *
 * @author gaochao
 * @date 2017-11-15 14:16
 */
@Controller
@ControllerLog
@RequestMapping("/orderSplit")
public class OrderSplitController extends BaseController {

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result add(@RequestBody @Validated OrderSplit orderSplit, BindingResult validResult) {
        ServiceResult<String, List<Integer>> serviceResult = orderSplitDetailService.addOrderSplitDetail(orderSplit);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryByTypeAndId", method = RequestMethod.POST)
    public Result findByItemTypeAndItemId(@RequestBody @Validated({OrderSplitQueryByTypeAndIdGroup.class}) OrderSplitQueryParam orderSplitQueryParam, BindingResult validResult) {
        ServiceResult<String, List<OrderSplit>> serviceResult = orderSplitDetailService.queryOrderSplitDetailByOrderItemTypeAndOrderItemReferId(orderSplitQueryParam.getOrderItemType(), orderSplitQueryParam.getOrderItemReferId());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result update(@RequestBody OrderSplit orderSplit, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = orderSplitDetailService.updateOrderSplit(orderSplit);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Result delete(@RequestBody OrderSplitQueryParam orderSplitQueryParam, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = orderSplitDetailService.deleteOrderSplit(orderSplitQueryParam.getOrderItemType(), orderSplitQueryParam.getOrderItemReferId());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @Autowired
    private ResultGenerator resultGenerator;

    @Autowired
    private OrderSplitDetailService orderSplitDetailService;
}
