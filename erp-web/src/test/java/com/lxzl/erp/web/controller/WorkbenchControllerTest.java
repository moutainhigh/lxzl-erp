package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.bank.BankSlipDetailQueryParam;
import com.lxzl.erp.common.domain.customer.CustomerCompanyQueryParam;
import com.lxzl.erp.common.domain.customer.CustomerPersonQueryParam;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderQueryParam;
import com.lxzl.erp.common.domain.order.OrderQueryParam;
import com.lxzl.erp.common.domain.statement.StatementOrderQueryParam;
import com.lxzl.erp.common.domain.workbench.*;
import com.lxzl.erp.common.domain.workflow.WorkflowLinkQueryParam;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 20:03 2018/7/16
 * @Modified By:
 */
public class WorkbenchControllerTest extends ERPUnTransactionalTest {

    /**
     * 待审核的工作流 1
     *
     * */
    @Test
    public void queryWaitVerifyWorkflowLinkCount() throws Exception {
        WorkflowLinkQueryParam workflowLinkQueryParam = new WorkflowLinkQueryParam();
        workflowLinkQueryParam.setVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
        TestResult testResult = getJsonTestResult("/workbench/queryWaitVerifyWorkflowLinkCount", workflowLinkQueryParam);
    }
    /**
     * 待审核的工作流 1
     *
     * */
    @Test
    public void queryWaitVerifyWorkflowLinkPage() throws Exception {
        WorkflowLinkQueryParam workflowLinkQueryParam = new WorkflowLinkQueryParam();
        workflowLinkQueryParam.setVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
        TestResult testResult = getJsonTestResult("/workbench/queryWaitVerifyWorkflowLinkPage", workflowLinkQueryParam);
      }
    /**
     * 待认领的流水 1
     *
     * */
    @Test
    public void queryBankSlipDetailCount() throws Exception {
        BankSlipDetailQueryParam bankSlipDetailQueryParam = new BankSlipDetailQueryParam();
        bankSlipDetailQueryParam.setLoanSign(LoanSignType.INCOME);
        bankSlipDetailQueryParam.setDetailStatus(BankSlipDetailStatus.UN_CLAIMED);
        TestResult testResult = getJsonTestResult("/workbench/queryBankSlipDetailCount", bankSlipDetailQueryParam);
    }
    /**
     * 未支付的结算单 0  部分支付的结算单 4
     *
     * */
    @Test
    public void queryStatementOrderCount() throws Exception {
        List<StatementOrderQueryParam> list = new ArrayList<>();
        StatementOrderQueryParam statementOrderQueryParam = new StatementOrderQueryParam();
        statementOrderQueryParam.setStatementOrderStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_INIT);  //未支付的结算单
        list.add(statementOrderQueryParam);
        StatementOrderQueryParam statementOrderQueryParam1 = new StatementOrderQueryParam();
        statementOrderQueryParam1.setStatementOrderStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED_PART);  //部分支付的结算单
        list.add(statementOrderQueryParam1);

        TestResult testResult = getJsonTestResult("/workbench/queryStatementOrderCount", list);
    }
    /**
     * 未支付的结算单 0  部分支付的结算单 4
     *
     * */
    @Test
    public void queryStatementOrderPage() throws Exception {
        StatementOrderQueryParam statementOrderQueryParam = new StatementOrderQueryParam();
        statementOrderQueryParam.setPageNo(1);
        statementOrderQueryParam.setPageSize(Integer.MAX_VALUE);
        statementOrderQueryParam.setStatementOrderStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_INIT);  //未支付的结算单
//        statementOrderQueryParam.setStatementOrderStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED_PART);  //部分支付的结算单
        TestResult testResult = getJsonTestResult("/workbench/queryStatementOrderPage", statementOrderQueryParam);
    }


    /**
     * 审核中的订单 4
     * 待发货的订单 8
     * 到期未处理订单
     *  未支付的订单
     * */
    @Test
    public void queryOrderTest() throws Exception{
        WorkbenchOrderQueryParam param = new WorkbenchOrderQueryParam();
        List<OrderQueryParam> list = new ArrayList<>();
        OrderQueryParam orderQueryParam1 = new OrderQueryParam();
        orderQueryParam1.setOrderStatus(OrderStatus.ORDER_STATUS_VERIFYING);
        list.add(orderQueryParam1);

        OrderQueryParam orderQueryParam2 = new OrderQueryParam();
        orderQueryParam2.setOrderStatus(OrderStatus.ORDER_STATUS_WAIT_DELIVERY);
        list.add(orderQueryParam2);

        OrderQueryParam orderQueryParam3 = new OrderQueryParam();
        orderQueryParam3.setIsReturnOverDue(CommonConstant.DATA_STATUS_ENABLE);
        list.add(orderQueryParam3);

        OrderQueryParam orderQueryParam4 = new OrderQueryParam();
        orderQueryParam4.setPayStatus(PayStatus.PAY_STATUS_INIT);
        list.add(orderQueryParam4);

        param.setOrderQueryParamList(list);
        TestResult testResult = getJsonTestResult("/workbench/queryOrder", param);
    }


    /** 查询可续租的订单 */
    @Test
    public void isCanReletOrderTest() throws Exception{
        OrderQueryParam param = new OrderQueryParam();
        param.setIsCanReletOrder(1);

        TestResult testResult = getJsonTestResult("/workbench/queryCanReletOrder", param);
    }

    /**
     * 未提交的退货单 0
     * 审核中的退货单 4
     * 处理中的退货单 12
     * 被驳回的退货单 24
     * */
    @Test
    public void queryReturnOrderTest() throws Exception{
        WorkbenchReturnOrderQueryParam param = new WorkbenchReturnOrderQueryParam();
        List<K3ReturnOrderQueryParam> list = new ArrayList<>();
        K3ReturnOrderQueryParam k3ReturnOrderQueryParam1 = new K3ReturnOrderQueryParam();
        k3ReturnOrderQueryParam1.setReturnOrderStatus(ReturnOrderStatus.RETURN_ORDER_STATUS_WAIT_COMMIT);
        list.add(k3ReturnOrderQueryParam1);

        K3ReturnOrderQueryParam k3ReturnOrderQueryParam2 = new K3ReturnOrderQueryParam();
        k3ReturnOrderQueryParam2.setReturnOrderStatus(ReturnOrderStatus.RETURN_ORDER_STATUS_VERIFYING);
        list.add(k3ReturnOrderQueryParam2);

        K3ReturnOrderQueryParam k3ReturnOrderQueryParam3 = new K3ReturnOrderQueryParam();
        k3ReturnOrderQueryParam3.setReturnOrderStatus(ReturnOrderStatus.RETURN_ORDER_STATUS_PROCESSING);
        list.add(k3ReturnOrderQueryParam3);

        K3ReturnOrderQueryParam k3ReturnOrderQueryParam4 = new K3ReturnOrderQueryParam();
        k3ReturnOrderQueryParam4.setReturnOrderStatus(ReturnOrderStatus.RETURN_ORDER_STATUS_BACKED);
        list.add(k3ReturnOrderQueryParam4);

        param.setK3ReturnOrderQueryParamList(list);

        TestResult testResult = getJsonTestResult("/workbench/queryReturnOrder", param);
    }

    /**
     * 审核中的企业客户 1
     * 被驳回的企业客户 3
     * */
    @Test
    public void queryCompanyCustomerTest() throws Exception{
        WorkbenchCompanyCustomerQueryParam param = new WorkbenchCompanyCustomerQueryParam();
        List<Integer> List = new ArrayList<>();
        List.add(CustomerStatus.STATUS_COMMIT);
        List.add(CustomerStatus.STATUS_REJECT);
//        param.setCompanyCustomerStatus(List);
        TestResult testResult = getJsonTestResult("/workbench/queryCompanyCustomer", param);
    }

    /**
     * 审核中的个人客户 1
     * 被驳回的个人客户 3
     * */
    @Test
    public void queryPersonCustomerTest() throws Exception{
        WorkbenchPersonCustomerQueryParam param = new WorkbenchPersonCustomerQueryParam();
        List<Integer> List = new ArrayList<>();
        List.add(CustomerStatus.STATUS_COMMIT);
        List.add(CustomerStatus.STATUS_REJECT);
//        param.setPersonCustomerStatusList(List);

        TestResult testResult = getJsonTestResult("/workbench/queryPersonCustomer", param);
    }

    /**
     * 审核中的工作流 1
     * 被驳回的工作流 3
     * */
    @Test
    public void queryWorkflowTest() throws Exception{
        WorkbenchWorkflowQueryParam param = new WorkbenchWorkflowQueryParam();
        List<Integer> List = new ArrayList<>();
        List.add(VerifyStatus.VERIFY_STATUS_COMMIT);
        List.add(VerifyStatus.VERIFY_STATUS_BACK);
//        param.setWorkflowStatusList(List);

        TestResult testResult = getJsonTestResult("/workbench/queryWorkflow", param);
    }

}

