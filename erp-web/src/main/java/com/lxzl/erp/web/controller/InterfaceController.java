package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.customer.pojo.Customer;
import com.lxzl.erp.common.domain.erpInterface.customer.InterfaceCustomerQueryParam;
import com.lxzl.erp.common.domain.erpInterface.order.InterfaceOrderQueryParam;
import com.lxzl.erp.common.domain.erpInterface.statementOrder.InterfaceStatementOrderPayParam;
import com.lxzl.erp.common.domain.erpInterface.statementOrder.InterfaceStatementOrderQueryParam;
import com.lxzl.erp.common.domain.erpInterface.weiXin.InterfaceWeixinChargeParam;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.payment.account.pojo.ChargeRecord;
import com.lxzl.erp.common.domain.statement.pojo.StatementOrder;
import com.lxzl.erp.common.domain.validGroup.customer.QueryCustomerNameGroup;
import com.lxzl.erp.common.domain.validGroup.customer.QueryCustomerNoGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.businessSystemConfig.BusinessSystemConfigService;
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

    @Autowired
    private BusinessSystemConfigService businessSystemConfigService;

    @RequestMapping(value = "queryOrderByNo", method = RequestMethod.POST)
    public Result queryOrderByNo(@RequestBody @Validated(QueryCustomerNoGroup.class) InterfaceOrderQueryParam interfaceOrderQueryParam, BindingResult validResult) {

        boolean erpIdentity = businessSystemConfigService.verifyErpIdentity(interfaceOrderQueryParam.getErpAppId(), interfaceOrderQueryParam.getErpAppSecret());
        if(erpIdentity){
            ServiceResult<String, Order> serviceResult = orderService.queryOrderByNo(interfaceOrderQueryParam.getOrderNo());
            return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
        }

        return resultGenerator.generate(ErrorCode.BUSINESS_SYSTEM_ERROR);

    }

    @RequestMapping(value = "queryAllOrder", method = RequestMethod.POST)
    public Result queryAllOrder(@RequestBody @Validated InterfaceOrderQueryParam interfaceOrderQueryParam, BindingResult validResult) {

        boolean erpIdentity = businessSystemConfigService.verifyErpIdentity(interfaceOrderQueryParam.getErpAppId(), interfaceOrderQueryParam.getErpAppSecret());
        if(erpIdentity){
            ServiceResult<String, Page<Order>> serviceResult = orderService.queryOrderByUserIdInterface(interfaceOrderQueryParam);
            return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
        }

        return resultGenerator.generate(ErrorCode.BUSINESS_SYSTEM_ERROR);

    }

    @RequestMapping(value = "queryCustomerByName", method = RequestMethod.POST)
    public Result queryCustomerByName(@RequestBody @Validated(QueryCustomerNameGroup.class) InterfaceCustomerQueryParam interfaceCustomerQueryParam, BindingResult validResult) {

        boolean erpIdentity = businessSystemConfigService.verifyErpIdentity(interfaceCustomerQueryParam.getErpAppId(), interfaceCustomerQueryParam.getErpAppSecret());
        if(erpIdentity){
            ServiceResult<String, Customer> serviceResult = customerService.queryCustomerByCompanyName(interfaceCustomerQueryParam.getCustomerName());
            return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
        }
        return resultGenerator.generate(ErrorCode.BUSINESS_SYSTEM_ERROR);

    }

    @RequestMapping(value = "queryStatementOrder", method = RequestMethod.POST)
    public Result queryStatementOrder(@RequestBody @Validated InterfaceStatementOrderQueryParam interfaceStatementOrderQueryParam, BindingResult validResult) {
        boolean erpIdentity = businessSystemConfigService.verifyErpIdentity(interfaceStatementOrderQueryParam.getErpAppId(), interfaceStatementOrderQueryParam.getErpAppSecret());
        if(erpIdentity){
            ServiceResult<String, Page<StatementOrder>> serviceResult = statementService.queryStatementOrder(interfaceStatementOrderQueryParam);
            return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
        }
        return resultGenerator.generate(ErrorCode.BUSINESS_SYSTEM_ERROR);

    }

    @RequestMapping(value = "queryStatementOrderDetail", method = RequestMethod.POST)
    public Result queryStatementOrderDetail(@RequestBody @Validated InterfaceStatementOrderQueryParam interfaceStatementOrderQueryParam, BindingResult validResult) {
        boolean erpIdentity = businessSystemConfigService.verifyErpIdentity(interfaceStatementOrderQueryParam.getErpAppId(), interfaceStatementOrderQueryParam.getErpAppSecret());
        if(erpIdentity){
            ServiceResult<String, StatementOrder> serviceResult = statementService.queryStatementOrderDetail(interfaceStatementOrderQueryParam.getStatementOrderNo());
            return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
        }
        return resultGenerator.generate(ErrorCode.BUSINESS_SYSTEM_ERROR);

    }

    @RequestMapping(value = "weixinPayStatementOrder", method = RequestMethod.POST)
    public Result weixinPayStatementOrder(@RequestBody @Validated InterfaceStatementOrderPayParam interfaceStatementOrderPayParam, BindingResult validResult) {
        boolean erpIdentity = businessSystemConfigService.verifyErpIdentity(interfaceStatementOrderPayParam.getErpAppId(), interfaceStatementOrderPayParam.getErpAppSecret());
        if(erpIdentity){
            ServiceResult<String, String> serviceResult = statementService.weixinPayStatementOrder(interfaceStatementOrderPayParam.getStatementOrderNo(), interfaceStatementOrderPayParam.getOpenId(), NetworkUtil.getIpAddress(request));
            return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
        }
        return resultGenerator.generate(ErrorCode.BUSINESS_SYSTEM_ERROR);

    }

    @RequestMapping(value = "wechatCharge", method = RequestMethod.POST)
    public Result wechatCharge(@RequestBody @Validated InterfaceWeixinChargeParam interfaceWeixinChargeParam, BindingResult validResult) {
        boolean erpIdentity = businessSystemConfigService.verifyErpIdentity(interfaceWeixinChargeParam.getErpAppId(), interfaceWeixinChargeParam.getErpAppSecret());
        if(erpIdentity){
            ServiceResult<String, String> serviceResult = paymentService.wechatCharge(interfaceWeixinChargeParam.getCustomerNo(),interfaceWeixinChargeParam.getAmount(),interfaceWeixinChargeParam.getOpenId(),NetworkUtil.getIpAddress(request));
            return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
        }
        return resultGenerator.generate(ErrorCode.BUSINESS_SYSTEM_ERROR);
    }

    @RequestMapping(value = "queryChargeRecordPage", method = RequestMethod.POST)
    public Result queryChargeRecordPage(@RequestBody @Validated(QueryCustomerNoGroup.class) InterfaceCustomerQueryParam interfaceCustomerQueryParam, BindingResult validResult) {
        boolean erpIdentity = businessSystemConfigService.verifyErpIdentity(interfaceCustomerQueryParam.getErpAppId(), interfaceCustomerQueryParam.getErpAppSecret());
        if(erpIdentity){
            ServiceResult<String, Page<ChargeRecord>> serviceResult = paymentService.queryChargeRecordPage(interfaceCustomerQueryParam.getCustomerNo());
            return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
        }
        return resultGenerator.generate(ErrorCode.BUSINESS_SYSTEM_ERROR);
    }

    @RequestMapping(value = "queryCustomerByNo", method = RequestMethod.POST)
    public Result queryCustomerByNo(@RequestBody @Validated(QueryCustomerNoGroup.class) InterfaceCustomerQueryParam interfaceCustomerQueryParam, BindingResult validResult) {
        boolean erpIdentity = businessSystemConfigService.verifyErpIdentity(interfaceCustomerQueryParam.getErpAppId(), interfaceCustomerQueryParam.getErpAppSecret());
        if(erpIdentity){
            ServiceResult<String, Customer> serviceResult = customerService.queryCustomerByNo(interfaceCustomerQueryParam.getCustomerNo());
            return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
        }
        return resultGenerator.generate(ErrorCode.BUSINESS_SYSTEM_ERROR);
    }

}
