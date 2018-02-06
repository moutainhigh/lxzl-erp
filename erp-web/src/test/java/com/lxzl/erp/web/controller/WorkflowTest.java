package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.VerifyStatus;
import com.lxzl.erp.common.constant.WorkflowType;
import com.lxzl.erp.common.domain.workflow.VerifyWorkflowParam;
import com.lxzl.erp.common.domain.workflow.WorkflowLinkQueryParam;
import com.lxzl.erp.common.domain.workflow.WorkflowTemplateQueryParam;
import com.lxzl.erp.common.domain.workflow.pojo.WorkflowNode;
import com.lxzl.erp.common.domain.workflow.pojo.WorkflowTemplate;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
        workflowParam.setWorkflowLinkNo("LXWF-500030-20180201-00005");
        workflowParam.setVerifyStatus(VerifyStatus.VERIFY_STATUS_PASS);
//        workflowParam.setVerifyStatus(VerifyStatus.VERIFY_STATUS_BACK);
        workflowParam.setReturnType(1);
//        workflowParam.setNextVerifyUser(500003);

        TestResult testResult = getJsonTestResult("/workflow/verifyWorkFlow", workflowParam);
    }

    @Test
    public void queryNextVerifyUsers() throws Exception {
        WorkflowLinkQueryParam workflowLinkQueryParam = new WorkflowLinkQueryParam();
        workflowLinkQueryParam.setWorkflowType(WorkflowType.WORKFLOW_TYPE_PURCHASE);
        workflowLinkQueryParam.setWorkflowReferNo("PO201712291657381525000021440");
        TestResult testResult = getJsonTestResult("/workflow/queryNextVerifyUsers", workflowLinkQueryParam);
    }

    @Test
    public void queryWorkflowLinkPage() throws Exception {
        WorkflowLinkQueryParam workflowLinkQueryParam = new WorkflowLinkQueryParam();
//        workflowLinkQueryParam.setWorkflowType(WorkflowType.WORKFLOW_TYPE_PURCHASE);
        workflowLinkQueryParam.setWorkflowReferNo("");
        TestResult testResult = getJsonTestResult("/workflow/queryWorkflowLinkPage", workflowLinkQueryParam);
    }

    @Test
    public void queryWorkflowLinkDetail() throws Exception {
        WorkflowLinkQueryParam workflowLinkQueryParam = new WorkflowLinkQueryParam();
        workflowLinkQueryParam.setWorkflowLinkNo("WL201711282117102791529");
        TestResult testResult = getJsonTestResult("/workflow/queryWorkflowLinkDetail", workflowLinkQueryParam);
    }

    @Test
    public void commitWorkFlow() throws Exception {
        WorkflowLinkQueryParam workflowLinkQueryParam = new WorkflowLinkQueryParam();
        workflowLinkQueryParam.setWorkflowReferNo("PO201711291004320305000011635");
        workflowLinkQueryParam.setWorkflowType(WorkflowType.WORKFLOW_TYPE_PURCHASE);
        workflowLinkQueryParam.setVerifyStatus(WorkflowType.WORKFLOW_TYPE_PURCHASE);
        TestResult testResult = getJsonTestResult("/workflow/commitWorkFlow", workflowLinkQueryParam);
    }

    @Test
    public void isNeedVerify() throws Exception {
        WorkflowLinkQueryParam workflowLinkQueryParam = new WorkflowLinkQueryParam();
        workflowLinkQueryParam.setWorkflowType(WorkflowType.WORKFLOW_TYPE_PURCHASE);
        TestResult testResult = getJsonTestResult("/workflow/isNeedVerify", workflowLinkQueryParam);
    }


    @Test
    public void updateWorkflowNodeTest() throws Exception {
        WorkflowTemplate workflowTemplate = new WorkflowTemplate();
        workflowTemplate.setWorkflowTemplateId(7);
        List<WorkflowNode> workflowNodeList = new ArrayList<>();
        WorkflowNode workflowNode1 = new WorkflowNode();
        workflowNode1.setWorkflowNodeName("test3");
//        workflowNode1.setWorkflowDepartmentType(300007);
//        workflowNode1.setWorkflowDepartment(400040);
        workflowNode1.setWorkflowUser(500006);
//        workflowNode1.setWorkflowRole(600012);
        workflowNodeList.add(workflowNode1);
        WorkflowNode workflowNode2 = new WorkflowNode();
        workflowNode2.setWorkflowNodeName("test4");
        workflowNode2.setWorkflowDepartmentType(300004);
        workflowNode2.setWorkflowDepartment(400011);
        workflowNode2.setWorkflowUser(500005);
        workflowNode2.setWorkflowRole(600005);
        workflowNodeList.add(workflowNode2);
        workflowTemplate.setWorkflowNodeList(workflowNodeList);
        TestResult testResult = getJsonTestResult("/workflow/updateWorkflowNodeList", workflowTemplate);
    }

    @Test
    public void findTemplateByIdTest() throws Exception {
        WorkflowTemplate workflowTemplate = new WorkflowTemplate();
        workflowTemplate.setWorkflowTemplateId(1);
        TestResult testResult = getJsonTestResult("/workflow/findWorkflowTemplate", workflowTemplate);
    }

    @Test
    public void workflowTemplatePageTest() throws Exception {
        WorkflowTemplateQueryParam workflowTemplateQueryParam = new WorkflowTemplateQueryParam();
        workflowTemplateQueryParam.setWorkflowTemplateId(6);
        TestResult testResult = getJsonTestResult("/workflow/pageWorkflowTemplate", workflowTemplateQueryParam);
    }
}
