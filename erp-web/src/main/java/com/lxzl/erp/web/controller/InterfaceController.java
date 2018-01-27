package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.customer.pojo.Customer;
import com.lxzl.erp.common.domain.order.OrderQueryParam;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.payment.ChargeRecordParam;
import com.lxzl.erp.common.domain.payment.WeixinPayParam;
import com.lxzl.erp.common.domain.payment.account.pojo.ChargeRecord;
import com.lxzl.erp.common.domain.statement.StatementOrderPayParam;
import com.lxzl.erp.common.domain.statement.StatementOrderQueryParam;
import com.lxzl.erp.common.domain.statement.pojo.StatementOrder;
import com.lxzl.erp.common.domain.validGroup.customer.QueryCustomerGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.customer.CustomerService;
import com.lxzl.erp.core.service.order.OrderService;
import com.lxzl.erp.core.service.payment.PaymentService;
import com.lxzl.erp.core.service.statement.StatementService;
import com.lxzl.erp.web.util.NetworkUtil;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2018-01-14 17:09
 */
@Controller
@ControllerLog
@RequestMapping("interface")
public class InterfaceController extends BaseController {

    @Autowired
    private ResultGenerator resultGenerator;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private StatementService statementService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "queryOrderByNo", method = RequestMethod.POST)
    public Result queryOrderByNo(@RequestBody OrderQueryParam order, BindingResult validResult) {
        ServiceResult<String, Order> serviceResult = orderService.queryOrderByNo(order.getOrderNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryAllOrder", method = RequestMethod.POST)
    public Result queryAllOrder(@RequestBody OrderQueryParam param, BindingResult validResult) {
        ServiceResult<String, Page<Order>> serviceResult = orderService.queryOrderByUserIdInterface(param);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryCustomerByName", method = RequestMethod.POST)
    public Result queryCustomerByName(@RequestBody @Validated(QueryCustomerGroup.class) Customer customer, BindingResult validResult) {
        ServiceResult<String, Customer> serviceResult = customerService.queryCustomerByCompanyName(customer.getCustomerName());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryStatementOrder", method = RequestMethod.POST)
    public Result queryStatementOrder(@RequestBody StatementOrderQueryParam statementOrderQueryParam, BindingResult validResult) {
        ServiceResult<String, Page<StatementOrder>> serviceResult = statementService.queryStatementOrder(statementOrderQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryStatementOrderDetail", method = RequestMethod.POST)
    public Result queryStatementOrderDetail(@RequestBody StatementOrderQueryParam statementOrderQueryParam, BindingResult validResult) {
        ServiceResult<String, StatementOrder> serviceResult = statementService.queryStatementOrderDetail(statementOrderQueryParam.getStatementOrderNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "weixinPayStatementOrder", method = RequestMethod.POST)
    public Result weixinPayStatementOrder(@RequestBody StatementOrderPayParam param, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = statementService.weixinPayStatementOrder(param.getStatementOrderNo(), param.getOpenId(), NetworkUtil.getIpAddress(request));
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "wechatCharge", method = RequestMethod.POST)
    public Result wechatCharge(@RequestBody WeixinPayParam weixinPayParam, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = paymentService.wechatCharge(weixinPayParam,NetworkUtil.getIpAddress(request));
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryChargeRecordPage", method = RequestMethod.POST)
    public Result queryChargeRecordPage(@RequestBody ChargeRecordParam param, BindingResult validResult) {
        ServiceResult<String, Page<ChargeRecord>> serviceResult = paymentService.queryChargeRecordPage(param);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

}
