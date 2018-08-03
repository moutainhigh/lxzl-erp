package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.bank.BankSlipDetailQueryParam;
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
//        workflowLinkQueryParam.setVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
//        workflowLinkQueryParam.setIsWorkbench(true);
        TestResult testResult = getJsonTestResult("/workbench/queryWaitVerifyWorkflowLinkCount", workflowLinkQueryParam);
    }
    /**
     * 待审核的工作流 1
     *
     * */
    @Test
    public void queryWaitVerifyWorkflowLinkPage() throws Exception {
        WorkflowLinkQueryParam workflowLinkQueryParam = new WorkflowLinkQueryParam();
        workflowLinkQueryParam.setPageNo(1);
        workflowLinkQueryParam.setPageSize(Integer.MAX_VALUE);
//        workflowLinkQueryParam.setVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
//        workflowLinkQueryParam.setIsWorkbench(1);
        TestResult testResult = getJsonTestResult("/workflow/queryWorkflowLinkPage", workflowLinkQueryParam);
      }
    /**
     * 待认领的流水 1
     *
     * */
    @Test
    public void queryBankSlipDetailCount() throws Exception {
        BankSlipDetailQueryParam bankSlipDetailQueryParam = new BankSlipDetailQueryParam();
//        bankSlipDetailQueryParam.setLoanSign(LoanSignType.INCOME);
//        bankSlipDetailQueryParam.setDetailStatus(BankSlipDetailStatus.UN_CLAIMED);
        TestResult testResult = getJsonTestResult("/workbench/queryBankSlipDetailCount", bankSlipDetailQueryParam);
    }
    /**
     * 未支付的结算单 0  部分支付的结算单 4
     *
     * */
    @Test
    public void queryStatementOrderCount() throws Exception {
        WorkbenchStatementOrderQueryParam  workbenchStatementOrderQueryParam = new WorkbenchStatementOrderQueryParam();
        List<StatementOrderQueryParam> list = new ArrayList<>();
        StatementOrderQueryParam statementOrderQueryParam = new StatementOrderQueryParam();
        statementOrderQueryParam.setStatementOrderStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_INIT);  //未支付的结算单
        list.add(statementOrderQueryParam);
        StatementOrderQueryParam statementOrderQueryParam1 = new StatementOrderQueryParam();
        statementOrderQueryParam1.setStatementOrderStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED_PART);  //部分支付的结算单
        list.add(statementOrderQueryParam1);
        StatementOrderQueryParam statementOrderQueryParam2 = new StatementOrderQueryParam();
//        statementOrderQueryParam2.setStatementOrderStatus(StatementOrderStatus.STATEMENT_ORDER_STATUS_SETTLED_PART);  //部分支付的结算单
        list.add(statementOrderQueryParam2);
        workbenchStatementOrderQueryParam.setStatementOrderQueryParamList(list);
        TestResult testResult = getJsonTestResult("/workbench/queryStatementOrderCount", workbenchStatementOrderQueryParam);
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
//        statementOrderQueryParam.setIsWorkbench(true);
        TestResult testResult = getJsonTestResult("/statementOrder/page", statementOrderQueryParam);
    }


    /**
     * 审核中的订单 4
     * 待发货的订单 8
     * 到期未处理订单
     *  未支付的订单
     * */
    @Test
    public void queryOrderTest() throws Exception{
        WorkbenchQueryParam param = new WorkbenchQueryParam();
        param.setIsDisabled(1);
        param.setIsRecycleBin(1);
        //业务工作台
        param.setWorkbenchName(CommonConstant.COMMON_ZERO);
        //商务工作台
//        param.setWorkbenchName(CommonConstant.COMMON_ONE);
        //商务+业务工作台
//        param.setWorkbenchName(CommonConstant.COMMON_TWO);

        TestResult testResult = getJsonTestResult("/workbench/queryWorkbenchCount", param);
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
        List<Integer> list = new ArrayList<>();
        list.add(ReturnOrderStatus.RETURN_ORDER_STATUS_WAIT_COMMIT);
        list.add(ReturnOrderStatus.RETURN_ORDER_STATUS_VERIFYING);
        list.add(ReturnOrderStatus.RETURN_ORDER_STATUS_PROCESSING);
        list.add(ReturnOrderStatus.RETURN_ORDER_STATUS_BACKED);

        param.setReturnOrderStatusList(list);

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
        param.setCustomerCompanyStatusList(List);
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
        param.setCustomerPersonStatusList(List);

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
        param.setWorkflowLinkStatusList(List);

        TestResult testResult = getJsonTestResult("/workbench/queryWorkflow", param);
    }

}

