package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.VerifyStatus;
import com.lxzl.erp.common.domain.workflow.VerifyWorkflowParam;
import org.junit.Test;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-07 19:23
 */
public class WorkflowTest extends ERPUnTransactionalTest {

    @Test
    public void verifyWorkFlow() throws Exception{

        VerifyWorkflowParam workflowParam = new VerifyWorkflowParam();
        workflowParam.setWorkflowLinkId(6);
        workflowParam.setVerifyStatus(VerifyStatus.VERIFY_STATUS_PASS);
        workflowParam.setVerifyOpinion("允许采购");

        TestResult result = getJsonTestResult("/workflow/verifyWorkFlow",workflowParam);
    }
}
