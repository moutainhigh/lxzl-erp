package com.lxzl.erp.core.service.workbench;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.bank.BankSlipDetailQueryParam;
import com.lxzl.erp.common.domain.customer.CustomerCompanyQueryParam;
import com.lxzl.erp.common.domain.customer.CustomerPersonQueryParam;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderQueryParam;
import com.lxzl.erp.common.domain.order.OrderQueryParam;
import com.lxzl.erp.common.domain.statement.StatementOrderQueryParam;
import com.lxzl.erp.common.domain.statement.pojo.StatementOrder;
import com.lxzl.erp.common.domain.workflow.WorkflowLinkQueryParam;
import com.lxzl.erp.common.domain.workflow.pojo.WorkflowLink;

import java.util.List;
import com.lxzl.erp.common.domain.order.OrderQueryParam;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.workbench.*;

import java.util.Map;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 17:07 2018/7/16
 * @Modified By:
 */
public interface WorkbenchService {

    /**
     * 查询审核中,到期未处理,待发货的订单
     *
     * @param
     * @return Result
     */
    ServiceResult<String,Map<String,Integer>> queryVerifingOrder(WorkbenchOrderQueryParam workbenchOrderQueryParam);

    /**
     * 查询可续租的订单
     *
     * @param
     * @return Result
     */
    ServiceResult<String,Integer> queryCanReletOrder(OrderQueryParam orderQueryParam);

    /**
     * 分页展示可续租的订单
     * @param orderQueryParam
     * @return
     */
    ServiceResult<String,Page<Order>> queryCanReletOrderPage(OrderQueryParam orderQueryParam);

    /**
     * 查询审核中，被驳回，处理中，未提交退货单
     * @param workbenchReturnOrderQueryParam
     * @return
     */
    ServiceResult<String,Map<String,Integer>> queryReturnOrder(WorkbenchReturnOrderQueryParam workbenchReturnOrderQueryParam);

    /**
     * 查询审核中，被驳回的企业客户
     * @param workbenchCompanyCustomerQueryParam
     * @return
     */
    ServiceResult<String, Map<String,Integer>> queryCompanyCustomer(WorkbenchCompanyCustomerQueryParam workbenchCompanyCustomerQueryParam);

    /**
     * 查询审核中，被驳回的个人客户
     * @param workbenchPersonCustomerQueryParam
     * @return
     */
    ServiceResult<String, Map<String,Integer>> queryPersonCustomer(WorkbenchPersonCustomerQueryParam workbenchPersonCustomerQueryParam);

    /**
     *查询审核中，被驳回的工作流
     *
     */
    ServiceResult<String,Map<String,Integer>> queryWorkflow(WorkbenchWorkflowQueryParam workbenchWorkflowQueryParam);
    /**
     *查询待审核工作流总数
     *
     */
    ServiceResult<String,Integer> queryWaitVerifyWorkflowLinkCount(WorkflowLinkQueryParam workflowLinkQueryParam);
    /**
     *查询审核中，被驳回的工作流
     *
     */
    ServiceResult<String, Page<WorkflowLink>> queryWaitVerifyWorkflowLinkPage(WorkflowLinkQueryParam workflowLinkQueryParam);
    /**
     *查询审核中，被驳回的工作流
     *
     */
    ServiceResult<String,Integer> queryBankSlipDetailCount(BankSlipDetailQueryParam bankSlipDetailQueryParam);
    /**
     *查询审核中，被驳回的工作流
     *
     */
    ServiceResult<String, List<Map<String, Integer>>> queryStatementOrderCount(List<StatementOrderQueryParam> statementOrderQueryParamList);
    /**
     *查询审核中，被驳回的工作流
     *
     */
    ServiceResult<String, Page<StatementOrder>>  queryStatementOrderPage(StatementOrderQueryParam statementOrderQueryParam);

}
