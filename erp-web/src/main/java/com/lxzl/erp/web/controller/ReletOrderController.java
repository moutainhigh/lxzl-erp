package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.reletorder.ReletOrderQueryParam;
import com.lxzl.erp.common.domain.reletorder.pojo.ReletOrder;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.reletorder.ReletOrderService;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 续租单控制器
 *
 * @author ZhaoZiXuan
 * @date 2018/4/24 9:37
 */
@Controller
@ControllerLog
@RequestMapping("/reletOrder")
public class ReletOrderController extends BaseController {

    @Autowired
    private ResultGenerator resultGenerator;

    @Autowired
    private ReletOrderService reletOrderService;

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public Result create(@RequestBody Order order, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = reletOrderService.createReletOrder(order);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "page", method = RequestMethod.POST)
    public Result queryAllReletOrder(@RequestBody ReletOrderQueryParam param, BindingResult validResult) {
        ServiceResult<String, Page<ReletOrder>> serviceResult = reletOrderService.queryAllReletOrder(param);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryReletOrderDetailById", method = RequestMethod.POST)
    public Result queryReletOrderDetailById(@RequestBody ReletOrderQueryParam param, BindingResult validResult){
        ServiceResult<String, ReletOrder> serviceResult = reletOrderService.queryReletOrderDetailById(param.getReletOrderId());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
}