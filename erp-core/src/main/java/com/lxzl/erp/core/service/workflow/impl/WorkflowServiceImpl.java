package com.lxzl.erp.core.service.workflow.impl;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.user.UserQueryParam;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.domain.workflow.WorkflowLinkQueryParam;
import com.lxzl.erp.common.domain.workflow.WorkflowTemplateQueryParam;
import com.lxzl.erp.common.domain.workflow.pojo.*;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.VerifyReceiver;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.message.MessageService;
import com.lxzl.erp.core.service.permission.PermissionSupport;
import com.lxzl.erp.core.service.user.UserService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.workflow.WorkFlowManager;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.core.service.workflow.support.WorkflowSupport;
import com.lxzl.erp.dataaccess.dao.mysql.company.*;
import com.lxzl.erp.dataaccess.dao.mysql.customer.*;
import com.lxzl.erp.dataaccess.dao.mysql.system.DataDictionaryMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.RoleMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserMapper;
import com.lxzl.erp.dataaccess.dao.mysql.workflow.*;
import com.lxzl.erp.dataaccess.domain.company.DepartmentDO;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyCityCoverDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerConsignInfoDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.system.DataDictionaryDO;
import com.lxzl.erp.dataaccess.domain.user.RoleDO;
import com.lxzl.erp.dataaccess.domain.user.UserDO;
import com.lxzl.erp.dataaccess.domain.workflow.*;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import com.lxzl.se.dataaccess.mysql.source.interceptor.SqlLogInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-04 16:12
 */
@Service("workflowService")
public class WorkflowServiceImpl implements WorkflowService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> commitWorkFlow(Integer workflowType, String workflowReferNo, Integer verifyUser, String verifyMatters, String commitRemark, List<Integer> imgIdList, String orderRemark) {
        ServiceResult<String, String> result = new ServiceResult<>();
        Date currentTime = new Date();
        WorkflowTemplateDO workflowTemplateDO = workflowTemplateMapper.findByWorkflowType(workflowType);
        if (workflowTemplateDO == null) {
            result.setErrorCode(ErrorCode.WORKFLOW_HAVE_NO_CONFIG);
            return result;
        }
        List<WorkflowNodeDO> workflowNodeDOList = workflowTemplateDO.getWorkflowNodeDOList();
        if (workflowNodeDOList == null || workflowNodeDOList.isEmpty()) {
            result.setErrorCode(ErrorCode.WORKFLOW_TEMPLATE_HAVE_NO_NODE);
            return result;
        }
        String workflowLinkNo;
        if (WorkflowType.WORKFLOW_TYPE_CUSTOMER.equals(workflowType)) {
            ServiceResult<String, String> customerCommitWorkFlow = customerCommitWorkFlow(workflowType, workflowReferNo, verifyUser, verifyMatters, commitRemark, imgIdList, orderRemark, currentTime, workflowNodeDOList, workflowTemplateDO);
            if (!ErrorCode.SUCCESS.equals(customerCommitWorkFlow.getErrorCode())) {
                result.setErrorCode(customerCommitWorkFlow.getErrorCode());
                return result;
            }
            workflowLinkNo = customerCommitWorkFlow.getResult();
        } else {
            Integer subCompanyId = workflowSupport.getSubCompanyId(workflowType, workflowReferNo, workflowNodeDOList.get(0));
            if (CommonConstant.ELECTRIC_SALE_COMPANY_ID.equals(subCompanyId)) {
                subCompanyId = CommonConstant.HEAD_COMPANY_ID;
            }
            if (CommonConstant.CHANNEL_CUSTOMER_COMPANY_ID.equals(subCompanyId) && WorkflowType.WORKFLOW_TYPE_ORDER_INFO.equals(workflowType)) {
                subCompanyId = CommonConstant.HEAD_COMPANY_ID;
            }

            if (WorkflowType.WORKFLOW_TYPE_CHANNEL_CUSTOMER.equals(workflowType)) {
                CustomerDO customerDO = customerMapper.findByNo(workflowReferNo);
                WorkflowLinkDO workflowLinkDO = workflowLinkMapper.findByWorkflowTypeAndReferNo(workflowType, workflowReferNo);
                List<CustomerConsignInfoDO> customerConsignInfoDOList = customerConsignInfoMapper.findVerifyStatusByCustomerId(customerDO.getId());
                if (customerConsignInfoDOList.size() >= 0 && (workflowLinkDO != null && workflowLinkDO.getWorkflowLinkDetailDOList().get(0).getWorkflowStep() > 1)) {
                    return channelCustomerCommitWorkFlow(workflowLinkDO, verifyUser);
                }
            }
            WorkflowNodeDO thisWorkflowNodeDO = workflowNodeDOList.get(0);
            //检查审核人以及类型
            List<User> verifyUserList = workflowSupport.checkVerifyUserType(thisWorkflowNodeDO, subCompanyId, verifyUser);

            WorkflowLinkDO workflowLinkDO = workflowLinkMapper.findByWorkflowTypeAndReferNo(workflowType, workflowReferNo);
            if (workflowLinkDO == null) {
                workflowLinkNo = generateWorkflowLink(workflowTemplateDO, workflowReferNo, commitRemark, verifyUser, verifyMatters, imgIdList, currentTime, orderRemark, verifyUserList);
                workflowLinkDO = workflowLinkMapper.findByNo(workflowLinkNo);
            } else {
                String errorCode = continueWorkflowLink(workflowLinkDO, commitRemark, verifyUser, verifyMatters, imgIdList, currentTime, orderRemark, verifyUserList);
                if (!ErrorCode.SUCCESS.equals(errorCode)) {
                    result.setErrorCode(errorCode);
                    return result;
                }
                workflowLinkNo = workflowLinkDO.getWorkflowLinkNo();
            }
            //发送信息
            if (VerifyType.VERIFY_TYPE_ALL_USER_THIS_IS_PASS.equals(thisWorkflowNodeDO.getVerifyType()) || VerifyType.VERIFY_TYPE_THE_SAME_GROUP_ALL_PASS.equals(thisWorkflowNodeDO.getVerifyType())) {
                for (User user : verifyUserList) {
                    messageService.superSendMessage(MessageContant.WORKFLOW_COMMIT_TITLE, String.format(MessageContant.WORKFLOW_COMMIT_CONTENT, WorkflowType.getWorkflowTypeDesc(workflowLinkDO.getWorkflowType()), workflowLinkDO.getWorkflowLinkNo()), user.getUserId());
                }
            } else {
                messageService.superSendMessage(MessageContant.WORKFLOW_COMMIT_TITLE, String.format(MessageContant.WORKFLOW_COMMIT_CONTENT, WorkflowType.getWorkflowTypeDesc(workflowLinkDO.getWorkflowType()), workflowLinkDO.getWorkflowLinkNo()), verifyUser);
            }
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(workflowLinkNo);
        // 提交工作流到钉钉上
//        dingdingService.applyApprovingWorkflowToDingding(workflowLinkNo);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> cancelWorkFlow(Integer workflowType, String workflowReferNo) {
        ServiceResult<String, String> result = new ServiceResult<>();
        Date currentTime = new Date();
        String userId = userSupport.getCurrentUser().getUserId().toString();
        WorkflowLinkDO workflowLinkDO = workflowLinkMapper.findByWorkflowTypeAndReferNo(workflowType, workflowReferNo);
        if (workflowLinkDO == null) {
            result.setErrorCode(ErrorCode.WORKFLOW_LINK_NOT_EXISTS);
            return result;
        }
        if (VerifyStatus.VERIFY_STATUS_PASS.equals(workflowLinkDO.getCurrentVerifyStatus()) || VerifyStatus.VERIFY_STATUS_BACK.equals(workflowLinkDO.getCurrentVerifyStatus())) {
            result.setErrorCode(ErrorCode.VERIFY_STATUS_ERROR);
            return result;
        }
        //取消审核组人员在审核中的变成取消
        List<WorkflowVerifyUserGroupDO> workflowVerifyUserGroupDOList = workflowVerifyUserGroupMapper.findByVerifyUserGroupId(workflowLinkDO.getVerifyUserGroupId());
        if (CollectionUtil.isNotEmpty(workflowVerifyUserGroupDOList)) {
            List<WorkflowVerifyUserGroupDO> updateWorkflowVerifyUserGroupDOList = new ArrayList<>();
            for (WorkflowVerifyUserGroupDO workflowVerifyUserGroupDO : workflowVerifyUserGroupDOList) {
                if (VerifyStatus.VERIFY_STATUS_COMMIT.equals(workflowVerifyUserGroupDO.getVerifyStatus())) {
                    workflowVerifyUserGroupDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_CANCEL);
                    workflowVerifyUserGroupDO.setUpdateUser(userId);
                    workflowVerifyUserGroupDO.setUpdateTime(currentTime);
                    updateWorkflowVerifyUserGroupDOList.add(workflowVerifyUserGroupDO);
                }
            }
            if (CollectionUtil.isNotEmpty(updateWorkflowVerifyUserGroupDOList)) {
                SqlLogInterceptor.setExecuteSql("skip print  workflowVerifyUserGroupMapper.updateBatchVerifyUserGroup  sql ......");
                workflowVerifyUserGroupMapper.updateBatchVerifyUserGroup(updateWorkflowVerifyUserGroupDOList);
            }
        }
        //新增一条审核组当前取消人的审核组人员
        String verifyUserGroupId = generateNoSupport.generateVerifyUserGroupId();
        WorkflowVerifyUserGroupDO workflowVerifyUserGroupDO = new WorkflowVerifyUserGroupDO();
        workflowVerifyUserGroupDO.setVerifyUserGroupId(verifyUserGroupId);
        workflowVerifyUserGroupDO.setVerifyType(VerifyType.VERIFY_TYPE_THE_SAME_GROUP_ALL_PASS);
        workflowVerifyUserGroupDO.setVerifyUser(userSupport.getCurrentUserId());
        workflowVerifyUserGroupDO.setVerifyTime(currentTime);
        workflowVerifyUserGroupDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_CANCEL);
        workflowVerifyUserGroupDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        workflowVerifyUserGroupDO.setCreateTime(currentTime);
        workflowVerifyUserGroupDO.setCreateUser(userId);
        workflowVerifyUserGroupMapper.save(workflowVerifyUserGroupDO);
        //修改当前工作流状态
        workflowLinkDO.setCurrentVerifyStatus(VerifyStatus.VERIFY_STATUS_CANCEL);
        workflowLinkDO.setCurrentVerifyUser(userSupport.getCurrentUserId());
        workflowLinkDO.setVerifyUserGroupId(verifyUserGroupId);
        workflowLinkMapper.update(workflowLinkDO);
        WorkflowLinkDetailDO workflowLinkDetailDO = new WorkflowLinkDetailDO();
        //新增工作流详情取消节点
        List<WorkflowLinkDetailDO> workflowLinkDetailDOList = workflowLinkDO.getWorkflowLinkDetailDOList();
        if (workflowLinkDetailDOList != null && workflowLinkDetailDOList.size() > 1) {
            WorkflowLinkDetailDO previousWorkflowLinkDetailDO = workflowLinkDetailDOList.get(1);
            workflowLinkDetailDO.setWorkflowPreviousNodeId(previousWorkflowLinkDetailDO.getWorkflowCurrentNodeId());
            workflowLinkDetailDO.setWorkflowStep(previousWorkflowLinkDetailDO.getWorkflowStep() + 1);
        }
        workflowLinkDetailDO.setVerifyUserGroupId(verifyUserGroupId);
        workflowLinkDetailDO.setVerifyUser(userSupport.getCurrentUserId());
        workflowLinkDetailDO.setWorkflowLinkId(workflowLinkDO.getId());
        workflowLinkDetailDO.setWorkflowReferNo(workflowReferNo);
        workflowLinkDetailDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_CANCEL);
        workflowLinkDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        workflowLinkDetailDO.setUpdateUser(userId);
        workflowLinkDetailDO.setCreateUser(userId);
        workflowLinkDetailDO.setUpdateTime(currentTime);
        workflowLinkDetailDO.setCreateTime(currentTime);
        workflowLinkDetailMapper.save(workflowLinkDetailDO);

        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> rejectPassWorkFlow(Integer workflowType, String workflowReferNo, String commitRemark) {
        ServiceResult<String, String> result = new ServiceResult<>();
        Date currentTime = new Date();
        Integer loginUser = userSupport.getCurrentUser().getUserId();

        WorkflowLinkDO workflowLinkDO = workflowLinkMapper.findByWorkflowTypeAndReferNo(workflowType, workflowReferNo);

        WorkflowTemplateDO workflowTemplateDO = workflowTemplateMapper.findByWorkflowType(workflowType);
        if (workflowTemplateDO == null) {
            result.setErrorCode(ErrorCode.WORKFLOW_TEMPLATE_NOT_EXISTS);
            return result;
        }
        if (workflowLinkDO != null) {
            //保存单条审核组成员并返回审核组id
            String userGroupId = workflowSupport.saveVerifyGroupAndGetUserGroupId(loginUser, currentTime, commitRemark);

            workflowLinkDO.setCurrentVerifyStatus(VerifyStatus.VERIFY_STATUS_BACK);
            workflowLinkDO.setVerifyUserGroupId(userGroupId);
            workflowLinkDO.setUpdateUser(loginUser.toString());
            workflowLinkDO.setUpdateTime(currentTime);
            workflowLinkMapper.update(workflowLinkDO);
            //保存工作流详情记录
            workflowSupport.saveWorkflowLinkDetail(workflowLinkDO.getId(), workflowReferNo, (workflowTemplateDO.getWorkflowNodeDOList().size() + 1), loginUser, userGroupId, currentTime, commitRemark);
        } else {
            //保存单条审核组成员并返回审核组id
            String userGroupId = workflowSupport.saveVerifyGroupAndGetUserGroupId(loginUser, currentTime, commitRemark);

            WorkflowLinkDO newWorkflowLinkDO = new WorkflowLinkDO();
            newWorkflowLinkDO.setWorkflowLinkNo(generateNoSupport.generateWorkflowLinkNo(currentTime, loginUser));
            newWorkflowLinkDO.setWorkflowType(workflowTemplateDO.getWorkflowType());
            newWorkflowLinkDO.setWorkflowTemplateId(workflowTemplateDO.getId());
            newWorkflowLinkDO.setWorkflowReferNo(workflowReferNo);
            newWorkflowLinkDO.setWorkflowStep(3);
            newWorkflowLinkDO.setWorkflowLastStep(3);
            newWorkflowLinkDO.setWorkflowCurrentNodeId(workflowTemplateDO.getWorkflowNodeDOList().get(1).getId());
            newWorkflowLinkDO.setCommitUser(loginUser);
            newWorkflowLinkDO.setCurrentVerifyUser(loginUser);
            newWorkflowLinkDO.setVerifyUserGroupId(userGroupId);
            newWorkflowLinkDO.setCurrentVerifyStatus(VerifyStatus.VERIFY_STATUS_BACK);
            newWorkflowLinkDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            newWorkflowLinkDO.setCreateUser(loginUser.toString());
            newWorkflowLinkDO.setCreateTime(currentTime);
            newWorkflowLinkDO.setRemark(commitRemark);
            workflowLinkMapper.save(newWorkflowLinkDO);
            //保存工作流详情记录
            workflowSupport.saveWorkflowLinkDetail(newWorkflowLinkDO.getId(), workflowReferNo, (workflowTemplateDO.getWorkflowNodeDOList().size() + 1), loginUser, userGroupId, currentTime, commitRemark);
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, String> workflowImportData() {
        ServiceResult<String, String> result = new ServiceResult<>();
//        超级管理员权限控制
        if (!userSupport.isSuperUser()) {
            result.setErrorCode(ErrorCode.USER_ROLE_IS_NOT_SUPER_ADMIN);
            return result;
        }

        Date now = new Date();
        WorkflowLinkQueryParam workflowLinkQueryParam = new WorkflowLinkQueryParam();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("start", 0);
        paramMap.put("pageSize", Integer.MAX_VALUE);
        paramMap.put("workflowLinkQueryParam", workflowLinkQueryParam);
        List<WorkflowLinkDO> workflowLinkDOList = workflowLinkMapper.listPage(paramMap);

        for (WorkflowLinkDO workflowLinkDO : workflowLinkDOList) {

            for (WorkflowLinkDetailDO workflowLinkDetailDO : workflowLinkDO.getWorkflowLinkDetailDOList()) {
                Integer data = 19;
                if (data.equals(workflowLinkDetailDO.getWorkflowCurrentNodeId())) {
                    CustomerDO customerDO = customerMapper.findByNo(workflowLinkDetailDO.getWorkflowReferNo());

                    List<CustomerConsignInfoDO> customerConsignInfoDOList = customerConsignInfoMapper.findByCustomerId(customerDO.getId());
                    Map<Integer, Integer> map = new HashMap<>();
                    String verifyUserGroupId = generateNoSupport.generateVerifyUserGroupId();
                    SubCompanyCityCoverDO subCompanyCityCoverDO;
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
                                workflowVerifyUserGroupDO.setVerifyOpinion(workflowLinkDetailDO.getVerifyOpinion());
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
                } else {
                    String groupId = generateNoSupport.generateVerifyUserGroupId();
                    WorkflowVerifyUserGroupDO workflowVerifyUserGroupDO = new WorkflowVerifyUserGroupDO();
                    workflowVerifyUserGroupDO.setVerifyUserGroupId(groupId);
                    workflowVerifyUserGroupDO.setVerifyType(VerifyType.VERIFY_TYPE_THIS_IS_PASS);
                    workflowVerifyUserGroupDO.setVerifyUser(workflowLinkDetailDO.getVerifyUser());
                    workflowVerifyUserGroupDO.setVerifyTime(workflowLinkDetailDO.getVerifyTime());
                    workflowVerifyUserGroupDO.setVerifyStatus(workflowLinkDetailDO.getVerifyStatus());
                    workflowVerifyUserGroupDO.setVerifyOpinion(workflowLinkDetailDO.getVerifyOpinion());
                    workflowVerifyUserGroupDO.setRemark(workflowLinkDetailDO.getRemark());
                    workflowVerifyUserGroupDO.setDataStatus(workflowLinkDetailDO.getDataStatus());
                    workflowVerifyUserGroupDO.setCreateTime(now);
                    workflowVerifyUserGroupDO.setUpdateTime(now);
                    workflowVerifyUserGroupDO.setCreateUser(workflowLinkDetailDO.getCreateUser());
                    workflowVerifyUserGroupMapper.save(workflowVerifyUserGroupDO);

                    workflowLinkDetailDO.setVerifyUserGroupId(groupId);
                    workflowLinkDetailMapper.update(workflowLinkDetailDO);
                }
            }
            WorkflowLinkDetailDO lastWorkflowLinkDetailDO = workflowLinkDO.getWorkflowLinkDetailDOList().get(0);
            workflowLinkDO.setVerifyUserGroupId(lastWorkflowLinkDetailDO.getVerifyUserGroupId());
            workflowLinkMapper.update(workflowLinkDO);

        }
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, List<User>> getNextVerifyUsers(Integer workflowType, String workflowReferNo) {
        ServiceResult<String, List<User>> result = new ServiceResult<>();
        if (workflowType == null || StringUtil.isBlank(workflowReferNo)) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }
        if (WorkflowType.WORKFLOW_TYPE_CUSTOMER.equals(workflowType)) {
            CustomerDO customerDO = customerMapper.findByNo(workflowReferNo);
            if (customerDO == null) {
                result.setErrorCode(ErrorCode.WORKFLOW_LINK_NOT_EXISTS);
            }
            UserDO userDO = userMapper.findByUserId(customerDO.getOwner());
            if (userSupport.isChannelSubCompany(ConverterUtil.convert(userDO, User.class))) {
                workflowType = WorkflowType.WORKFLOW_TYPE_CHANNEL_CUSTOMER;
            }
        }
        WorkflowLinkDO workflowLinkDO = workflowLinkMapper.findByWorkflowTypeAndReferNo(workflowType, workflowReferNo);
        WorkflowNodeDO workflowNodeDO = null;
        WorkflowTemplateDO workflowTemplateDO = workflowTemplateMapper.findByWorkflowType(workflowType);
        if (workflowLinkDO == null) {
            if (workflowTemplateDO == null || CollectionUtil.isEmpty(workflowTemplateDO.getWorkflowNodeDOList())) {
                result.setErrorCode(ErrorCode.WORKFLOW_LINK_NOT_EXISTS);
                return result;
            }
            if (WorkflowType.WORKFLOW_TYPE_CUSTOMER.equals(workflowType)) {
                result.setErrorCode(ErrorCode.SUCCESS);
                return result;
            }
            workflowNodeDO = workflowTemplateDO.getWorkflowNodeDOList().get(0);
        } else {
            if (VerifyStatus.VERIFY_STATUS_PASS.equals(workflowLinkDO.getCurrentVerifyStatus())) {
                result.setErrorCode(ErrorCode.SUCCESS);
                return result;
            }
            List<WorkflowLinkDetailDO> workflowLinkDetailDOList = workflowLinkDO.getWorkflowLinkDetailDOList();
            if (workflowLinkDetailDOList == null || workflowLinkDetailDOList.isEmpty()) {
                result.setErrorCode(ErrorCode.WORKFLOW_LINK_HAVE_NO_DETAIL);
                return result;
            }

            WorkflowLinkDetailDO lastWorkflowLinkDetailDO = workflowLinkDetailDOList.get(0);
            if (VerifyStatus.VERIFY_STATUS_PASS.equals(lastWorkflowLinkDetailDO.getVerifyStatus())) {
                result.setErrorCode(ErrorCode.SUCCESS);
                return result;
            } else if (VerifyStatus.VERIFY_STATUS_BACK.equals(lastWorkflowLinkDetailDO.getVerifyStatus())) {

                if (WorkflowType.WORKFLOW_TYPE_CUSTOMER.equals(workflowLinkDO.getWorkflowType()) ||
                        (WorkflowType.WORKFLOW_TYPE_CHANNEL_CUSTOMER.equals(workflowLinkDO.getWorkflowType()) && workflowLinkDO.getWorkflowStep() == 2)) {
                    CustomerDO customerDO = customerMapper.findByNo(workflowReferNo);
                    if (customerDO == null) {
                        result.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
                        return result;
                    }
                    if (CustomerType.CUSTOMER_TYPE_COMPANY.equals(customerDO.getCustomerType())) {
                        customerDO = customerMapper.findCustomerCompanyByNo(customerDO.getCustomerNo());
                        if (CustomerConsignVerifyStatus.VERIFY_STATUS_PENDING.equals(customerDO.getCustomerCompanyDO().getAddressVerifyStatus())
                                || CustomerConsignVerifyStatus.VERIFY_STATUS_BACK.equals(customerDO.getCustomerCompanyDO().getAddressVerifyStatus())) {
                            result.setErrorCode(ErrorCode.SUCCESS);
                            return result;
                        }
                    }
                    List<CustomerConsignInfoDO> customerConsignInfoDOList = customerConsignInfoMapper.findVerifyStatusByCustomerId(customerDO.getId());
                    if (customerConsignInfoDOList.size() == 0) {
                        if (WorkflowType.WORKFLOW_TYPE_CUSTOMER.equals(workflowLinkDO.getWorkflowType()) || WorkflowType.WORKFLOW_TYPE_CUSTOMER_CONSIGN.equals(workflowLinkDO.getWorkflowType())) {
                            workflowNodeDO = workflowTemplateDO.getWorkflowNodeDOList().get(1);
                        } else if (WorkflowType.WORKFLOW_TYPE_CHANNEL_CUSTOMER.equals(workflowLinkDO.getWorkflowType())) {
                            if (lastWorkflowLinkDetailDO.getWorkflowStep() == 2 || lastWorkflowLinkDetailDO.getWorkflowStep() == 3) {
                                workflowNodeDO = workflowTemplateDO.getWorkflowNodeDOList().get(1);
                            } else {
                                result.setErrorCode(ErrorCode.SUCCESS);
                                return result;
                            }
                        }
                    } else {
                        result.setErrorCode(ErrorCode.SUCCESS);
                        return result;
                    }
                } else {
                    workflowNodeDO = workflowTemplateDO.getWorkflowNodeDOList().get(0);
                }
            } else {
                List<WorkflowVerifyUserGroupDO> workflowVerifyUserGroupDOList = workflowVerifyUserGroupMapper.findByVerifyUserGroupId(lastWorkflowLinkDetailDO.getVerifyUserGroupId());
                if (CollectionUtil.isEmpty(workflowVerifyUserGroupDOList)) {
                    result.setErrorCode(ErrorCode.WORKFLOW_VERIFY_USER_GROUP_NOT_EXISTS);
                    return result;
                }
                if (VerifyType.VERIFY_TYPE_THE_SAME_GROUP_ALL_PASS.equals(workflowVerifyUserGroupDOList.get(0).getVerifyType())) {
                    Integer count = 0;
                    for (WorkflowVerifyUserGroupDO workflowVerifyUserGroupDO : workflowVerifyUserGroupDOList) {
                        if (VerifyStatus.VERIFY_STATUS_PASS.equals(workflowVerifyUserGroupDO.getVerifyStatus())) {
                            count++;
                        }
                    }
                    if (count < workflowVerifyUserGroupDOList.size() - 1) {
                        result.setErrorCode(ErrorCode.SUCCESS);
                        return result;
                    }
                }
                workflowNodeDO = workflowNodeMapper.findById(lastWorkflowLinkDetailDO.getWorkflowNextNodeId());
            }
        }
        Integer subCompanyId = workflowSupport.getSubCompanyId(workflowType, workflowReferNo, workflowNodeDO);
        if (CommonConstant.ELECTRIC_SALE_COMPANY_ID.equals(subCompanyId)) {
            subCompanyId = CommonConstant.HEAD_COMPANY_ID;
        }
        if (CommonConstant.CHANNEL_CUSTOMER_COMPANY_ID.equals(subCompanyId) && WorkflowType.WORKFLOW_TYPE_ORDER_INFO.equals(workflowType)) {
            subCompanyId = CommonConstant.HEAD_COMPANY_ID;
        }
        //为了不影响之前审核逻辑，这里copy了部分逻辑
        if (workflowLinkDO != null && (WorkflowType.WORKFLOW_TYPE_CUSTOMER.equals(workflowLinkDO.getWorkflowType()) ||
                (WorkflowType.WORKFLOW_TYPE_CHANNEL_CUSTOMER.equals(workflowLinkDO.getWorkflowType()) && workflowLinkDO.getWorkflowLinkDetailDOList().get(0).getWorkflowStep() > 1))) {
            CustomerDO customerDO = customerMapper.findByNo(workflowReferNo);
            List<CustomerConsignInfoDO> customerConsignInfoDOList = customerConsignInfoMapper.findVerifyStatusByCustomerId(customerDO.getId());
            if (customerConsignInfoDOList.size() == 0) {
                workflowNodeDO = workflowTemplateDO.getWorkflowNodeDOList().get(workflowTemplateDO.getWorkflowNodeDOList().size() - 1);
            } else {
                result.setErrorCode(ErrorCode.SUCCESS);
                return result;
            }
        }
        if (VerifyType.VERIFY_TYPE_ALL_USER_THIS_IS_PASS.equals(workflowNodeDO.getVerifyType()) || VerifyType.VERIFY_TYPE_THE_SAME_GROUP_ALL_PASS.equals(workflowNodeDO.getVerifyType())) {
            result.setErrorCode(ErrorCode.SUCCESS);
            return result;
        }
        List<User> userList = workflowSupport.getUserListByNode(workflowNodeDO, subCompanyId);
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(userList);
        return result;
    }

    @Override
    public ServiceResult<String, WorkflowLink> getWorkflowLink(String workflowLinkNo) {
        ServiceResult<String, WorkflowLink> result = new ServiceResult<>();
        if (workflowLinkNo == null) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_ENOUGH);
            return result;
        }
        WorkflowLinkDO workflowLinkDO = workflowLinkMapper.findByNo(workflowLinkNo);
        if (workflowLinkDO == null) {
            result.setErrorCode(ErrorCode.WORKFLOW_LINK_NOT_EXISTS);
            return result;
        }
        result.setResult(ConverterUtil.convert(workflowSupport.getVerifyUserGroupAndImg(workflowLinkDO), WorkflowLink.class));
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, WorkflowLink> getWorkflowLink(Integer workflowType, String workflowReferNo) {
        ServiceResult<String, WorkflowLink> result = new ServiceResult<>();
        if (workflowType == null || workflowReferNo == null) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_ENOUGH);
            return result;
        }
        //todo
        if (WorkflowType.WORKFLOW_TYPE_CUSTOMER.equals(workflowType)) {
            CustomerDO customerDO = customerMapper.findByNo(workflowReferNo);
            if (customerDO == null) {
                result.setErrorCode(ErrorCode.CUSTOMER_STATUS_IS_NOT_PASS);
                return result;
            }
            ServiceResult<String, User> userResult = userService.getUserById(customerDO.getOwner());
            if (userSupport.isChannelSubCompany(userResult.getResult())) {
                workflowType = WorkflowType.WORKFLOW_TYPE_CHANNEL_CUSTOMER;
            }
        }
        WorkflowLinkDO workflowLinkDO = workflowLinkMapper.findByWorkflowTypeAndReferNo(workflowType, workflowReferNo);
        if (workflowLinkDO == null) {
            workflowLinkDO = workflowLinkMapper.findByWorkflowTypeAndReferNo(WorkflowType.WORKFLOW_TYPE_CUSTOMER, workflowReferNo);
            if(workflowLinkDO == null){
                result.setErrorCode(ErrorCode.WORKFLOW_LINK_NOT_EXISTS);
                return result;
            }
        }
        result.setResult(ConverterUtil.convert(workflowSupport.getVerifyUserGroupAndImg(workflowLinkDO), WorkflowLink.class));
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, Page<WorkflowLink>> getWorkflowLinkPage(WorkflowLinkQueryParam workflowLinkQueryParam) {
        ServiceResult<String, Page<WorkflowLink>> result = new ServiceResult<>();
        //工作台判断
        PageQuery pageQuery = new PageQuery(workflowLinkQueryParam.getPageNo(), workflowLinkQueryParam.getPageSize());
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("start", pageQuery.getStart());
        paramMap.put("pageSize", pageQuery.getPageSize());
        paramMap.put("workflowQueryParam", workflowLinkQueryParam);
        List<WorkflowLinkDO> workflowLinkDOList;
        Integer dataCount;
        if (workflowLinkQueryParam.getIsWorkbench() != null && CommonConstant.COMMON_CONSTANT_YES.equals(workflowLinkQueryParam.getIsWorkbench())) {
            //只有审核人数据
            List<String> currentUserGroupList = workflowVerifyUserGroupMapper.findGroupUUIDByUserIdAndVerifyStatus(userSupport.getCurrentUserId(), workflowLinkQueryParam.getVerifyStatus());
            paramMap.put("currentUserGroupList", currentUserGroupList);
            paramMap.put("verifyUserId", userSupport.getCurrentUserId().toString());
            SqlLogInterceptor.setExecuteSql("skip print  workflowLinkMapper.workbenchListCount  sql ......");
            dataCount = workflowLinkMapper.workbenchListCount(paramMap);
            SqlLogInterceptor.setExecuteSql("skip print  workflowLinkMapper.workbenchListPage  sql ......");
            workflowLinkDOList = workflowLinkMapper.workbenchListPage(paramMap);
        } else {
            //只有创建人与相关审核人查看自己数据
            if (!userSupport.isSuperUser()) {
                paramMap.put("verifyUserId", userSupport.getCurrentUserId().toString());
                List<String> currentUserGroupList = workflowVerifyUserGroupMapper.findGroupUUIDByUserId(userSupport.getCurrentUserId());
                paramMap.put("currentUserGroupList", currentUserGroupList);
            }
            SqlLogInterceptor.setExecuteSql("skip print  workflowLinkMapper.listCount  sql ......");
            dataCount = workflowLinkMapper.listCount(paramMap);
            SqlLogInterceptor.setExecuteSql("skip print  workflowLinkMapper.listPage  sql ......");
            workflowLinkDOList = workflowLinkMapper.listPage(paramMap);
        }
        Page<WorkflowLink> page = new Page<>(ConverterUtil.convertList(workflowLinkDOList, WorkflowLink.class), dataCount, workflowLinkQueryParam.getPageNo(), workflowLinkQueryParam.getPageSize());
        result.setResult(page);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, Page<WorkflowLink>> getCurrentUserWorkflowLinkPage(WorkflowLinkQueryParam workflowLinkQueryParam) {
        ServiceResult<String, Page<WorkflowLink>> result = new ServiceResult<>();
        workflowLinkQueryParam.setCurrentVerifyUser(userSupport.getCurrentUserId());
        workflowLinkQueryParam.setVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
        PageQuery pageQuery = new PageQuery(workflowLinkQueryParam.getPageNo(), workflowLinkQueryParam.getPageSize());
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("start", pageQuery.getStart());
        paramMap.put("pageSize", pageQuery.getPageSize());
        paramMap.put("workflowQueryParam", workflowLinkQueryParam);
        paramMap.put("permissionParam", permissionSupport.getPermissionParam(PermissionType.PERMISSION_TYPE_USER));
        Integer dataCount = workflowLinkMapper.listCount(paramMap);
        List<WorkflowLinkDO> workflowLinkDOList = workflowLinkMapper.listPage(paramMap);
        Page<WorkflowLink> page = new Page<>(ConverterUtil.convertList(workflowLinkDOList, WorkflowLink.class), dataCount, workflowLinkQueryParam.getPageNo(), workflowLinkQueryParam.getPageSize());
        result.setResult(page);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    /**
     * <pre>
     * 将该方法类的相关代码移到verifyWorkFlowFromCore方法中
     * 目的是为了动态传入当前审核人用户编号
     * </pre>
     *
     * @return
     * @author daiqi
     * @date 2018/4/28 15:12
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, Integer> verifyWorkFlow(String workflowLinkNo, Integer verifyStatus, Integer returnType, String verifyOpinion, Integer nextVerifyUser, List<Integer> imgIdList) {
        return verifyWorkFlowFromCore(workflowLinkNo, verifyStatus, returnType, verifyOpinion, userSupport.getCurrentUserId(), nextVerifyUser, imgIdList);
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, Integer> verifyWorkFlowFromCore(String workflowLinkNo, Integer verifyStatus, Integer returnType, String verifyOpinion, Integer currentVerifyUser, Integer nextVerifyUser, List<Integer> imgIdList) {
        Date currentTime = new Date();
        ServiceResult<String, Integer> result = new ServiceResult<>();
        WorkflowLinkDO workflowLinkDO = workflowLinkMapper.findByNo(workflowLinkNo);
        if (workflowLinkDO == null) {
            result.setErrorCode(ErrorCode.WORKFLOW_LINK_NOT_EXISTS);
            return result;
        }
        //客户不允许拨回到上一层
        if ((WorkflowType.WORKFLOW_TYPE_CHANNEL_CUSTOMER.equals(workflowLinkDO.getWorkflowType()) || WorkflowType.WORKFLOW_TYPE_CUSTOMER.equals(workflowLinkDO.getWorkflowType())) && WorkflowReturnType.RETURN_TYPE_PREVIOUS.equals(returnType)) {
            result.setErrorCode(ErrorCode.WORKFLOW_CUSTOMER_CAN_NOT_BACK_PREVIOUS);
            return result;
        }
        List<WorkflowLinkDetailDO> workflowLinkDetailDOList = workflowLinkDO.getWorkflowLinkDetailDOList();
        if (workflowLinkDetailDOList == null || workflowLinkDetailDOList.isEmpty()) {
            result.setErrorCode(ErrorCode.WORKFLOW_LINK_HAVE_NO_DETAIL);
            return result;
        }
        WorkflowLinkDetailDO lastWorkflowLinkDetailDO = workflowLinkDetailDOList.get(0);
        if (VerifyStatus.VERIFY_STATUS_PASS.equals(lastWorkflowLinkDetailDO.getVerifyStatus()) || VerifyStatus.VERIFY_STATUS_BACK.equals(lastWorkflowLinkDetailDO.getVerifyStatus()) || VerifyStatus.VERIFY_STATUS_CANCEL.equals(lastWorkflowLinkDetailDO.getVerifyStatus())) {
            result.setErrorCode(ErrorCode.WORKFLOW_LINK_VERIFY_ALREADY_OVER);
            return result;
        }

        List<WorkflowVerifyUserGroupDO> workflowVerifyUserGroupDOList = workflowVerifyUserGroupMapper.findByVerifyUserGroupId(lastWorkflowLinkDetailDO.getVerifyUserGroupId());
        if (CollectionUtil.isEmpty(workflowVerifyUserGroupDOList)) {
            result.setErrorCode(ErrorCode.WORKFLOW_VERIFY_USER_GROUP_NOT_EXISTS);
            return result;
        }
        boolean flag = false;
        for (WorkflowVerifyUserGroupDO workflowVerifyUserGroupDO : workflowVerifyUserGroupDOList) {
            if (currentVerifyUser.equals(workflowVerifyUserGroupDO.getVerifyUser())) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            result.setErrorCode(ErrorCode.WORKFLOW_NOT_BELONG_TO_YOU);
            return result;
        }

        WorkflowTemplateDO workflowTemplateDO = workflowTemplateMapper.findById(workflowLinkDO.getWorkflowTemplateId());
        if (workflowTemplateDO == null) {
            result.setErrorCode(ErrorCode.WORKFLOW_TEMPLATE_NOT_EXISTS);
            return result;
        }
        List<WorkflowNodeDO> workflowNodeDOList = workflowTemplateDO.getWorkflowNodeDOList();
        if (workflowNodeDOList == null || workflowNodeDOList.isEmpty()) {
            result.setErrorCode(ErrorCode.WORKFLOW_TEMPLATE_HAVE_NO_NODE);
            return result;
        }
        // 是否通知业务模块
        boolean noticeBusinessModule = false;
        // 本步骤节点
        WorkflowNodeDO nextWorkflowNodeDO = null;
        WorkflowNodeDO previousWorkflowNodeDO = null;
        for (WorkflowNodeDO workflowNodeDO : workflowNodeDOList) {
            if (workflowNodeDO.getId().equals(lastWorkflowLinkDetailDO.getWorkflowPreviousNodeId())) {
                previousWorkflowNodeDO = workflowNodeDO;
            }
            if (workflowNodeDO.getId().equals(lastWorkflowLinkDetailDO.getWorkflowNextNodeId())) {
                nextWorkflowNodeDO = workflowNodeDO;
                break;
            }
        }
        Integer passCount = 0;
        boolean flagBoolean = false;
        for (WorkflowVerifyUserGroupDO workflowVerifyUserGroupDO : workflowVerifyUserGroupDOList) {
            if (currentVerifyUser.equals(workflowVerifyUserGroupDO.getVerifyUser()) && VerifyStatus.VERIFY_STATUS_COMMIT.equals(workflowVerifyUserGroupDO.getVerifyStatus())) {
                workflowVerifyUserGroupDO.setVerifyTime(currentTime);
                workflowVerifyUserGroupDO.setVerifyStatus(verifyStatus);
                workflowVerifyUserGroupDO.setVerifyOpinion(verifyOpinion);
                workflowVerifyUserGroupDO.setRemark(verifyOpinion);
                workflowVerifyUserGroupDO.setUpdateTime(currentTime);
                workflowVerifyUserGroupDO.setUpdateUser(currentVerifyUser.toString());
                workflowVerifyUserGroupMapper.update(workflowVerifyUserGroupDO);
                //保存审核图片
                workflowSupport.saveWorkflowImage(workflowVerifyUserGroupDO.getId(), imgIdList, currentTime);

                if (WorkflowType.WORKFLOW_TYPE_CUSTOMER.equals(workflowLinkDO.getWorkflowType()) || (WorkflowType.WORKFLOW_TYPE_CHANNEL_CUSTOMER.equals(workflowLinkDO.getWorkflowType()) && workflowLinkDO.getWorkflowStep() >= CommonConstant.WORKFLOW_STEP_TWO)) {
                    CustomerDO customerDO = customerMapper.findByNo(workflowLinkDO.getWorkflowReferNo());
                    if (customerDO == null) {
                        result.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
                        return result;
                    }
                    Integer companyId = userSupport.getCurrentUserCompanyId();

                    if (CustomerType.CUSTOMER_TYPE_COMPANY.equals(customerDO.getCustomerType())) {
                        customerDO = customerMapper.findCustomerCompanyByNo(customerDO.getCustomerNo());

                        if (CustomerConsignVerifyStatus.VERIFY_STATUS_COMMIT.equals(customerDO.getCustomerCompanyDO().getAddressVerifyStatus())) {
                            SubCompanyCityCoverDO subCompanyCityCoverDO = workflowSupport.getSubCompanyCityCoverDO(customerDO.getCustomerCompanyDO().getCity(), customerDO.getCustomerCompanyDO().getProvince());
                            if (VerifyStatus.VERIFY_STATUS_PASS.equals(workflowVerifyUserGroupDO.getVerifyStatus()) && companyId.equals(subCompanyCityCoverDO.getSubCompanyId())) {
                                customerDO.getCustomerCompanyDO().setAddressVerifyStatus(CustomerConsignVerifyStatus.VERIFY_STATUS_FIRST_PASS);
                                customerDO.getCustomerCompanyDO().setUpdateTime(currentTime);
                                customerDO.getCustomerCompanyDO().setUpdateUser(currentVerifyUser.toString());
                                customerCompanyMapper.update(customerDO.getCustomerCompanyDO());
                            }
                        } else if (VerifyStatus.VERIFY_STATUS_PASS.equals(workflowVerifyUserGroupDO.getVerifyStatus()) && CustomerConsignVerifyStatus.VERIFY_STATUS_FIRST_PASS.equals(customerDO.getCustomerCompanyDO().getAddressVerifyStatus()) && userSupport.isRiskManagementPerson()) {
                            customerDO.getCustomerCompanyDO().setAddressVerifyStatus(CustomerConsignVerifyStatus.VERIFY_STATUS_END_PASS);
                            customerDO.getCustomerCompanyDO().setUpdateTime(currentTime);
                            customerDO.getCustomerCompanyDO().setUpdateUser(currentVerifyUser.toString());
                            customerCompanyMapper.update(customerDO.getCustomerCompanyDO());
                        }
                    } else if (CustomerType.CUSTOMER_TYPE_PERSON.equals(customerDO.getCustomerType())) {
                        customerDO = customerMapper.findCustomerPersonByNo(customerDO.getCustomerNo());
                    }
                    List<CustomerConsignInfoDO> customerConsignInfoDOList = customerConsignInfoMapper.findByCustomerId(customerDO.getId());
                    if (CollectionUtil.isEmpty(customerConsignInfoDOList)) {
                        result.setErrorCode(ErrorCode.CUSTOMER_CONSIGN_NOT_EXISTS);
                        return result;
                    }
                    //判断收货地址
                    for (CustomerConsignInfoDO customerConsignInfoDO : customerConsignInfoDOList) {
                        if (CustomerConsignVerifyStatus.VERIFY_STATUS_COMMIT.equals(customerConsignInfoDO.getVerifyStatus())) {
                            SubCompanyCityCoverDO subCompanyCityCoverDO = workflowSupport.getSubCompanyCityCoverDO(customerConsignInfoDO.getCity(), customerConsignInfoDO.getProvince());
                            if (VerifyStatus.VERIFY_STATUS_PASS.equals(workflowVerifyUserGroupDO.getVerifyStatus()) && companyId.equals(subCompanyCityCoverDO.getSubCompanyId())) {
                                customerConsignInfoDO.setVerifyStatus(CustomerConsignVerifyStatus.VERIFY_STATUS_FIRST_PASS);
                                customerConsignInfoDO.setUpdateTime(currentTime);
                                customerConsignInfoDO.setUpdateUser(currentVerifyUser.toString());
                                customerConsignInfoMapper.update(customerConsignInfoDO);
                            }
                        } else if (VerifyStatus.VERIFY_STATUS_PASS.equals(workflowVerifyUserGroupDO.getVerifyStatus()) && CustomerConsignVerifyStatus.VERIFY_STATUS_FIRST_PASS.equals(customerConsignInfoDO.getVerifyStatus()) && userSupport.isRiskManagementPerson()) {
                            customerConsignInfoDO.setVerifyStatus(CustomerConsignVerifyStatus.VERIFY_STATUS_END_PASS);
                            customerConsignInfoDO.setUpdateTime(currentTime);
                            customerConsignInfoDO.setUpdateUser(currentVerifyUser.toString());
                            customerConsignInfoMapper.update(customerConsignInfoDO);
                        }
                    }
                } else if (WorkflowType.WORKFLOW_TYPE_CUSTOMER_CONSIGN.equals(workflowLinkDO.getWorkflowType()) && VerifyStatus.VERIFY_STATUS_PASS.equals(workflowVerifyUserGroupDO.getVerifyStatus())) {
                    updateCustomerConsignVerifyStatus(Integer.valueOf(workflowLinkDO.getWorkflowReferNo()), CustomerConsignVerifyStatus.VERIFY_STATUS_FIRST_PASS, currentTime, currentVerifyUser);
                }
            }
            if (VerifyType.VERIFY_TYPE_THE_SAME_GROUP_ALL_PASS.equals(workflowVerifyUserGroupDO.getVerifyType()) && VerifyStatus.VERIFY_STATUS_PASS.equals(workflowVerifyUserGroupDO.getVerifyStatus())) {
                passCount++;
            } else if ((VerifyType.VERIFY_TYPE_THIS_IS_PASS.equals(workflowVerifyUserGroupDO.getVerifyType()) || (VerifyType.VERIFY_TYPE_ALL_USER_THIS_IS_PASS.equals(workflowVerifyUserGroupDO.getVerifyType())) && VerifyStatus.VERIFY_STATUS_PASS.equals(workflowVerifyUserGroupDO.getVerifyStatus()))) {
                //在审核中的人员改为他人已审核通过成功
                List<WorkflowVerifyUserGroupDO> updateWorkflowVerifyUserGroupDOList = new ArrayList<>();
                for (WorkflowVerifyUserGroupDO workflowVerifyUserGroupDOIng : workflowVerifyUserGroupDOList) {
                    if (VerifyStatus.VERIFY_STATUS_COMMIT.equals(workflowVerifyUserGroupDOIng.getVerifyStatus())) {
                        workflowVerifyUserGroupDOIng.setVerifyTime(currentTime);
                        workflowVerifyUserGroupDOIng.setVerifyStatus(VerifyStatus.VERIFY_STATUS_OTHER_PASS);
                        workflowVerifyUserGroupDOIng.setUpdateTime(currentTime);
                        workflowVerifyUserGroupDOIng.setUpdateUser(currentVerifyUser.toString());
                        updateWorkflowVerifyUserGroupDOList.add(workflowVerifyUserGroupDOIng);
                    }
                }
                if (CollectionUtil.isNotEmpty(updateWorkflowVerifyUserGroupDOList)) {
                    SqlLogInterceptor.setExecuteSql("skip print  workflowVerifyUserGroupMapper.updateBatchVerifyUserGroup  sql ......");
                    workflowVerifyUserGroupMapper.updateBatchVerifyUserGroup(updateWorkflowVerifyUserGroupDOList);
                }
                flagBoolean = true;
                break;
            }
            if (VerifyStatus.VERIFY_STATUS_BACK.equals(workflowVerifyUserGroupDO.getVerifyStatus())) {
                //在审核中的人员改为取消中
                List<WorkflowVerifyUserGroupDO> updateWorkflowVerifyUserGroupDOList = new ArrayList<>();
                for (WorkflowVerifyUserGroupDO workflowVerifyUserGroupDOIng : workflowVerifyUserGroupDOList) {
                    if (VerifyStatus.VERIFY_STATUS_COMMIT.equals(workflowVerifyUserGroupDOIng.getVerifyStatus())) {
                        workflowVerifyUserGroupDOIng.setVerifyTime(currentTime);
                        workflowVerifyUserGroupDOIng.setVerifyStatus(VerifyStatus.VERIFY_STATUS_CANCEL);
                        workflowVerifyUserGroupDOIng.setUpdateTime(currentTime);
                        workflowVerifyUserGroupDOIng.setUpdateUser(currentVerifyUser.toString());
                        updateWorkflowVerifyUserGroupDOList.add(workflowVerifyUserGroupDOIng);
                    }
                    if (WorkflowType.WORKFLOW_TYPE_CUSTOMER.equals(workflowLinkDO.getWorkflowType()) || (WorkflowType.WORKFLOW_TYPE_CHANNEL_CUSTOMER.equals(workflowLinkDO.getWorkflowType()) && CommonConstant.WORKFLOW_STEP_TWO.equals(workflowLinkDO.getWorkflowStep()))) {
                        CustomerDO customerDO = customerMapper.findByNo(workflowLinkDO.getWorkflowReferNo());
                        if (customerDO == null) {
                            result.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
                            return result;
                        }
                        if (CustomerType.CUSTOMER_TYPE_COMPANY.equals(customerDO.getCustomerType())) {
                            customerDO = customerMapper.findCustomerCompanyByNo(customerDO.getCustomerNo());
                            if (CustomerConsignVerifyStatus.VERIFY_STATUS_COMMIT.equals(customerDO.getCustomerCompanyDO().getAddressVerifyStatus())) {
                                customerDO.getCustomerCompanyDO().setAddressVerifyStatus(CustomerConsignVerifyStatus.VERIFY_STATUS_BACK);
                                customerDO.getCustomerCompanyDO().setUpdateTime(currentTime);
                                customerDO.getCustomerCompanyDO().setUpdateUser(currentVerifyUser.toString());
                                customerCompanyMapper.update(customerDO.getCustomerCompanyDO());
                            }
                        } else if (CustomerType.CUSTOMER_TYPE_PERSON.equals(customerDO.getCustomerType())) {
                            customerDO = customerMapper.findCustomerPersonByNo(customerDO.getCustomerNo());
                        }
                        List<CustomerConsignInfoDO> customerConsignInfoDOList = customerConsignInfoMapper.findByCustomerId(customerDO.getId());
                        if (CollectionUtil.isEmpty(customerConsignInfoDOList)) {
                            result.setErrorCode(ErrorCode.CUSTOMER_CONSIGN_NOT_EXISTS);
                            return result;
                        }
                        for (CustomerConsignInfoDO customerConsignInfoDO : customerConsignInfoDOList) {
                            if (CustomerConsignVerifyStatus.VERIFY_STATUS_COMMIT.equals(customerConsignInfoDO.getVerifyStatus())) {
                                customerConsignInfoDO.setVerifyStatus(CustomerConsignVerifyStatus.VERIFY_STATUS_BACK);
                                customerConsignInfoDO.setUpdateTime(currentTime);
                                customerConsignInfoDO.setUpdateUser(currentVerifyUser.toString());
                                customerConsignInfoMapper.update(customerConsignInfoDO);
                            }
                        }
                    }
                }
                if (CollectionUtil.isNotEmpty(updateWorkflowVerifyUserGroupDOList)) {
                    SqlLogInterceptor.setExecuteSql("skip print  workflowVerifyUserGroupMapper.updateBatchVerifyUserGroup  sql ......");
                    workflowVerifyUserGroupMapper.updateBatchVerifyUserGroup(updateWorkflowVerifyUserGroupDOList);
                }
                flagBoolean = true;
                break;
            }
        }
        if (!flagBoolean) {
            if (!passCount.equals(workflowVerifyUserGroupDOList.size())) {
                result.setResult(workflowLinkDO.getId());
                result.setErrorCode(ErrorCode.SUCCESS);
                return result;
            }
        }
        // 如果审核通过并且下一步审核不为空的时候，判断下一步的审核人是否正确
        //工作流为渠道分公司审核客户时，不需要校验
        List<User> verifyUserList = null;
        if (!WorkflowType.WORKFLOW_TYPE_CHANNEL_CUSTOMER.equals(workflowTemplateDO.getWorkflowType()) && VerifyStatus.VERIFY_STATUS_PASS.equals(verifyStatus) && nextWorkflowNodeDO != null) {
            Integer subCompanyId = workflowSupport.getSubCompanyId(workflowLinkDO.getWorkflowType(), workflowLinkDO.getWorkflowReferNo(), nextWorkflowNodeDO);
            //检查审核人以及类型
            verifyUserList = workflowSupport.checkVerifyUserType(nextWorkflowNodeDO, subCompanyId, nextVerifyUser);
        }
        if (WorkflowType.WORKFLOW_TYPE_CHANNEL_CUSTOMER.equals(workflowTemplateDO.getWorkflowType()) && nextWorkflowNodeDO != null && VerifyStatus.VERIFY_STATUS_PASS.equals(verifyStatus) && CommonConstant.WORKFLOW_STEP_THREE.equals(nextWorkflowNodeDO.getWorkflowStep())) {
            Integer subCompanyId = workflowSupport.getSubCompanyId(workflowLinkDO.getWorkflowType(), workflowLinkDO.getWorkflowReferNo(), nextWorkflowNodeDO);
            //检查审核人以及类型
            verifyUserList = workflowSupport.checkVerifyUserType(nextWorkflowNodeDO, subCompanyId, nextVerifyUser);
        }

        lastWorkflowLinkDetailDO.setVerifyStatus(verifyStatus);
        lastWorkflowLinkDetailDO.setVerifyTime(currentTime);
        lastWorkflowLinkDetailDO.setVerifyOpinion(verifyOpinion);
        lastWorkflowLinkDetailDO.setRemark(verifyOpinion);
        lastWorkflowLinkDetailDO.setUpdateUser(currentVerifyUser.toString());
        lastWorkflowLinkDetailDO.setUpdateTime(currentTime);
        workflowLinkDetailMapper.update(lastWorkflowLinkDetailDO);

        if (VerifyStatus.VERIFY_STATUS_PASS.equals(verifyStatus)) {
            if (WorkflowType.WORKFLOW_TYPE_CHANNEL_CUSTOMER.equals(workflowTemplateDO.getWorkflowType()) && workflowLinkDO.getWorkflowStep() == 1) {
                ServiceResult<String, String> channelCustomerCommitResult = channelCustomerCommitWorkFlow(workflowLinkDO, nextVerifyUser);
                if (!ErrorCode.SUCCESS.equals(channelCustomerCommitResult.getErrorCode())) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  // 回滚
                    result.setErrorCode(channelCustomerCommitResult.getErrorCode());
                    return result;
                }
            } else if (nextWorkflowNodeDO != null) {
                //生成审核组id
                String verifyUserGroupId = generateNoSupport.generateVerifyUserGroupId();
                //todo 判别商城或者原逻辑 生成审核组
                workflowSupport.saveWorkflowVerifyUserGroup(verifyUserGroupId, verifyUserList, currentTime, nextVerifyUser, currentVerifyUser, nextWorkflowNodeDO.getVerifyType());

                workflowLinkDO.setCurrentVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
                WorkflowLinkDetailDO workflowLinkDetailDO = new WorkflowLinkDetailDO();
                workflowLinkDetailDO.setWorkflowLinkId(workflowLinkDO.getId());
                workflowLinkDetailDO.setWorkflowReferNo(lastWorkflowLinkDetailDO.getWorkflowReferNo());
                workflowLinkDetailDO.setWorkflowStep(nextWorkflowNodeDO.getWorkflowStep());
                workflowLinkDetailDO.setWorkflowPreviousNodeId(lastWorkflowLinkDetailDO.getWorkflowCurrentNodeId());
                workflowLinkDetailDO.setWorkflowCurrentNodeId(nextWorkflowNodeDO.getId());
                workflowLinkDetailDO.setWorkflowNextNodeId(nextWorkflowNodeDO.getWorkflowNextNodeId());
                workflowLinkDetailDO.setVerifyUser(nextVerifyUser);
                workflowLinkDetailDO.setVerifyUserGroupId(verifyUserGroupId);
                workflowLinkDetailDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
                workflowLinkDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                workflowLinkDetailDO.setCreateUser(currentVerifyUser.toString());
                workflowLinkDetailDO.setCreateTime(currentTime);
                workflowLinkDetailMapper.save(workflowLinkDetailDO);
                workflowLinkDO.setWorkflowStep(nextWorkflowNodeDO.getWorkflowStep());
                workflowLinkDO.setWorkflowCurrentNodeId(nextWorkflowNodeDO.getId());
                workflowLinkDO.setVerifyUserGroupId(verifyUserGroupId);
                if (VerifyType.VERIFY_TYPE_ALL_USER_THIS_IS_PASS.equals(nextWorkflowNodeDO.getVerifyType()) || VerifyType.VERIFY_TYPE_THE_SAME_GROUP_ALL_PASS.equals(nextWorkflowNodeDO.getVerifyType())) {
                    for (User user : verifyUserList) {
                        messageService.superSendMessage(MessageContant.WORKFLOW_COMMIT_TITLE, String.format(MessageContant.WORKFLOW_COMMIT_CONTENT, WorkflowType.getWorkflowTypeDesc(workflowLinkDO.getWorkflowType()), workflowLinkDO.getWorkflowLinkNo()), user.getUserId());
                    }
                } else {
                    messageService.superSendMessage(MessageContant.WORKFLOW_COMMIT_TITLE, String.format(MessageContant.WORKFLOW_COMMIT_CONTENT, WorkflowType.getWorkflowTypeDesc(workflowLinkDO.getWorkflowType()), workflowLinkDO.getWorkflowLinkNo()), nextVerifyUser);
                }
            } else {
                workflowLinkDO.setCurrentVerifyStatus(VerifyStatus.VERIFY_STATUS_PASS);
                noticeBusinessModule = true;
            }
            if (nextVerifyUser != null) {
                workflowLinkDO.setCurrentVerifyUser(nextVerifyUser);
            }
        } else {
            workflowLinkDO.setCurrentVerifyUser(null);
            workflowLinkDO.setCurrentVerifyStatus(VerifyStatus.VERIFY_STATUS_BACK);
            // 拒绝并且一步驳回到底
            if (returnType == null || WorkflowReturnType.RETURN_TYPE_ROOT.equals(returnType)) {
                noticeBusinessModule = true;
                workflowLinkDO.setWorkflowStep(0);
                workflowLinkDO.setCurrentVerifyUser(CommonConstant.SUPER_USER_ID);
            } else if (previousWorkflowNodeDO != null) {
                if ((WorkflowType.WORKFLOW_TYPE_CUSTOMER.equals(workflowLinkDO.getWorkflowType()) || WorkflowType.WORKFLOW_TYPE_CHANNEL_CUSTOMER.equals(workflowLinkDO.getWorkflowType()) && userSupport.isRiskManagementPerson())) {
                    // 如果是客户和地址并且是风控审核就驳回，那么就相当于驳回到根部
                    noticeBusinessModule = true;
                    workflowLinkDO.setWorkflowStep(0);
                    workflowLinkDO.setCurrentVerifyUser(CommonConstant.SUPER_USER_ID);
                } else {
                    WorkflowLinkDetailDO workflowLinkDetailDO = new WorkflowLinkDetailDO();
                    List<Integer> userIdList = new ArrayList<>();
                    if (workflowLinkDetailDOList.size() > 1) {
                        WorkflowLinkDetailDO previousWorkflowLinkDetailDO = workflowLinkDetailDOList.get(1);
                        List<WorkflowVerifyUserGroupDO> newWorkflowVerifyUserGroupDOList = workflowVerifyUserGroupMapper.findByVerifyUserGroupId(previousWorkflowLinkDetailDO.getVerifyUserGroupId());
                        String groupId = generateNoSupport.generateVerifyUserGroupId();
                        for (WorkflowVerifyUserGroupDO workflowVerifyUserGroupDO : newWorkflowVerifyUserGroupDOList) {
                            WorkflowVerifyUserGroupDO newWorkflowVerifyUserGroupDO = new WorkflowVerifyUserGroupDO();
                            newWorkflowVerifyUserGroupDO.setVerifyUserGroupId(groupId);
                            if (WorkflowType.WORKFLOW_TYPE_CUSTOMER.equals(workflowLinkDO.getWorkflowType())) {
                                newWorkflowVerifyUserGroupDO.setVerifyType(VerifyType.VERIFY_TYPE_THE_SAME_GROUP_ALL_PASS);
                            } else {
                                newWorkflowVerifyUserGroupDO.setVerifyType(newWorkflowVerifyUserGroupDOList.get(0).getVerifyType());
                            }
                            newWorkflowVerifyUserGroupDO.setVerifyUser(workflowVerifyUserGroupDO.getVerifyUser());
                            newWorkflowVerifyUserGroupDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
                            newWorkflowVerifyUserGroupDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                            newWorkflowVerifyUserGroupDO.setCreateUser(currentVerifyUser.toString());
                            newWorkflowVerifyUserGroupDO.setCreateTime(currentTime);
                            workflowVerifyUserGroupMapper.save(newWorkflowVerifyUserGroupDO);
                            userIdList.add(workflowVerifyUserGroupDO.getVerifyUser());

                        }
                        workflowLinkDetailDO.setVerifyUserGroupId(groupId);
                        workflowLinkDO.setVerifyUserGroupId(groupId);
                        workflowLinkDO.setWorkflowCurrentNodeId(previousWorkflowNodeDO.getId());
                        workflowLinkDO.setCurrentVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
                    }
                    workflowLinkDetailDO.setWorkflowLinkId(workflowLinkDO.getId());
                    workflowLinkDetailDO.setWorkflowReferNo(lastWorkflowLinkDetailDO.getWorkflowReferNo());
                    workflowLinkDetailDO.setWorkflowStep(previousWorkflowNodeDO.getWorkflowStep());
                    workflowLinkDetailDO.setWorkflowPreviousNodeId(previousWorkflowNodeDO.getWorkflowPreviousNodeId());
                    workflowLinkDetailDO.setWorkflowCurrentNodeId(previousWorkflowNodeDO.getId());
                    workflowLinkDetailDO.setWorkflowNextNodeId(previousWorkflowNodeDO.getWorkflowNextNodeId());
                    workflowLinkDetailDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
                    workflowLinkDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                    workflowLinkDetailDO.setCreateUser(currentVerifyUser.toString());
                    workflowLinkDetailDO.setCreateTime(currentTime);
                    workflowLinkDetailMapper.save(workflowLinkDetailDO);
                    workflowLinkDO.setWorkflowStep(previousWorkflowNodeDO.getWorkflowStep());
                    for (Integer userId : userIdList) {
                        messageService.superSendMessage(MessageContant.WORKFLOW_VERIFY_BACK_TITLE, String.format(MessageContant.WORKFLOW_COMMIT_CONTENT, WorkflowType.getWorkflowTypeDesc(workflowLinkDO.getWorkflowType()), workflowLinkDO.getWorkflowLinkNo()), userId);
                    }
                }
            } else {
                // 如果第一步就驳回了，那么就相当于驳回到根部
                noticeBusinessModule = true;
                workflowLinkDO.setWorkflowStep(0);
                workflowLinkDO.setCurrentVerifyUser(CommonConstant.SUPER_USER_ID);
            }
        }

        if (noticeBusinessModule) {
            // 根据不同业务，回调业务系统
            VerifyReceiver verifyReceiver = workFlowManager.getService(workflowLinkDO.getWorkflowType());
            if (verifyReceiver == null) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  // 回滚
                result.setErrorCode(ErrorCode.WORKFLOW_LINK_NOT_EXISTS);
                return result;
            }
            String code = verifyReceiver.receiveVerifyResult(VerifyStatus.VERIFY_STATUS_PASS.equals(verifyStatus), workflowLinkDO.getWorkflowReferNo());
            if (!ErrorCode.SUCCESS.equals(code)) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  // 回滚
                result.setErrorCode(code);
                return result;
            }
        }
        workflowLinkDO.setUpdateUser(currentVerifyUser.toString());
        workflowLinkDO.setUpdateTime(currentTime);
        workflowLinkMapper.update(workflowLinkDO);

        result.setResult(workflowLinkDO.getId());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, Boolean> isNeedVerify(Integer workflowType) {
        ServiceResult<String, Boolean> result = new ServiceResult<>();
        if (workflowType == null) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }
        WorkflowTemplateDO workflowTemplateDO = workflowTemplateMapper.findByWorkflowType(workflowType);
        if (workflowTemplateDO == null || CollectionUtil.isEmpty(workflowTemplateDO.getWorkflowNodeDOList())) {
            result.setErrorCode(ErrorCode.SUCCESS);
            result.setResult(Boolean.FALSE);
            return result;
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(Boolean.TRUE);
        return result;
    }

    /**
     * 生成工作流线，只适用于首次创建
     */
    private String generateWorkflowLink(WorkflowTemplateDO workflowTemplateDO, String workflowReferNo, String commitRemark, Integer verifyUser, String verifyMatters, List<Integer> imgIdList, Date currentTime, String orderRemark, List<User> verifyUserList) {
        Integer loginUser = userSupport.getCurrentUserId();
        if (loginUser == null) {
            loginUser = CommonConstant.SUPER_USER_ID;
        }
        if (workflowTemplateDO == null) {
            return null;
        }
        List<WorkflowNodeDO> workflowNodeDOList = workflowTemplateDO.getWorkflowNodeDOList();
        if (workflowNodeDOList == null || workflowNodeDOList.isEmpty()) {
            return null;
        }
        WorkflowNodeDO thisWorkflowNodeDO = workflowNodeDOList.get(0);
        WorkflowNodeDO lastWorkflowNodeDO = workflowNodeDOList.get(workflowNodeDOList.size() - 1);
        //生成审核组id
        String verifyUserGroupId = generateNoSupport.generateVerifyUserGroupId();
        //todo 判别商城或者原逻辑 生成审核组
        workflowSupport.saveWorkflowVerifyUserGroup(verifyUserGroupId, verifyUserList, currentTime, verifyUser, loginUser, thisWorkflowNodeDO.getVerifyType());

        WorkflowLinkDO workflowLinkDO = new WorkflowLinkDO();
        workflowLinkDO.setWorkflowLinkNo(generateNoSupport.generateWorkflowLinkNo(currentTime, loginUser));
        workflowLinkDO.setWorkflowType(workflowTemplateDO.getWorkflowType());
        workflowLinkDO.setWorkflowTemplateId(workflowTemplateDO.getId());
        workflowLinkDO.setWorkflowReferNo(workflowReferNo);
        workflowLinkDO.setWorkflowStep(thisWorkflowNodeDO.getWorkflowStep());
        workflowLinkDO.setWorkflowLastStep(lastWorkflowNodeDO.getWorkflowStep());
        workflowLinkDO.setWorkflowCurrentNodeId(thisWorkflowNodeDO.getId());
        workflowLinkDO.setCommitUser(loginUser);
        workflowLinkDO.setCurrentVerifyUser(verifyUser);
        workflowLinkDO.setVerifyUserGroupId(verifyUserGroupId);
        workflowLinkDO.setCurrentVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
        workflowLinkDO.setVerifyMatters(verifyMatters);
        workflowLinkDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        workflowLinkDO.setCreateUser(loginUser.toString());
        workflowLinkDO.setCreateTime(currentTime);
        workflowLinkDO.setRemark(orderRemark);
        workflowLinkMapper.save(workflowLinkDO);

        // 生成提交人工作流
        WorkflowVerifyUserGroupDO commitWorkflowVerifyUserGroupDO = workflowSupport.saveCommitWorkflowVerifyUserGroupDO(loginUser, currentTime, commitRemark);
        //记录提交人的图片
        workflowSupport.saveWorkflowImage(commitWorkflowVerifyUserGroupDO.getId(), imgIdList, currentTime);
        //生成工作流详情提交人记录
        workflowSupport.saveCommitWorkflowLinkDetailDO(workflowLinkDO.getId(), workflowReferNo, thisWorkflowNodeDO.getId(), loginUser, commitWorkflowVerifyUserGroupDO.getVerifyUserGroupId(), commitRemark, currentTime);
        // 生成审批人工作流详情审核人组节点
        workflowSupport.saveVerifyWorkflowLinkDetailDO(workflowLinkDO.getId(), workflowReferNo, thisWorkflowNodeDO, workflowNodeDOList, verifyUser, verifyUserGroupId, loginUser, currentTime);
        return workflowLinkDO.getWorkflowLinkNo();
    }

    /**
     * 继续工作流
     */
    private String continueWorkflowLink(WorkflowLinkDO workflowLinkDO, String commitRemark, Integer verifyUser, String verifyMatters, List<Integer> imgIdList, Date currentTime, String orderRemark, List<User> verifyUserList) {
        Integer loginUser = userSupport.getCurrentUserId();
        if (loginUser == null) {
            loginUser = CommonConstant.SUPER_USER_ID;
        }
        if (workflowLinkDO == null) {
            return ErrorCode.WORKFLOW_LINK_NOT_EXISTS;
        }
        List<WorkflowLinkDetailDO> workflowLinkDetailDOList = workflowLinkDO.getWorkflowLinkDetailDOList();
        if (workflowLinkDetailDOList == null || workflowLinkDetailDOList.isEmpty()) {
            return ErrorCode.WORKFLOW_LINK_HAVE_NO_DETAIL;
        }

        WorkflowLinkDetailDO lastWorkflowLinkDetailDO = workflowLinkDetailDOList.get(0);
        if (!VerifyStatus.VERIFY_STATUS_BACK.equals(lastWorkflowLinkDetailDO.getVerifyStatus())) {
            return ErrorCode.WORKFLOW_LINK_STATUS_ERROR;
        }
        WorkflowTemplateDO workflowTemplateDO = workflowTemplateMapper.findById(workflowLinkDO.getWorkflowTemplateId());
        if (workflowTemplateDO == null) {
            return ErrorCode.WORKFLOW_TEMPLATE_NOT_EXISTS;
        }
        List<WorkflowNodeDO> workflowNodeDOList = workflowTemplateDO.getWorkflowNodeDOList();
        if (workflowNodeDOList == null || workflowNodeDOList.isEmpty()) {
            return ErrorCode.WORKFLOW_TEMPLATE_HAVE_NO_NODE;
        }
        WorkflowNodeDO thisWorkflowNodeDO = workflowNodeDOList.get(0);
        WorkflowNodeDO lastWorkflowNodeDO = workflowNodeDOList.get(workflowNodeDOList.size() - 1);

        //生成审核组id
        String verifyUserGroupId = generateNoSupport.generateVerifyUserGroupId();
        workflowSupport.saveWorkflowVerifyUserGroup(verifyUserGroupId, verifyUserList, currentTime, verifyUser, loginUser, thisWorkflowNodeDO.getVerifyType());

        workflowLinkDO.setWorkflowStep(thisWorkflowNodeDO.getWorkflowStep());
        workflowLinkDO.setWorkflowLastStep(lastWorkflowNodeDO.getWorkflowStep());
        workflowLinkDO.setWorkflowCurrentNodeId(thisWorkflowNodeDO.getId());
        workflowLinkDO.setCurrentVerifyUser(verifyUser);
        workflowLinkDO.setVerifyUserGroupId(verifyUserGroupId);
        workflowLinkDO.setCurrentVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
        workflowLinkDO.setVerifyMatters(verifyMatters);
        workflowLinkDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        workflowLinkDO.setUpdateUser(loginUser.toString());
        workflowLinkDO.setUpdateTime(currentTime);
        workflowLinkDO.setRemark(orderRemark);
        workflowLinkMapper.update(workflowLinkDO);

        // 生成提交人工作流
        WorkflowVerifyUserGroupDO commitWorkflowVerifyUserGroupDO = workflowSupport.saveCommitWorkflowVerifyUserGroupDO(loginUser, currentTime, commitRemark);
        //记录提交人的图片
        workflowSupport.saveWorkflowImage(commitWorkflowVerifyUserGroupDO.getId(), imgIdList, currentTime);
        //生成工作流详情提交人记录
        workflowSupport.saveCommitWorkflowLinkDetailDO(workflowLinkDO.getId(), workflowLinkDO.getWorkflowReferNo(), thisWorkflowNodeDO.getId(), loginUser, commitWorkflowVerifyUserGroupDO.getVerifyUserGroupId(), commitRemark, currentTime);
        // 生成审批人工作流详情审核人组节点
        workflowSupport.saveVerifyWorkflowLinkDetailDO(workflowLinkDO.getId(), workflowLinkDO.getWorkflowReferNo(), thisWorkflowNodeDO, workflowNodeDOList, verifyUser, verifyUserGroupId, loginUser, currentTime);
        return ErrorCode.SUCCESS;
    }

    @Override
    public ServiceResult<String, WorkflowTemplate> findWorkflowTemplate(Integer workflowTemplateId) {
        ServiceResult<String, WorkflowTemplate> result = new ServiceResult<>();
        WorkflowTemplateDO workflowTemplateDO = workflowTemplateMapper.findByWorkflowTemplateId(workflowTemplateId);
        if (workflowTemplateDO == null) {
            result.setErrorCode(ErrorCode.WORKFLOW_TEMPLATE_NOT_EXISTS);
            return result;
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(ConverterUtil.convert(workflowTemplateDO, WorkflowTemplate.class));
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, Integer> updateWorkflowNodeList(WorkflowTemplate workflowTemplate) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        String currentUserId = userSupport.getCurrentUserId().toString();
        Date currentTime = new Date();
        WorkflowTemplateDO workflowTemplateDO = workflowTemplateMapper.findByWorkflowTemplateId(workflowTemplate.getWorkflowTemplateId());

        if (workflowTemplateDO == null) {
            result.setErrorCode(ErrorCode.WORKFLOW_TEMPLATE_NOT_EXISTS);
            return result;
        }
        //删除原节点列表
        List<WorkflowNodeDO> workflowNodeDOList = workflowTemplateDO.getWorkflowNodeDOList();
        if (CollectionUtil.isNotEmpty(workflowNodeDOList)) {
            //删除
            for (WorkflowNodeDO workflowNodeDO : workflowNodeDOList) {
                workflowNodeDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                workflowNodeDO.setUpdateTime(currentTime);
                workflowNodeDO.setUpdateUser(currentUserId);
                workflowNodeMapper.update(workflowNodeDO);
            }
        }
        List<WorkflowNode> workflowNodeList = workflowTemplate.getWorkflowNodeList();
        //构建新节点列表
        if (CollectionUtil.isNotEmpty(workflowNodeList)) {
            //构建
            Integer previousNodeId = null;
            LinkedList<WorkflowNodeDO> linkedList = new LinkedList();
            for (int i = 0; i < workflowNodeList.size(); i++) {
                WorkflowNode workflowNode = workflowNodeList.get(i);
                WorkflowNodeDO workflowNodeDO = ConverterUtil.convert(workflowNode, WorkflowNodeDO.class);

                Integer workflowDepartmentTypeId = workflowNodeDO.getWorkflowDepartmentType();
                if (workflowDepartmentTypeId != null) {
                    DataDictionaryDO dataDictionaryDO = dataDictionaryMapper.findByDictionaryId(workflowDepartmentTypeId);
                    if (dataDictionaryDO == null) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        result.setErrorCode(ErrorCode.DEPARTMENT_NOT_EXISTS);
                        return result;
                    }
                }
                Integer workflowDepartmentId = workflowNodeDO.getWorkflowDepartment();
                if (workflowDepartmentId != null) {
                    DepartmentDO departmentDO = departmentMapper.findById(workflowDepartmentId);
                    if (departmentDO == null) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        result.setErrorCode(ErrorCode.DEPARTMENT_NOT_EXISTS);
                        return result;
                    }
                }
                Integer workflowRoleId = workflowNodeDO.getWorkflowRole();
                if (workflowRoleId != null) {
                    RoleDO roleDO = roleMapper.findById(workflowRoleId);
                    if (roleDO == null) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        result.setErrorCode(ErrorCode.ROLE_NOT_NULL);
                        return result;
                    }
                }
                Integer workflowUserId = workflowNodeDO.getWorkflowUser();
                if (workflowUserId != null) {
                    UserDO userDO = userMapper.findByUserId(workflowUserId);
                    if (userDO == null) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        result.setErrorCode(ErrorCode.USER_NOT_EXISTS);
                        return result;
                    }
                }

                workflowNodeDO.setWorkflowTemplateId(workflowTemplate.getWorkflowTemplateId());
                workflowNodeDO.setWorkflowStep(i + 1);
                workflowNodeDO.setWorkflowPreviousNodeId(previousNodeId);
                workflowNodeDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                workflowNodeDO.setCreateTime(currentTime);
                workflowNodeDO.setCreateUser(currentUserId);
                workflowNodeDO.setUpdateTime(currentTime);
                workflowNodeDO.setUpdateUser(currentUserId);
                workflowNodeMapper.save(workflowNodeDO);
                previousNodeId = workflowNodeDO.getId();

                if (CollectionUtil.isNotEmpty(linkedList)) {
                    WorkflowNodeDO lastWorkflowNodeDO = linkedList.getLast();
                    lastWorkflowNodeDO.setWorkflowNextNodeId(workflowNodeDO.getId());
                    workflowNodeMapper.update(lastWorkflowNodeDO);
                }
                linkedList.add(workflowNodeDO);
            }
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(workflowTemplateDO.getId());
        return result;
    }

    @Override
    public ServiceResult<String, Page<WorkflowTemplate>> pageWorkflowTemplate(WorkflowTemplateQueryParam workflowTemplateQueryParam) {
        ServiceResult<String, Page<WorkflowTemplate>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(workflowTemplateQueryParam.getPageNo(), workflowTemplateQueryParam.getPageSize());
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("start", pageQuery.getStart());
        paramMap.put("pageSize", pageQuery.getPageSize());
        paramMap.put("workflowTemplateQueryParam", workflowTemplateQueryParam);
        Integer totalCount = workflowTemplateMapper.listCount(paramMap);
        List<WorkflowTemplateDO> workflowTemplateDOList = workflowTemplateMapper.listPage(paramMap);
        List<WorkflowTemplate> list = ConverterUtil.convertList(workflowTemplateDOList, WorkflowTemplate.class);
        Page<WorkflowTemplate> page = new Page<>(list, totalCount, workflowTemplateQueryParam.getPageNo(), workflowTemplateQueryParam.getPageSize());
        result.setResult(page);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    public ServiceResult<String, String> customerCommitWorkFlow(Integer workflowType, String workflowReferNo, Integer verifyUser, String verifyMatters, String commitRemark, List<Integer> imgIdList, String orderRemark, Date currentTime, List<WorkflowNodeDO> workflowNodeDOList, WorkflowTemplateDO workflowTemplateDO) {
        ServiceResult<String, String> result = new ServiceResult<>();
        Integer loginUser = userSupport.getCurrentUser().getUserId();

        CustomerDO customerDO = customerMapper.findByNo(workflowReferNo);
        if (customerDO == null) {
            result.setErrorCode(ErrorCode.CUSTOMER_NOT_EXISTS);
            return result;
        }
        if (CustomerType.CUSTOMER_TYPE_COMPANY.equals(customerDO.getCustomerType())) {
            customerDO = customerMapper.findCustomerCompanyByNo(workflowReferNo);
        } else if (CustomerType.CUSTOMER_TYPE_PERSON.equals(customerDO.getCustomerType())) {
            customerDO = customerMapper.findCustomerPersonByNo(workflowReferNo);
        }

        List<CustomerConsignInfoDO> customerConsignInfoDOList = customerConsignInfoMapper.findByCustomerId(customerDO.getId());
        if (CollectionUtil.isEmpty(customerConsignInfoDOList)) {
            result.setErrorCode(ErrorCode.CUSTOMER_CONSIGN_NOT_EXISTS);
            return result;
        }

        String workflowLinkNo;
        String verifyUserGroupId = generateNoSupport.generateVerifyUserGroupId();
        //得到经营范围的公司id
        Set<Integer> subCompanySet = workflowSupport.getSubCompanyIdSet(customerDO, customerConsignInfoDOList, loginUser, currentTime);
        if (subCompanySet.size() > 0) {
            //保存经营范围的审核人并返回審核人id
            List<Integer> verifyUserList = workflowSupport.saveVerifyUserAndGetVerifyUser(subCompanySet, workflowNodeDOList.get(0).getWorkflowRoleType(), verifyUserGroupId, currentTime, loginUser);

            WorkflowLinkDO workflowLinkDO = workflowLinkMapper.findByWorkflowTypeAndReferNo(workflowType, workflowReferNo);

            WorkflowNodeDO thisWorkflowNodeDO = workflowNodeDOList.get(0);
            WorkflowNodeDO lastWorkflowNodeDO = workflowNodeDOList.get(workflowNodeDOList.size() - 1);

            if (workflowLinkDO == null) {
                WorkflowLinkDO newWorkflowLinkDO = new WorkflowLinkDO();
                newWorkflowLinkDO.setWorkflowLinkNo(generateNoSupport.generateWorkflowLinkNo(currentTime, loginUser));
                newWorkflowLinkDO.setWorkflowType(workflowTemplateDO.getWorkflowType());
                newWorkflowLinkDO.setWorkflowTemplateId(workflowTemplateDO.getId());
                newWorkflowLinkDO.setWorkflowReferNo(workflowReferNo);
                newWorkflowLinkDO.setWorkflowStep(thisWorkflowNodeDO.getWorkflowStep());
                newWorkflowLinkDO.setWorkflowLastStep(lastWorkflowNodeDO.getWorkflowStep());
                newWorkflowLinkDO.setWorkflowCurrentNodeId(thisWorkflowNodeDO.getId());
                newWorkflowLinkDO.setCommitUser(loginUser);
                newWorkflowLinkDO.setCurrentVerifyUser(verifyUser);
                newWorkflowLinkDO.setVerifyUserGroupId(verifyUserGroupId);
                newWorkflowLinkDO.setCurrentVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
                newWorkflowLinkDO.setVerifyMatters(verifyMatters);
                newWorkflowLinkDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                newWorkflowLinkDO.setCreateUser(loginUser.toString());
                newWorkflowLinkDO.setCreateTime(currentTime);
                newWorkflowLinkDO.setRemark(orderRemark);
                workflowLinkMapper.save(newWorkflowLinkDO);

                saveWorkflowLink(newWorkflowLinkDO, workflowReferNo, thisWorkflowNodeDO, loginUser, commitRemark, currentTime, imgIdList, verifyUser, verifyUserList, workflowNodeDOList, verifyUserGroupId);
                workflowLinkNo = newWorkflowLinkDO.getWorkflowLinkNo();
            } else {
                List<WorkflowLinkDetailDO> workflowLinkDetailDOList = workflowLinkDO.getWorkflowLinkDetailDOList();
                if (workflowLinkDetailDOList == null || workflowLinkDetailDOList.isEmpty()) {
                    result.setErrorCode(ErrorCode.WORKFLOW_LINK_NOT_EXISTS);
                    return result;
                }

                WorkflowLinkDetailDO lastWorkflowLinkDetailDO = workflowLinkDetailDOList.get(0);
                if (!VerifyStatus.VERIFY_STATUS_BACK.equals(lastWorkflowLinkDetailDO.getVerifyStatus())) {
                    result.setErrorCode(ErrorCode.WORKFLOW_LINK_STATUS_ERROR);
                    return result;
                }

                workflowLinkDO.setWorkflowStep(thisWorkflowNodeDO.getWorkflowStep());
                workflowLinkDO.setWorkflowLastStep(lastWorkflowNodeDO.getWorkflowStep());
                workflowLinkDO.setWorkflowCurrentNodeId(thisWorkflowNodeDO.getId());
                workflowLinkDO.setCurrentVerifyUser(verifyUser);
                workflowLinkDO.setVerifyUserGroupId(verifyUserGroupId);
                workflowLinkDO.setCurrentVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
                workflowLinkDO.setVerifyMatters(verifyMatters);
                workflowLinkDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                workflowLinkDO.setUpdateUser(loginUser.toString());
                workflowLinkDO.setUpdateTime(currentTime);
                workflowLinkDO.setRemark(orderRemark);
                workflowLinkMapper.update(workflowLinkDO);

                saveWorkflowLink(workflowLinkDO, workflowReferNo, thisWorkflowNodeDO, loginUser, commitRemark, currentTime, imgIdList, verifyUser, verifyUserList, workflowNodeDOList, verifyUserGroupId);
                workflowLinkNo = workflowLinkDO.getWorkflowLinkNo();
            }
        } else {
            Integer subCompanyId = workflowSupport.getSubCompanyId(workflowType, workflowReferNo, workflowNodeDOList.get(1));
            if (CommonConstant.ELECTRIC_SALE_COMPANY_ID.equals(subCompanyId)) {
                subCompanyId = CommonConstant.HEAD_COMPANY_ID;
            }
            WorkflowLinkDO workflowLinkDO = workflowLinkMapper.findByWorkflowTypeAndReferNo(workflowType, workflowReferNo);

            WorkflowNodeDO thisWorkflowNodeDO = workflowNodeDOList.get(1);
            List<User> verifyUserList = null;
            //todo 针对商城 获取审核人列表
            if (VerifyType.VERIFY_TYPE_ALL_USER_THIS_IS_PASS.equals(thisWorkflowNodeDO.getVerifyType())) {
                verifyUserList = workflowSupport.getUserListByNode(thisWorkflowNodeDO, subCompanyId);
                if (CollectionUtil.isEmpty(verifyUserList)) {
                    result.setErrorCode(ErrorCode.WORKFLOW_VERIFY_USER_IS_NULL);
                    return result;
                }
                //todo 判别商城或者原逻辑 生成审核组
                workflowSupport.saveWorkflowVerifyUserGroup(verifyUserGroupId, verifyUserList, currentTime, verifyUser, loginUser, thisWorkflowNodeDO.getVerifyType());
            } else {
                if (!workflowSupport.verifyVerifyUsers(thisWorkflowNodeDO, verifyUser, subCompanyId)) {
                    result.setErrorCode(ErrorCode.WORKFLOW_VERIFY_USER_ERROR);
                    return result;
                }
                //生成审核组id
                workflowVerifyUserGroupMapper.save(workflowSupport.getWorkflowVerifyUserGroupDO(verifyUserGroupId, verifyUser, currentTime, loginUser.toString(), VerifyType.VERIFY_TYPE_THIS_IS_PASS));
            }

            workflowLinkDO.setWorkflowStep(thisWorkflowNodeDO.getWorkflowStep());
            workflowLinkDO.setWorkflowLastStep(thisWorkflowNodeDO.getWorkflowStep());
            workflowLinkDO.setWorkflowCurrentNodeId(thisWorkflowNodeDO.getId());
            workflowLinkDO.setCurrentVerifyUser(verifyUser);
            workflowLinkDO.setVerifyUserGroupId(verifyUserGroupId);
            workflowLinkDO.setCurrentVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
            workflowLinkDO.setVerifyMatters(verifyMatters);
            workflowLinkDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            workflowLinkDO.setUpdateUser(loginUser.toString());
            workflowLinkDO.setUpdateTime(currentTime);
            workflowLinkDO.setRemark(orderRemark);
            workflowLinkMapper.update(workflowLinkDO);

            // 生成提交人工作流
            WorkflowVerifyUserGroupDO commitWorkflowVerifyUserGroupDO = workflowSupport.saveCommitWorkflowVerifyUserGroupDO(loginUser, currentTime, commitRemark);
            //记录提交人的图片
            workflowSupport.saveWorkflowImage(commitWorkflowVerifyUserGroupDO.getId(), imgIdList, currentTime);
            //生成工作流详情提交人记录
            workflowSupport.saveCommitWorkflowLinkDetailDO(workflowLinkDO.getId(), workflowReferNo, thisWorkflowNodeDO.getId(), loginUser, commitWorkflowVerifyUserGroupDO.getVerifyUserGroupId(), commitRemark, currentTime);

            WorkflowLinkDetailDO workflowLinkDetailDO = new WorkflowLinkDetailDO();
            workflowLinkDetailDO.setWorkflowLinkId(workflowLinkDO.getId());
            workflowLinkDetailDO.setWorkflowReferNo(workflowLinkDO.getWorkflowReferNo());
            workflowLinkDetailDO.setWorkflowStep(thisWorkflowNodeDO.getWorkflowStep());
            workflowLinkDetailDO.setWorkflowCurrentNodeId(thisWorkflowNodeDO.getId());
            workflowLinkDetailDO.setWorkflowPreviousNodeId(0);
            workflowLinkDetailDO.setVerifyUser(verifyUser);
            workflowLinkDetailDO.setVerifyUserGroupId(verifyUserGroupId);
            workflowLinkDetailDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
            workflowLinkDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            workflowLinkDetailDO.setCreateUser(loginUser.toString());
            workflowLinkDetailDO.setCreateTime(currentTime);
            workflowLinkDetailMapper.save(workflowLinkDetailDO);

            //发送信息
            if (VerifyType.VERIFY_TYPE_ALL_USER_THIS_IS_PASS.equals(thisWorkflowNodeDO.getVerifyType()) || VerifyType.VERIFY_TYPE_THE_SAME_GROUP_ALL_PASS.equals(thisWorkflowNodeDO.getVerifyType())) {
                for (User user : verifyUserList) {
                    messageService.superSendMessage(MessageContant.WORKFLOW_COMMIT_TITLE, String.format(MessageContant.WORKFLOW_COMMIT_CONTENT, WorkflowType.getWorkflowTypeDesc(workflowLinkDO.getWorkflowType()), workflowLinkDO.getWorkflowLinkNo()), user.getUserId());
                }
            } else {
                messageService.superSendMessage(MessageContant.WORKFLOW_COMMIT_TITLE, String.format(MessageContant.WORKFLOW_COMMIT_CONTENT, WorkflowType.getWorkflowTypeDesc(workflowLinkDO.getWorkflowType()), workflowLinkDO.getWorkflowLinkNo()), verifyUser);
            }
            workflowLinkNo = workflowLinkDO.getWorkflowLinkNo();
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(workflowLinkNo);
        return result;
    }

    public ServiceResult<String, String> channelCustomerCommitWorkFlow(WorkflowLinkDO workflowLinkDO, Integer verifyUser) {
        ServiceResult<String, String> result = new ServiceResult<>();
        Integer loginUser = userSupport.getCurrentUser().getUserId();
        Integer workflowType = WorkflowType.WORKFLOW_TYPE_CHANNEL_CUSTOMER;
        WorkflowTemplateDO workflowTemplateDO = workflowTemplateMapper.findByWorkflowType(workflowType);
        List<WorkflowNodeDO> workflowNodeDOList = workflowTemplateDO.getWorkflowNodeDOList();
        Date currentTime = new Date();
        CustomerDO customerDO = customerMapper.findByNo(workflowLinkDO.getWorkflowReferNo());
        if (CustomerType.CUSTOMER_TYPE_COMPANY.equals(customerDO.getCustomerType())) {
            customerDO = customerMapper.findCustomerCompanyByNo(workflowLinkDO.getWorkflowReferNo());
        } else if (CustomerType.CUSTOMER_TYPE_PERSON.equals(customerDO.getCustomerType())) {
            customerDO = customerMapper.findCustomerPersonByNo(workflowLinkDO.getWorkflowReferNo());
        }
        List<CustomerConsignInfoDO> customerConsignInfoDOList = customerConsignInfoMapper.findByCustomerId(customerDO.getId());
        if (CollectionUtil.isEmpty(customerConsignInfoDOList)) {
            result.setErrorCode(ErrorCode.CUSTOMER_CONSIGN_NOT_EXISTS);
            return result;
        }

        WorkflowNodeDO preWorkflowNodeDO = null;
        WorkflowNodeDO thisWorkflowNodeDO = null;
        WorkflowNodeDO nextWorkflowNodeDO = null;
        for (WorkflowNodeDO workflowNodeDO : workflowNodeDOList) {
            if (workflowNodeDO.getWorkflowStep() == 1) {
                //修改当前步骤审核状态
                preWorkflowNodeDO = workflowNodeDO;
            }
            if (workflowNodeDO.getWorkflowStep() == 2) {
                //修改当前步骤审核状态
                thisWorkflowNodeDO = workflowNodeDO;
            }
            if (workflowNodeDO.getWorkflowStep() == 3) {
                nextWorkflowNodeDO = workflowNodeDO;
            }
        }

        WorkflowNodeDO lastWorkflowNodeDO = workflowNodeDOList.get(workflowNodeDOList.size() - 1);
        String workflowLinkNo;
        String verifyUserGroupId = generateNoSupport.generateVerifyUserGroupId();
        //得到经营范围的公司id
        Set<Integer> subCompanySet = workflowSupport.getSubCompanyIdSet(customerDO, customerConsignInfoDOList, loginUser, currentTime);
        //如果没有需要审核的城市了（有可能是因为分总审核全部通过后，风控拒绝）
        if (subCompanySet.size() > 0) {
            //保存经营范围的审核人并返回審核人id
            List<Integer> verifyUserList = workflowSupport.saveVerifyUserAndGetVerifyUser(subCompanySet, thisWorkflowNodeDO.getWorkflowRoleType(), verifyUserGroupId, currentTime, loginUser);

            workflowLinkDO.setWorkflowStep(thisWorkflowNodeDO.getWorkflowStep());
            workflowLinkDO.setWorkflowLastStep(lastWorkflowNodeDO.getWorkflowStep());
            workflowLinkDO.setWorkflowCurrentNodeId(thisWorkflowNodeDO.getId());
            workflowLinkDO.setCurrentVerifyUser(0);
            workflowLinkDO.setVerifyUserGroupId(verifyUserGroupId);
            workflowLinkDO.setCurrentVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
            workflowLinkDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            workflowLinkDO.setUpdateUser(loginUser.toString());
            workflowLinkDO.setUpdateTime(currentTime);
            workflowLinkMapper.update(workflowLinkDO);

            // 生成审批人工作流
            WorkflowLinkDetailDO workflowLinkDetailDO = new WorkflowLinkDetailDO();
            workflowLinkDetailDO.setWorkflowLinkId(workflowLinkDO.getId());
            workflowLinkDetailDO.setWorkflowReferNo(workflowLinkDO.getWorkflowReferNo());
            workflowLinkDetailDO.setWorkflowStep(thisWorkflowNodeDO.getWorkflowStep());
            workflowLinkDetailDO.setWorkflowPreviousNodeId(preWorkflowNodeDO.getId());
            workflowLinkDetailDO.setWorkflowCurrentNodeId(thisWorkflowNodeDO.getId());
            workflowLinkDetailDO.setWorkflowNextNodeId(nextWorkflowNodeDO.getId());
            workflowLinkDetailDO.setVerifyUserGroupId(verifyUserGroupId);
            workflowLinkDetailDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
            workflowLinkDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            workflowLinkDetailDO.setCreateUser(loginUser.toString());
            workflowLinkDetailDO.setCreateTime(currentTime);
            workflowLinkDetailMapper.save(workflowLinkDetailDO);

            if (CollectionUtil.isNotEmpty(verifyUserList)) {
                for (Integer userId : verifyUserList) {
                    messageService.superSendMessage(MessageContant.WORKFLOW_COMMIT_TITLE, String.format(MessageContant.WORKFLOW_COMMIT_CONTENT, WorkflowType.getWorkflowTypeDesc(workflowLinkDO.getWorkflowType()), workflowLinkDO.getWorkflowLinkNo()), userId);
                }
            }
            workflowLinkNo = workflowLinkDO.getWorkflowLinkNo();
        } else {
            //直接走风控
            List<User> verifyUserList = null;
            if (VerifyType.VERIFY_TYPE_ALL_USER_THIS_IS_PASS.equals(nextWorkflowNodeDO.getVerifyType()) || VerifyType.VERIFY_TYPE_THE_SAME_GROUP_ALL_PASS.equals(nextWorkflowNodeDO.getVerifyType())) {
                verifyUserList = workflowSupport.getUserListByNode(nextWorkflowNodeDO, CommonConstant.HEAD_COMPANY_ID);
                if (CollectionUtil.isEmpty(verifyUserList)) {
                    result.setErrorCode(ErrorCode.WORKFLOW_VERIFY_USER_IS_NULL);
                    return result;
                }
                //todo 判别商城或者原逻辑 生成审核组
                workflowSupport.saveWorkflowVerifyUserGroup(verifyUserGroupId, verifyUserList, currentTime, verifyUser, loginUser, nextWorkflowNodeDO.getVerifyType());
            } else {
                if (!workflowSupport.verifyVerifyUsers(thisWorkflowNodeDO, verifyUser, CommonConstant.HEAD_COMPANY_ID)) {
                    result.setErrorCode(ErrorCode.WORKFLOW_VERIFY_USER_ERROR);
                    return result;
                }
                //生成审核组id
                workflowVerifyUserGroupMapper.save(workflowSupport.getWorkflowVerifyUserGroupDO(verifyUserGroupId, verifyUser, currentTime, loginUser.toString(), VerifyType.VERIFY_TYPE_THIS_IS_PASS));
            }
            //生成审核组id
            WorkflowVerifyUserGroupDO workflowVerifyUserGroupDO = new WorkflowVerifyUserGroupDO();
            workflowVerifyUserGroupDO.setVerifyUser(verifyUser);
            workflowVerifyUserGroupDO.setVerifyUserGroupId(verifyUserGroupId);
            workflowVerifyUserGroupDO.setVerifyType(VerifyType.VERIFY_TYPE_THIS_IS_PASS);
            workflowVerifyUserGroupDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
            workflowVerifyUserGroupDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            workflowVerifyUserGroupDO.setCreateUser(loginUser.toString());
            workflowVerifyUserGroupDO.setCreateTime(currentTime);
            workflowVerifyUserGroupMapper.save(workflowVerifyUserGroupDO);

            workflowLinkDO.setWorkflowStep(nextWorkflowNodeDO.getWorkflowStep());
            workflowLinkDO.setWorkflowLastStep(nextWorkflowNodeDO.getWorkflowStep());
            workflowLinkDO.setWorkflowCurrentNodeId(nextWorkflowNodeDO.getId());
            workflowLinkDO.setVerifyUserGroupId(verifyUserGroupId);
            workflowLinkDO.setCurrentVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
            workflowLinkDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            workflowLinkDO.setUpdateUser(loginUser.toString());
            workflowLinkDO.setUpdateTime(currentTime);
            workflowLinkMapper.update(workflowLinkDO);

            WorkflowLinkDetailDO workflowLinkDetailDO = new WorkflowLinkDetailDO();
            workflowLinkDetailDO.setWorkflowLinkId(workflowLinkDO.getId());
            workflowLinkDetailDO.setWorkflowReferNo(workflowLinkDO.getWorkflowReferNo());
            workflowLinkDetailDO.setWorkflowStep(nextWorkflowNodeDO.getWorkflowStep());
            workflowLinkDetailDO.setWorkflowCurrentNodeId(nextWorkflowNodeDO.getId());
            workflowLinkDetailDO.setWorkflowPreviousNodeId(workflowNodeDOList.get(1).getId());
            workflowLinkDetailDO.setVerifyUserGroupId(verifyUserGroupId);
            workflowLinkDetailDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
            workflowLinkDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            workflowLinkDetailDO.setCreateUser(loginUser.toString());
            workflowLinkDetailDO.setCreateTime(currentTime);
            workflowLinkDetailMapper.save(workflowLinkDetailDO);

            //发送信息
            if (VerifyType.VERIFY_TYPE_ALL_USER_THIS_IS_PASS.equals(nextWorkflowNodeDO.getVerifyType()) || VerifyType.VERIFY_TYPE_THE_SAME_GROUP_ALL_PASS.equals(nextWorkflowNodeDO.getVerifyType())) {
                for (User user : verifyUserList) {
                    messageService.superSendMessage(MessageContant.WORKFLOW_COMMIT_TITLE, String.format(MessageContant.WORKFLOW_COMMIT_CONTENT, WorkflowType.getWorkflowTypeDesc(workflowLinkDO.getWorkflowType()), workflowLinkDO.getWorkflowLinkNo()), user.getUserId());
                }
            } else {
                messageService.superSendMessage(MessageContant.WORKFLOW_COMMIT_TITLE, String.format(MessageContant.WORKFLOW_COMMIT_CONTENT, WorkflowType.getWorkflowTypeDesc(workflowLinkDO.getWorkflowType()), workflowLinkDO.getWorkflowLinkNo()), verifyUser);
            }
            workflowLinkNo = workflowLinkDO.getWorkflowLinkNo();
        }
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(workflowLinkNo);
        return result;
    }

    private void saveWorkflowLink(WorkflowLinkDO workflowLinkDO, String workflowReferNo, WorkflowNodeDO thisWorkflowNodeDO, Integer loginUserId, String commitRemark, Date currentTime, List<Integer> imgIdList, Integer verifyUser, List<Integer> verifyUserList, List<WorkflowNodeDO> workflowNodeDOList, String verifyUserGroupId) {
        // 生成提交人工作流
        WorkflowVerifyUserGroupDO commitWorkflowVerifyUserGroupDO = workflowSupport.saveCommitWorkflowVerifyUserGroupDO(loginUserId, currentTime, commitRemark);
        //记录提交人的图片
        workflowSupport.saveWorkflowImage(commitWorkflowVerifyUserGroupDO.getId(), imgIdList, currentTime);
        //生成工作流详情提交人记录
        workflowSupport.saveCommitWorkflowLinkDetailDO(workflowLinkDO.getId(), workflowReferNo, thisWorkflowNodeDO.getId(), loginUserId, commitWorkflowVerifyUserGroupDO.getVerifyUserGroupId(), commitRemark, currentTime);
        // 生成审批人工作流详情审核人组节点
        workflowSupport.saveVerifyWorkflowLinkDetailDO(workflowLinkDO.getId(), workflowReferNo, thisWorkflowNodeDO, workflowNodeDOList, verifyUser, verifyUserGroupId, loginUserId, currentTime);

        if (CollectionUtil.isNotEmpty(verifyUserList)) {
            for (Integer userId : verifyUserList) {
                messageService.superSendMessage(MessageContant.WORKFLOW_COMMIT_TITLE, String.format(MessageContant.WORKFLOW_COMMIT_CONTENT, WorkflowType.getWorkflowTypeDesc(workflowLinkDO.getWorkflowType()), workflowLinkDO.getWorkflowLinkNo()), userId);
            }
        }
    }

    private String updateCustomerConsignVerifyStatus(Integer customerConsignId, Integer verifyStatus, Date currentTime, Integer loginUserId) {
        CustomerConsignInfoDO customerConsignInfoDO = customerConsignInfoMapper.findById(customerConsignId);
        if (customerConsignInfoDO == null) {
            return ErrorCode.CUSTOMER_CONSIGN_NOT_EXISTS;
        }
        customerConsignInfoDO.setVerifyStatus(verifyStatus);
        customerConsignInfoDO.setUpdateTime(currentTime);
        customerConsignInfoDO.setUpdateUser(loginUserId.toString());
        customerConsignInfoMapper.update(customerConsignInfoDO);
        return ErrorCode.SUCCESS;
    }

    @Autowired
    private WorkflowTemplateMapper workflowTemplateMapper;

    @Autowired
    private WorkflowNodeMapper workflowNodeMapper;

    @Autowired
    private WorkflowLinkMapper workflowLinkMapper;

    @Autowired
    private WorkflowLinkDetailMapper workflowLinkDetailMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private WorkFlowManager workFlowManager;

    @Autowired
    private MessageService messageService;

    @Autowired
    private GenerateNoSupport generateNoSupport;

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private DataDictionaryMapper dataDictionaryMapper;

    @Autowired
    private PermissionSupport permissionSupport;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private CustomerConsignInfoMapper customerConsignInfoMapper;

    @Autowired
    private WorkflowVerifyUserGroupMapper workflowVerifyUserGroupMapper;

    @Autowired
    private SubCompanyCityCoverMapper subCompanyCityCoverMapper;

    @Autowired
    private CustomerCompanyMapper customerCompanyMapper;

    @Autowired
    private WorkflowSupport workflowSupport;
}
