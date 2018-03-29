package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.VerifyStatus;
import com.lxzl.erp.common.constant.VerifyType;
import com.lxzl.erp.common.constant.WorkflowType;
import com.lxzl.erp.common.domain.workflow.VerifyWorkflowParam;
import com.lxzl.erp.common.domain.workflow.WorkflowLinkQueryParam;
import com.lxzl.erp.common.domain.workflow.WorkflowTemplateQueryParam;
import com.lxzl.erp.common.domain.workflow.pojo.WorkflowNode;
import com.lxzl.erp.common.domain.workflow.pojo.WorkflowTemplate;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.dataaccess.dao.mysql.workflow.WorkflowLinkDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.workflow.WorkflowLinkMapper;
import com.lxzl.erp.dataaccess.dao.mysql.workflow.WorkflowVerifyUserGroupMapper;
import com.lxzl.erp.dataaccess.domain.workflow.WorkflowLinkDO;
import com.lxzl.erp.dataaccess.domain.workflow.WorkflowLinkDetailDO;
import com.lxzl.erp.dataaccess.domain.workflow.WorkflowVerifyUserGroupDO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

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
        workflowParam.setWorkflowLinkNo("LXWF-500355-20180329-00784");
//        workflowParam.setVerifyStatus(VerifyStatus.VERIFY_STATUS_PASS);
        workflowParam.setVerifyStatus(VerifyStatus.VERIFY_STATUS_BACK);
        workflowParam.setReturnType(1);
//        workflowParam.setNextVerifyUser(500016);
//        List<Integer> list = new ArrayList<>();
//        list.add(1819);
//        workflowParam.setImgIdList(list);

        TestResult testResult = getJsonTestResult("/workflow/verifyWorkFlow", workflowParam);
    }

    @Test
    public void queryNextVerifyUsers() throws Exception {
        WorkflowLinkQueryParam workflowLinkQueryParam = new WorkflowLinkQueryParam();
        workflowLinkQueryParam.setWorkflowType(WorkflowType.WORKFLOW_TYPE_CUSTOMER);
        workflowLinkQueryParam.setWorkflowReferNo("LXCC-027-20180328-00818");
        TestResult testResult = getJsonTestResult("/workflow/queryNextVerifyUsers", workflowLinkQueryParam);
    }

    @Test
    public void queryWorkflowLinkPage() throws Exception {
        WorkflowLinkQueryParam workflowLinkQueryParam = new WorkflowLinkQueryParam();
//        workflowLinkQueryParam.setWorkflowType(WorkflowType.WORKFLOW_TYPE_PURCHASE);
//        workflowLinkQueryParam.setWorkflowReferNo("");
        TestResult testResult = getJsonTestResult("/workflow/queryWorkflowLinkPage", workflowLinkQueryParam);
    }

    @Test
    public void queryWorkflowLinkDetail() throws Exception {
        WorkflowLinkQueryParam workflowLinkQueryParam = new WorkflowLinkQueryParam();
        workflowLinkQueryParam.setWorkflowLinkNo("LXWF-500324-20180328-00762");
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

    @Test
    public void workflowImportData() throws Exception {
        Date now = new Date();
        WorkflowLinkQueryParam workflowLinkQueryParam = new WorkflowLinkQueryParam();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("start", 0);
        paramMap.put("pageSize", Integer.MAX_VALUE);
        paramMap.put("workflowLinkQueryParam", workflowLinkQueryParam);
        List<WorkflowLinkDO> workflowLinkDOList = workflowLinkMapper.listPage(paramMap);

        for(WorkflowLinkDO workflowLinkDO:workflowLinkDOList){
            if(workflowLinkDO.getVerifyUserGroupId() == null){
                for(WorkflowLinkDetailDO workflowLinkDetailDO:workflowLinkDO.getWorkflowLinkDetailDOList()){
                    if(workflowLinkDetailDO.getVerifyUserGroupId() == null){
                        Integer groupId = generateNoSupport.generateVerifyUserGroupId(workflowLinkDetailDO.getCreateTime());
                        WorkflowVerifyUserGroupDO workflowVerifyUserGroupDO = new WorkflowVerifyUserGroupDO();
                        workflowVerifyUserGroupDO.setVerifyUserGroupId(groupId);
                        workflowVerifyUserGroupDO.setVerifyType(VerifyType.VERIFY_TYPE_THIS_IS_PASS);
                        workflowVerifyUserGroupDO.setVerifyUser(workflowLinkDetailDO.getVerifyUser());
                        workflowVerifyUserGroupDO.setVerifyTime(workflowLinkDetailDO.getVerifyTime());
                        workflowVerifyUserGroupDO.setVerifyStatus(workflowLinkDetailDO.getVerifyStatus());
                        workflowVerifyUserGroupDO.setVerifyOpinion(workflowLinkDetailDO.getVerifyOpinion());
                        workflowVerifyUserGroupDO.setDataStatus(workflowLinkDetailDO.getDataStatus());
                        workflowVerifyUserGroupDO.setCreateTime(now);
                        workflowVerifyUserGroupDO.setUpdateTime(now);
                        workflowVerifyUserGroupDO.setCreateUser(workflowLinkDetailDO.getCreateUser());
                        workflowVerifyUserGroupMapper.save(workflowVerifyUserGroupDO);
                        System.out.println(workflowVerifyUserGroupDO.getId());
                        workflowLinkDetailDO.setVerifyUserGroupId(groupId);
                        workflowLinkDetailMapper.update(workflowLinkDetailDO);
                        System.out.println(workflowLinkDetailDO.getId());
                    }
                }
            }
        }
    }
    @Autowired
    private WorkflowLinkMapper workflowLinkMapper;
    @Autowired
    private GenerateNoSupport generateNoSupport;
    @Autowired
    private WorkflowVerifyUserGroupMapper workflowVerifyUserGroupMapper;
    @Autowired
    private WorkflowLinkDetailMapper workflowLinkDetailMapper;
}
