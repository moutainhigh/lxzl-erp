package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.common.constant.VerifyStatus;
import com.lxzl.erp.common.constant.WorkflowType;
import com.lxzl.erp.common.domain.workflow.VerifyWorkflowParam;
import com.lxzl.erp.common.domain.workflow.WorkflowLinkQueryParam;
import com.lxzl.se.common.domain.Result;
import org.junit.Test;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-07 19:23
 */
public class WorkflowTest extends ERPUnTransactionalTest {

    @Test
    public void verifyWorkFlow() throws Exception {

        VerifyWorkflowParam workflowParam = new VerifyWorkflowParam();
        workflowParam.setWorkflowLinkNo("WL201712121358434671147");
        workflowParam.setVerifyStatus(VerifyStatus.VERIFY_STATUS_PASS);
        workflowParam.setVerifyOpinion("允许换货");
//        workflowParam.setNextVerifyUser(500003);

        Result result = getJsonTestResult("/workflow/verifyWorkFlow", workflowParam);
    }

    @Test
    public void queryNextVerifyUsers() throws Exception {
        WorkflowLinkQueryParam workflowLinkQueryParam = new WorkflowLinkQueryParam();
        workflowLinkQueryParam.setWorkflowType(WorkflowType.WORKFLOW_TYPE_ORDER_INFO);
        workflowLinkQueryParam.setWorkflowReferNo("O201712111658417621539");
        Result result = getJsonTestResult("/workflow/queryNextVerifyUsers", workflowLinkQueryParam);
    }

    @Test
    public void queryWorkflowLinkPage() throws Exception {
        WorkflowLinkQueryParam workflowLinkQueryParam = new WorkflowLinkQueryParam();
//        workflowLinkQueryParam.setWorkflowType(WorkflowType.WORKFLOW_TYPE_PURCHASE);
        workflowLinkQueryParam.setWorkflowReferNo("");
        Result result = getJsonTestResult("/workflow/queryWorkflowLinkPage", workflowLinkQueryParam);
    }

    @Test
    public void queryWorkflowLinkDetail() throws Exception {
        WorkflowLinkQueryParam workflowLinkQueryParam = new WorkflowLinkQueryParam();
        workflowLinkQueryParam.setWorkflowLinkNo("WL201711282117102791529");
        Result result = getJsonTestResult("/workflow/queryWorkflowLinkDetail", workflowLinkQueryParam);
    }

    @Test
    public void commitWorkFlow() throws Exception {
        WorkflowLinkQueryParam workflowLinkQueryParam = new WorkflowLinkQueryParam();
        workflowLinkQueryParam.setWorkflowReferNo("PO201711291004320305000011635");
        workflowLinkQueryParam.setWorkflowType(WorkflowType.WORKFLOW_TYPE_PURCHASE);
        workflowLinkQueryParam.setVerifyStatus(WorkflowType.WORKFLOW_TYPE_PURCHASE);
        Result result = getJsonTestResult("/workflow/commitWorkFlow", workflowLinkQueryParam);
    }

    @Test
    public void isNeedVerify() throws Exception {
        WorkflowLinkQueryParam workflowLinkQueryParam = new WorkflowLinkQueryParam();
        workflowLinkQueryParam.setWorkflowType(WorkflowType.WORKFLOW_TYPE_PURCHASE);
        Result result = getJsonTestResult("/workflow/isNeedVerify", workflowLinkQueryParam);
    }
}
