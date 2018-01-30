package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.customer.pojo.Customer;
import com.lxzl.erp.common.domain.payment.*;
import com.lxzl.erp.common.domain.payment.account.pojo.ChargeRecord;
import com.lxzl.erp.common.domain.payment.account.pojo.CustomerAccount;
import com.lxzl.erp.common.domain.payment.account.pojo.CustomerAccountLogSummary;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.payment.PaymentService;
import com.lxzl.erp.web.util.NetworkUtil;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;


/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-14 19:05
 */
@Controller
@ControllerLog
@RequestMapping("/payment")
public class PaymentController extends BaseController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ResultGenerator resultGenerator;

    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "queryAccount", method = RequestMethod.POST)
    public Result queryAccount(@RequestBody Customer customer, BindingResult validResult) {
        CustomerAccount result = paymentService.queryCustomerAccount(customer.getCustomerNo());
        return resultGenerator.generate(ErrorCode.SUCCESS, result);
    }

    @RequestMapping(value = "manualCharge", method = RequestMethod.POST)
    public Result manualCharge(@RequestBody ManualChargeParam param, BindingResult validResult) {
        ServiceResult<String, Boolean> result = paymentService.manualCharge(param);
        return resultGenerator.generate(result.getErrorCode(), result.getResult());
    }

    @RequestMapping(value = "manualDeduct", method = RequestMethod.POST)
    public Result manualDeduct(@RequestBody ManualDeductParam param, BindingResult validResult) {
        ServiceResult<String, Boolean> result = paymentService.manualDeduct(param);
        return resultGenerator.generate(result.getErrorCode(), result.getResult());
    }

    @RequestMapping(value = "wechatCharge", method = RequestMethod.POST)
    public Result wechatCharge(@RequestBody WeixinChargeParam parm, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = paymentService.wechatCharge(parm.getCustomerNo(),parm.getAmount(),parm.getOpenId(),NetworkUtil.getIpAddress(request));
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryChargeRecordPage", method = RequestMethod.POST)
    public Result queryChargeRecordPage(@RequestBody Customer customer, BindingResult validResult) {
        ServiceResult<String, Page<ChargeRecord>> serviceResult = paymentService.queryChargeRecordPage(customer.getCustomerNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryChargeRecordParamPage", method = RequestMethod.POST)
    public Result queryChargeRecordParamPage(@RequestBody ChargeRecordParam param, BindingResult validResult) {
        ServiceResult<String, Page<ChargeRecord>> serviceResult = paymentService.queryChargeRecordParamPage(param);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryCustomerAccountLogPage", method = RequestMethod.POST)
    public Result queryCustomerAccountLogPage(@RequestBody CustomerAccountLogParam param, BindingResult validResult) {
        ServiceResult<String, CustomerAccountLogSummary> serviceResult = paymentService.queryCustomerAccountLogPage(param);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
}
