package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.customer.pojo.Customer;
import com.lxzl.erp.common.domain.payment.account.pojo.CustomerAccount;
import com.lxzl.erp.common.domain.payment.account.pojo.ManualChargeParam;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.payment.PaymentService;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    @RequestMapping(value = "queryAccount", method = RequestMethod.POST)
    public Result queryAccount(@RequestBody Customer customer, BindingResult validResult) {
        CustomerAccount result = paymentService.queryCustomerAccount(customer.getCustomerNo());
        return resultGenerator.generate(ErrorCode.SUCCESS, result);
    }

    @RequestMapping(value = "manualCharge", method = RequestMethod.POST)
    public Result manualCharge(@RequestBody ManualChargeParam param, BindingResult validResult) {
        ServiceResult<String, Boolean> result = paymentService.manualCharge(param.getBusinessCustomerNo(), param.getChargeAmount());
        return resultGenerator.generate(result.getErrorCode(), result.getResult());
    }
}
