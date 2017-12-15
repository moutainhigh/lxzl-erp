package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.statement.StatementOrderQueryParam;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.statement.StatementService;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.math.BigDecimal;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-12 8:48
 */
@Controller
@ControllerLog
@RequestMapping("statementOrder")
public class StatementController extends BaseController {
    @Autowired
    private StatementService statementService;

    @Autowired
    private ResultGenerator resultGenerator;


    @RequestMapping(value = "createNew", method = RequestMethod.POST)
    public Result createNewOrderStatement(@RequestBody StatementOrderQueryParam param, BindingResult validResult) {
        ServiceResult<String, BigDecimal> serviceResult = statementService.createNewStatementOrder(param.getOrderNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
}
