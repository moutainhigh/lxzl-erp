package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.statement.StatementOrderPayParam;
import com.lxzl.erp.common.domain.statement.StatementOrderQueryParam;
import com.lxzl.erp.common.domain.statement.pojo.StatementOrder;
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
        ServiceResult<String, BigDecimal> serviceResult = statementService.createOrderStatement(param.getOrderNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "createChangeOrderStatement", method = RequestMethod.POST)
    public Result createChangeOrderStatement(@RequestBody StatementOrderQueryParam param, BindingResult validResult) {
        ServiceResult<String, BigDecimal> serviceResult = statementService.createChangeOrderStatement(param.getChangeOrderNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "createReturnOrderStatement", method = RequestMethod.POST)
    public Result createReturnOrderStatement(@RequestBody StatementOrderQueryParam param, BindingResult validResult) {
        ServiceResult<String, BigDecimal> serviceResult = statementService.createReturnOrderStatement(param.getReturnOrderNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "pay", method = RequestMethod.POST)
    public Result pay(@RequestBody StatementOrderPayParam param, BindingResult validResult) {
        ServiceResult<String, Boolean> serviceResult = statementService.payStatementOrder(param.getStatementOrderNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "page", method = RequestMethod.POST)
    public Result page(@RequestBody StatementOrderQueryParam statementOrderQueryParam, BindingResult validResult) {
        ServiceResult<String, Page<StatementOrder>> serviceResult = statementService.queryStatementOrder(statementOrderQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "detail", method = RequestMethod.POST)
    public Result detail(@RequestBody StatementOrderQueryParam statementOrderQueryParam, BindingResult validResult) {
        ServiceResult<String, StatementOrder> serviceResult = statementService.queryStatementOrderDetail(statementOrderQueryParam.getStatementOrderNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }


}
