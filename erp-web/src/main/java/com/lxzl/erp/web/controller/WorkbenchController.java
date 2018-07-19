package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.bank.BankSlipDetailQueryParam;
import com.lxzl.erp.common.domain.order.OrderQueryParam;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.statement.StatementOrderQueryParam;
import com.lxzl.erp.common.domain.statement.pojo.StatementOrder;
import com.lxzl.erp.common.domain.workbench.*;
import com.lxzl.erp.common.domain.workflow.WorkflowLinkQueryParam;
import com.lxzl.erp.common.domain.workflow.pojo.WorkflowLink;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.workbench.WorkbenchService;
import com.lxzl.se.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * @Author: Pengbinjie
 * @Description：
 * @Date: Created in 14:43 2018/7/16
 * @Modified By:
 */
@RequestMapping("workbench")
@Controller
@ControllerLog
public class WorkbenchController {
    /**
     * 查询审核中,到期未处理,待发货的订单
     *
     * @param
     * @return Result
     */
    @RequestMapping(value = "queryOrder", method = RequestMethod.POST)
    public Result queryOrder(@RequestBody WorkbenchOrderQueryParam workbenchOrderQueryParam, BindingResult validResult) {
        ServiceResult<String, Map<String,Integer>> serviceResult = workbenchService.queryVerifingOrder(workbenchOrderQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 查询可续租的订单
     *
     * @param
     * @return Result
     */
    @RequestMapping(value = "queryCanReletOrder", method = RequestMethod.POST)
    public Result queryCanReletOrder(@RequestBody OrderQueryParam orderQueryParam, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = workbenchService.queryCanReletOrder(orderQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 分页展示可续租的订单
     */
    @RequestMapping(value = "queryCanReletOrderPage", method = RequestMethod.POST)
    public Result queryCanReletOrderPage(@RequestBody OrderQueryParam orderQueryParam, BindingResult validResult) {
        ServiceResult<String, Page<Order>> serviceResult = workbenchService.queryCanReletOrderPage(orderQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 查询审核中，被驳回，处理中，未提交退货单
     * @param workbenchReturnOrderQueryParam
     * @param validResult
     * @return
     */
    @RequestMapping(value = "queryReturnOrder", method = RequestMethod.POST)
    public Result queryReturnOrder(@RequestBody WorkbenchReturnOrderQueryParam workbenchReturnOrderQueryParam, BindingResult validResult) {
        ServiceResult<String, Map<String,Integer>> serviceResult = workbenchService.queryReturnOrder(workbenchReturnOrderQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 查询审核中，被驳回的企业客户
     * @param workbenchCompanyCustomerQueryParam
     * @param validResult
     * @return
     */
    @RequestMapping(value = "queryCompanyCustomer", method = RequestMethod.POST)
    public Result queryCompanyCustomer(@RequestBody WorkbenchCompanyCustomerQueryParam workbenchCompanyCustomerQueryParam, BindingResult validResult) {
        ServiceResult<String,  Map<String,Integer>> serviceResult = workbenchService.queryCompanyCustomer(workbenchCompanyCustomerQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 查询审核中，被驳回的个人客户
     * @param workbenchPersonCustomerQueryParam
     * @param validResult
     * @return
     */
    @RequestMapping(value = "queryPersonCustomer", method = RequestMethod.POST)
    public Result queryPersonCustomer(@RequestBody WorkbenchPersonCustomerQueryParam workbenchPersonCustomerQueryParam, BindingResult validResult) {
        ServiceResult<String,  Map<String,Integer>> serviceResult = workbenchService.queryPersonCustomer(workbenchPersonCustomerQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     *查询审核中，被驳回的工作流
     *
     */
    @RequestMapping(value = "queryWorkflow", method = RequestMethod.POST)
    public Result queryWorkflow(@RequestBody WorkbenchWorkflowQueryParam workbenchWorkflowQueryParam, BindingResult validResult) {
        ServiceResult<String, Map<String,Integer>> serviceResult = workbenchService.queryWorkflow(workbenchWorkflowQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }


   /**
    *@描述 查询待审核工作流总数
    *@param  workflowLinkQueryParam
    *@param validResult
    *@return  com.lxzl.se.common.domain.Result
    */
    @RequestMapping(value = "queryWaitVerifyWorkflowLinkCount", method = RequestMethod.POST)
    public Result queryWaitVerifyWorkflowLinkCount(@RequestBody  WorkflowLinkQueryParam workflowLinkQueryParam, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = workbenchService.queryWaitVerifyWorkflowLinkCount(workflowLinkQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
    *@描述 查询待审核工作流分页
    *@param  workflowLinkQueryParam
    *@param validResult
    *@return  com.lxzl.se.common.domain.Result
    */
    @RequestMapping(value = "queryWaitVerifyWorkflowLinkPage", method = RequestMethod.POST)
    public Result queryWaitVerifyWorkflowLinkPage(@RequestBody  WorkflowLinkQueryParam workflowLinkQueryParam, BindingResult validResult) {
        ServiceResult<String, Page<WorkflowLink>> serviceResult = workbenchService.queryWaitVerifyWorkflowLinkPage(workflowLinkQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
    *@描述 查询待认领的银行流水数量
    *@param  bankSlipDetailQueryParam
    *@param validResult
    *@return  com.lxzl.se.common.domain.Result
    */
    @RequestMapping(value = "queryBankSlipDetailCount", method = RequestMethod.POST)
    public Result queryBankSlipDetailCount(@RequestBody  BankSlipDetailQueryParam bankSlipDetailQueryParam, BindingResult validResult) {
        ServiceResult<String, Integer> serviceResult = workbenchService.queryBankSlipDetailCount(bankSlipDetailQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
    *@描述 未支付,部分支付的结算单总数
    *@param  statementOrderQueryParamList
    *@param validResult
    *@return  com.lxzl.se.common.domain.Result
    */
    @RequestMapping(value = "queryStatementOrderCount", method = RequestMethod.POST)
    public Result queryStatementOrderCount(@RequestBody  List<StatementOrderQueryParam>  statementOrderQueryParamList, BindingResult validResult) {
        ServiceResult<String, Map<String, Integer>> serviceResult = workbenchService.queryStatementOrderCount(statementOrderQueryParamList);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     *@描述 未支付,部分支付的结算单分页
     *@param  statementOrderQueryParam
     *@param validResult
     *@return  com.lxzl.se.common.domain.Result
     */
    @RequestMapping(value = "queryStatementOrderPage", method = RequestMethod.POST)
    public Result queryStatementOrderPage(@RequestBody  StatementOrderQueryParam statementOrderQueryParam, BindingResult validResult) {
        ServiceResult<String, Page<StatementOrder>> serviceResult = workbenchService.queryStatementOrderPage(statementOrderQueryParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }


    @Autowired
    private ResultGenerator resultGenerator;

    @Autowired
    private WorkbenchService workbenchService;

}
