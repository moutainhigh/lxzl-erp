package com.lxzl.erp.web.controller;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.user.UserQueryParam;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.domain.workflow.*;
import com.lxzl.erp.common.domain.workflow.pojo.*;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.user.UserService;
import com.lxzl.erp.dataaccess.dao.mysql.company.SubCompanyCityCoverMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerConsignInfoMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.workflow.*;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyCityCoverDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerConsignInfoDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.workflow.*;
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
        workflowParam.setWorkflowLinkNo("LXWF-500001-20181009-00195");
        workflowParam.setVerifyStatus(VerifyStatus.VERIFY_STATUS_PASS);
//        workflowParam.setVerifyStatus(VerifyStatus.VERIFY_STATUS_BACK);
//        workflowParam.setReturnType(1);
        workflowParam.setVerifyOpinion("通过");
//        workflowParam.setNextVerifyUser(500081);
//        List<Integer> list = new ArrayList<>();
//        list.add(1819);
//        workflowParam.setImgIdList(list);

        TestResult testResult = getJsonTestResult("/workflow/verifyWorkFlow", workflowParam);
//        Thread.sleep(30000);
        System.out.println(JSON.toJSONString(testResult));
    }

    @Test
    public void queryNextVerifyUsers() throws Exception {
        WorkflowLinkQueryParam workflowLinkQueryParam = new WorkflowLinkQueryParam();
        workflowLinkQueryParam.setWorkflowType(WorkflowType.WORKFLOW_TYPE_CHANGE);
        workflowLinkQueryParam.setWorkflowReferNo("LXREO20180921135842793");
        TestResult testResult = getJsonTestResult("/workflow/queryNextVerifyUsers", workflowLinkQueryParam);
        System.out.println(JSON.toJSONString(testResult));
    }

    @Test
    public void queryWorkflowLinkPage() throws Exception {
        WorkflowLinkQueryParam workflowLinkQueryParam = new WorkflowLinkQueryParam();
//        workflowLinkQueryParam.setPageNo(1);
//        workflowLinkQueryParam.setPageSize(15);
        workflowLinkQueryParam.setVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
//        workflowLinkQueryParam.setWorkflowType(WorkflowType.WORKFLOW_TYPE_PURCHASE);
//        workflowLinkQueryParam.setWorkflowReferNo("");
        workflowLinkQueryParam.setVerifyStatus(3);
        TestResult testResult = getJsonTestResult("/workflow/queryWorkflowLinkPage", workflowLinkQueryParam);
    }

    @Test
    public void queryWorkflowLinkDetail() throws Exception {
        WorkflowLinkQueryParam workflowLinkQueryParam = new WorkflowLinkQueryParam();

        workflowLinkQueryParam.setWorkflowLinkNo("LXWF-500001-20180412-00096");
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

            for(WorkflowLinkDetailDO workflowLinkDetailDO:workflowLinkDO.getWorkflowLinkDetailDOList()){
                Integer data = 19;
                if(data.equals(workflowLinkDetailDO.getWorkflowCurrentNodeId())){
                    CustomerDO customerDO = customerMapper.findByNo(workflowLinkDetailDO.getWorkflowReferNo());

                    List<CustomerConsignInfoDO> customerConsignInfoDOList = customerConsignInfoMapper.findByCustomerId(customerDO.getId());
                    Map<Integer, Integer> map = new HashMap<>();
                    String verifyUserGroupId = generateNoSupport.generateVerifyUserGroupId();
                    SubCompanyCityCoverDO subCompanyCityCoverDO;
                    if(CustomerType.CUSTOMER_TYPE_COMPANY.equals(customerDO.getCustomerType())){
                        customerDO = customerMapper.findCustomerCompanyByNo(workflowLinkDetailDO.getWorkflowReferNo());
                        //判断经营地址
                        subCompanyCityCoverDO = subCompanyCityCoverMapper.findByCityId(customerDO.getCustomerCompanyDO().getCity());
                        if (subCompanyCityCoverDO == null) {
                            subCompanyCityCoverDO = subCompanyCityCoverMapper.findByProvinceId(customerDO.getCustomerCompanyDO().getProvince());
                        }
                        map.put(subCompanyCityCoverDO.getSubCompanyId(), subCompanyCityCoverDO.getSubCompanyId());
                    }
                    //判断收货地址
                    for (CustomerConsignInfoDO customerConsignInfoDO : customerConsignInfoDOList) {
                        subCompanyCityCoverDO = subCompanyCityCoverMapper.findByCityId(customerConsignInfoDO.getCity());
                        if (subCompanyCityCoverDO == null) {
                            subCompanyCityCoverDO = subCompanyCityCoverMapper.findByProvinceId(customerConsignInfoDO.getProvince());
                        }
                        map.put(subCompanyCityCoverDO.getSubCompanyId(), subCompanyCityCoverDO.getSubCompanyId());
                    }

                    for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                        UserQueryParam userQueryParam = new UserQueryParam();
                        userQueryParam.setRoleType(100004);
                        userQueryParam.setSubCompanyId(entry.getValue());
                        ServiceResult<String, List<User>> userResult = userService.getUserListByParam(userQueryParam);
                        if (ErrorCode.SUCCESS.equals(userResult.getErrorCode())) {
                            for (User user : userResult.getResult()) {
                                WorkflowVerifyUserGroupDO workflowVerifyUserGroupDO = new WorkflowVerifyUserGroupDO();
                                workflowVerifyUserGroupDO.setVerifyUserGroupId(verifyUserGroupId);
                                workflowVerifyUserGroupDO.setVerifyType(VerifyType.VERIFY_TYPE_THE_SAME_GROUP_ALL_PASS);
                                workflowVerifyUserGroupDO.setVerifyUser(user.getUserId());
                                workflowVerifyUserGroupDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
                                workflowVerifyUserGroupDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                                workflowVerifyUserGroupDO.setCreateUser(workflowLinkDetailDO.getCreateUser());
                                workflowVerifyUserGroupDO.setCreateTime(now);
                                workflowVerifyUserGroupMapper.save(workflowVerifyUserGroupDO);
                            }
                        }
                    }
                    workflowLinkDetailDO.setVerifyUserGroupId(verifyUserGroupId);
                    workflowLinkDetailMapper.update(workflowLinkDetailDO);
                    System.out.println(workflowLinkDetailDO.getId());
                }else{
                    String groupId = generateNoSupport.generateVerifyUserGroupId();
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
            WorkflowLinkDetailDO lastWorkflowLinkDetailDO = workflowLinkDO.getWorkflowLinkDetailDOList().get(0);
            workflowLinkDO.setVerifyUserGroupId(lastWorkflowLinkDetailDO.getVerifyUserGroupId());
            workflowLinkMapper.update(workflowLinkDO);

        }
    }
    @Test
    public void queryWorkflowLinkDetailByType() throws Exception {
        WorkflowLinkQueryParam workflowLinkQueryParam = new WorkflowLinkQueryParam();
        workflowLinkQueryParam.setWorkflowType(16);
        workflowLinkQueryParam.setWorkflowReferNo("LXCC-1000-20180327-00815");
        TestResult testResult = getJsonTestResult("/workflow/queryWorkflowLinkDetailByType", workflowLinkQueryParam);
    }
    @Test
    public void test123() throws Exception {

        TestResult testResult = getJsonTestResult("/workflow/workflowImportData", null);
    }

    @Test
    public void commit() throws Exception {
        WorkflowLinkQueryParam workflowLinkQueryParam = new WorkflowLinkQueryParam();
        workflowLinkQueryParam.setWorkflowType(WorkflowType.WORKFLOW_TYPE_MALL_ORDER);
        workflowLinkQueryParam.setWorkflowReferNo("LXO-20180529-1000-00056");
        TestResult testResult = getJsonTestResult("/workflow/commitWorkFlow", workflowLinkQueryParam);
    }

    @Autowired
    private WorkflowLinkMapper workflowLinkMapper;
    @Autowired
    private GenerateNoSupport generateNoSupport;
    @Autowired
    private WorkflowVerifyUserGroupMapper workflowVerifyUserGroupMapper;
    @Autowired
    private WorkflowLinkDetailMapper workflowLinkDetailMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private CustomerConsignInfoMapper customerConsignInfoMapper;
    @Autowired
    private SubCompanyCityCoverMapper subCompanyCityCoverMapper;
    @Autowired
    private UserService userService;
}
