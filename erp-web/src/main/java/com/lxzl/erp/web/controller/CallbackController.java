package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.callback.WeixinPayCallbackParam;
import com.lxzl.erp.common.domain.payment.WeixinPayParam;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.statement.StatementService;
import com.lxzl.se.common.domain.Result;
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
 * @date 2018-01-24 20:04
 */
@Controller
@ControllerLog
@RequestMapping("/callback")
public class CallbackController {

    @Autowired
    private ResultGenerator resultGenerator;

    @Autowired
    private StatementService statementService;

    @RequestMapping(value = "weixinPay", method = RequestMethod.POST)
    public Result weixinPay(@RequestBody WeixinPayCallbackParam param, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = statementService.weixinPayCallback(param);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

}
