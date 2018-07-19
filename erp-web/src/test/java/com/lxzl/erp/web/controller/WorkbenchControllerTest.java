package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.BankSlipDetailStatus;
import com.lxzl.erp.common.constant.LoanSignType;
import com.lxzl.erp.common.constant.StatementOrderStatus;
import com.lxzl.erp.common.constant.VerifyStatus;
import com.lxzl.erp.common.domain.bank.BankSlipDetailQueryParam;
import com.lxzl.erp.common.domain.customer.CustomerCompanyQueryParam;
import com.lxzl.erp.common.domain.customer.CustomerPersonQueryParam;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderQueryParam;
import com.lxzl.erp.common.domain.order.OrderQueryParam;
import com.lxzl.erp.common.domain.statement.StatementOrderQueryParam;
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
     *
     * */
    @Test
    public void queryOrderTest() throws Exception{
        OrderQueryParam param = new OrderQueryParam();
        param.setOrderStatus(4);
//        param.setOrderStatus(8);

        TestResult testResult = getJsonTestResult("/workbench/queryOrder", param);
    }

    /** 到期未处理的订单 */
    @Test
    public void isReturnOverDueOrderTest() throws Exception{
        OrderQueryParam param = new OrderQueryParam();
        param.setIsReturnOverDue(1);

        TestResult testResult = getJsonTestResult("/workbench/queryOrder", param);
    }

    /** 可续租的订单 */


    /**
     * 未提交的退货单 0
     * 审核中的退货单 4
     * 处理中的退货单 12
     * 被驳回的退货单 24
     * */
    @Test
    public void queryReturnOrderTest() throws Exception{
        K3ReturnOrderQueryParam param = new K3ReturnOrderQueryParam();
        param.setReturnOrderStatus(4);

        TestResult testResult = getJsonTestResult("/workbench/queryReturnOrder", param);
    }

    /**
     * 审核中的企业客户 1
     * 被驳回的企业客户 3
     * */
    @Test
    public void queryCompanyCustomerTest() throws Exception{
        CustomerCompanyQueryParam param = new CustomerCompanyQueryParam();
        param.setCustomerStatus(3);

        TestResult testResult = getJsonTestResult("/workbench/queryCompanyCustomer", param);
    }

    /**
     * 审核中的个人客户 1
     * 被驳回的个人客户 3
     * */
    @Test
    public void queryPersonCustomerTest() throws Exception{
        CustomerPersonQueryParam param = new CustomerPersonQueryParam();
        param.setCustomerStatus(3);

        TestResult testResult = getJsonTestResult("/workbench/queryPersonCustomer", param);
    }

    /**
     * 审核中的工作流 1
     * 被驳回的工作流 3
     * */
    @Test
    public void queryWorkflowTest() throws Exception{
        WorkflowLinkQueryParam param = new WorkflowLinkQueryParam();
        param.setVerifyStatus(3);

        TestResult testResult = getJsonTestResult("/workbench/queryWorkflow", param);
    }

}

