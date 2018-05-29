package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.erpInterface.statementOrder.InterfaceStatementOrderQueryParam;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrder;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.statement.BatchReCreateOrderStatementParam;
import com.lxzl.erp.common.domain.statement.StatementOrderMonthQueryParam;
import com.lxzl.erp.common.domain.statement.StatementOrderPayParam;
import com.lxzl.erp.common.domain.statement.StatementOrderQueryParam;
import com.lxzl.erp.common.domain.statement.pojo.StatementOrder;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
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
import java.math.BigDecimal;
import java.util.List;

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

    @Autowired
    private HttpServletRequest request;


    @RequestMapping(value = "createNew", method = RequestMethod.POST)
    public Result createNewOrderStatement(@RequestBody StatementOrderQueryParam param, BindingResult validResult) {
        ServiceResult<String, BigDecimal> serviceResult = statementService.createOrderStatement(param.getOrderNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "createK3StatementOrder", method = RequestMethod.POST)
    public Result createK3StatementOrder(@RequestBody Order order, BindingResult validResult) {
        ServiceResult<String, BigDecimal> serviceResult = statementService.createK3OrderStatement(order);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "createK3ReturnOrderStatement", method = RequestMethod.POST)
    public Result createK3ReturnOrderStatement(@RequestBody K3ReturnOrder k3ReturnOrder, BindingResult validResult) {
        ServiceResult<String, BigDecimal> serviceResult = statementService.createK3ReturnOrderStatement(k3ReturnOrder.getReturnOrderNo());
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

    @RequestMapping(value = "weixinPay", method = RequestMethod.POST)
    public Result weixinPay(@RequestBody StatementOrderPayParam param, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = statementService.weixinPayStatementOrder(param.getStatementOrderNo(), param.getOpenId(), NetworkUtil.getIpAddress(request));
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

    @RequestMapping(value = "detailByOrderId", method = RequestMethod.POST)
    public Result detailByOrderId(@RequestBody StatementOrderQueryParam statementOrderQueryParam, BindingResult validResult) {
        ServiceResult<String, StatementOrder> serviceResult = statementService.queryStatementOrderDetailByOrderId(statementOrderQueryParam.getOrderNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryStatementOrderCheckParam", method = RequestMethod.POST)
    public Result queryStatementOrderCheckParam(@RequestBody StatementOrderMonthQueryParam statementOrderMonthQueryParam, BindingResult validResult) {
        ServiceResult<String, Page<StatementOrder>> serviceResult = statementService.queryStatementOrderCheckParam(statementOrderMonthQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryStatementOrderMonthDetail", method = RequestMethod.POST)
    public Result queryStatementOrderMonthDetail(@RequestBody StatementOrderMonthQueryParam statementOrderMonthQueryParam, BindingResult validResult) {
        ServiceResult<String, StatementOrder> serviceResult = statementService.queryStatementOrderMonthDetail(statementOrderMonthQueryParam.getStatementOrderCustomerNo(),statementOrderMonthQueryParam.getMonthTime());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "batchPay", method = RequestMethod.POST)
    public Result batchPay(@RequestBody List<StatementOrderPayParam> param, BindingResult validResult) {
        ServiceResult<String, List<String>> serviceResult = statementService.batchPayStatementOrder(param);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "reCreateOrderStatement", method = RequestMethod.POST)
    public Result reCreateOrderStatement(@RequestBody Order order) {
        ServiceResult<String, BigDecimal> serviceResult = statementService.reCreateOrderStatement(order.getOrderNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "batchReCreateOrderStatement", method = RequestMethod.POST)
    public Result batchReCreateOrderStatement(@RequestBody BatchReCreateOrderStatementParam batchReCreateOrderStatementParam) {
        String result = statementService.batchReCreateOrderStatement(batchReCreateOrderStatementParam.getOrderNoList(),batchReCreateOrderStatementParam.getCustomerNoList());
        return resultGenerator.generate(result);
    }
}
