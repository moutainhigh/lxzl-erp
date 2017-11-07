package com.lxzl.erp.core.service.workflow.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.VerifyStatus;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.core.service.user.UserRoleService;
import com.lxzl.erp.core.service.user.UserService;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.dataaccess.dao.mysql.workflow.WorkflowLinkDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.workflow.WorkflowLinkMapper;
import com.lxzl.erp.dataaccess.dao.mysql.workflow.WorkflowNodeMapper;
import com.lxzl.erp.dataaccess.dao.mysql.workflow.WorkflowTemplateMapper;
import com.lxzl.erp.dataaccess.domain.workflow.WorkflowLinkDO;
import com.lxzl.erp.dataaccess.domain.workflow.WorkflowLinkDetailDO;
import com.lxzl.erp.dataaccess.domain.workflow.WorkflowNodeDO;
import com.lxzl.erp.dataaccess.domain.workflow.WorkflowTemplateDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-04 16:12
 */
@Service("workflowService")
public class WorkflowServiceImpl implements WorkflowService {

    @Autowired
    private WorkflowTemplateMapper workflowTemplateMapper;

    @Autowired
    private WorkflowNodeMapper workflowNodeMapper;

    @Autowired
    private WorkflowLinkMapper workflowLinkMapper;

    @Autowired
    private WorkflowLinkDetailMapper workflowLinkDetailMapper;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserService userService;

    @Autowired(required = false)
    private HttpSession session;

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> commitWorkFlow(Integer workflowType, Integer workflowReferId, Integer verifyUser) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        WorkflowTemplateDO workflowTemplateDO = workflowTemplateMapper.findByWorkflowType(workflowType);
        if (workflowTemplateDO == null) {
            result.setErrorCode(ErrorCode.WORKFLOW_TYPE_NOT_EXISTS);
            return result;
        }
        List<WorkflowNodeDO> workflowNodeDOList = workflowTemplateDO.getWorkflowNodeDOList();
        if (workflowNodeDOList == null || workflowNodeDOList.isEmpty()) {
            result.setErrorCode(ErrorCode.WORKFLOW_TEMPLATE_HAVE_NO_NODE);
            return result;
        }

        Integer workflowLinkId;
        WorkflowLinkDO workflowLinkDO = workflowLinkMapper.findByWorkflowTypeAndReferId(workflowType, workflowReferId);
        if (workflowLinkDO == null) {
            workflowLinkId = generateWorkflowLink(workflowTemplateDO, workflowReferId, verifyUser);
        } else {
            workflowLinkId = workflowLinkDO.getId();
            continueWorkflowLink(workflowLinkDO, verifyUser);
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(workflowLinkId);
        return result;
    }

    @Override
    public ServiceResult<String, List<User>> getNextVerifyUsers(Integer workflowType, Integer workflowReferId) {
        ServiceResult<String, List<User>> result = new ServiceResult<>();
        List<User> userList = new ArrayList<>();
        WorkflowLinkDO workflowLinkDO = workflowLinkMapper.findByWorkflowTypeAndReferId(workflowType, workflowReferId);
        if (workflowLinkDO == null) {
            result.setErrorCode(ErrorCode.WORKFLOW_LINK_NOT_EXISTS);
            return result;
        }

        List<WorkflowLinkDetailDO> workflowLinkDetailDOList = workflowLinkDO.getWorkflowLinkDetailDOList();
        if (workflowLinkDetailDOList == null || workflowLinkDetailDOList.isEmpty()) {
            result.setErrorCode(ErrorCode.WORKFLOW_LINK_HAVE_NO_DETAIL);
            return result;
        }
        WorkflowLinkDetailDO lastWorkflowLinkDetailDO = workflowLinkDetailDOList.get(workflowLinkDetailDOList.size() - 1);
        WorkflowNodeDO workflowNodeDO = workflowNodeMapper.findById(lastWorkflowLinkDetailDO.getWorkflowCurrentNodeId());
        if (workflowNodeDO == null) {
            result.setErrorCode(ErrorCode.WORKFLOW_NODE_NOT_EXISTS);
            return result;
        }

        if (workflowNodeDO.getWorkflowUser() != null) {
            ServiceResult<String,User> userResult= userService.getUserById(workflowNodeDO.getWorkflowUser());
            if(ErrorCode.SUCCESS.equals(userResult.getErrorCode())){
                userList.add(userResult.getResult());
            }
        } else if (workflowNodeDO.getWorkflowRole() != null) {
            ServiceResult<String,List<User>> userResult = userRoleService.getUserByRoleId(workflowNodeDO.getWorkflowRole());
            if(ErrorCode.SUCCESS.equals(userResult.getErrorCode())){
                userList = userResult.getResult();
            }
        } else if (workflowNodeDO.getWorkflowDepartment() != null) {

        }

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(userList);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> verifyWorkFlow(Integer workflowLinkId, Integer verifyStatus, String verifyOpinion, Integer nextVerifyUser) {
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Date currentTime = new Date();

        ServiceResult<String, Integer> result = new ServiceResult<>();
        WorkflowLinkDO workflowLinkDO = workflowLinkMapper.findById(workflowLinkId);
        if (workflowLinkDO == null) {
            result.setErrorCode(ErrorCode.WORKFLOW_LINK_NOT_EXISTS);
            return result;
        }

        List<WorkflowLinkDetailDO> workflowLinkDetailDOList = workflowLinkDO.getWorkflowLinkDetailDOList();
        if (workflowLinkDetailDOList == null || workflowLinkDetailDOList.isEmpty()) {
            result.setErrorCode(ErrorCode.WORKFLOW_LINK_HAVE_NO_DETAIL);
            return result;
        }

        WorkflowLinkDetailDO lastWorkflowLinkDetailDO = workflowLinkDetailDOList.get(workflowLinkDetailDOList.size() - 1);
        if (VerifyStatus.VERIFY_STATUS_PASS.equals(lastWorkflowLinkDetailDO.getVerifyStatus())
                || VerifyStatus.VERIFY_STATUS_BACK.equals(lastWorkflowLinkDetailDO.getVerifyStatus())) {
            result.setErrorCode(ErrorCode.WORKFLOW_LINK_VERIFY_ALREADY_OVER);
            return result;
        }
        if (!loginUser.getUserId().equals(lastWorkflowLinkDetailDO.getVerifyUser())) {
            result.setErrorCode(ErrorCode.WORKFLOW_NOT_BELONG_TO_YOU);
            return result;
        }
        WorkflowTemplateDO workflowTemplateDO = workflowTemplateMapper.findById(workflowLinkDO.getWorkflowTemplateId());
        if (workflowTemplateDO == null) {
            result.setErrorCode(ErrorCode.WORKFLOW_NOT_BELONG_TO_YOU);
            return result;
        }
        List<WorkflowNodeDO> workflowNodeDOList = workflowTemplateDO.getWorkflowNodeDOList();
        if (workflowNodeDOList == null || workflowNodeDOList.isEmpty()) {
            result.setErrorCode(ErrorCode.WORKFLOW_TEMPLATE_HAVE_NO_NODE);
            return result;
        }
        lastWorkflowLinkDetailDO.setVerifyStatus(verifyStatus);
        lastWorkflowLinkDetailDO.setVerifyTime(currentTime);
        lastWorkflowLinkDetailDO.setVerifyOpinion(verifyOpinion);
        workflowLinkDetailMapper.update(lastWorkflowLinkDetailDO);

        boolean thisWorkflowStep = false;
        WorkflowNodeDO thisWorkflowNodeDO = null;
        WorkflowNodeDO nextWorkflowNodeDO = null;
        for (WorkflowNodeDO workflowNodeDO : workflowNodeDOList) {
            if (thisWorkflowStep) {
                nextWorkflowNodeDO = workflowNodeDO;
                break;
            }
            if (workflowNodeDO.getId().equals(lastWorkflowLinkDetailDO.getWorkflowNextNodeId())) {
                thisWorkflowStep = true;
                thisWorkflowNodeDO = workflowNodeDO;
            }
        }
        if (nextWorkflowNodeDO != null) {
            WorkflowLinkDetailDO workflowLinkDetailDO = new WorkflowLinkDetailDO();
            workflowLinkDetailDO.setWorkflowLinkId(workflowLinkDO.getId());
            workflowLinkDetailDO.setWorkflowReferId(lastWorkflowLinkDetailDO.getWorkflowReferId());
            workflowLinkDetailDO.setWorkflowStep(nextWorkflowNodeDO.getWorkflowStep());
            workflowLinkDetailDO.setWorkflowPreviousNodeId(lastWorkflowLinkDetailDO.getId());
            workflowLinkDetailDO.setWorkflowCurrentNodeId(thisWorkflowNodeDO.getId());
            workflowLinkDetailDO.setWorkflowNextNodeId(nextWorkflowNodeDO.getId());
            workflowLinkDetailDO.setVerifyUser(nextVerifyUser);
            workflowLinkDetailMapper.save(workflowLinkDetailDO);
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }


    /**
     * 生成工作流线，只适用于首次创建
     */
    private Integer generateWorkflowLink(WorkflowTemplateDO workflowTemplateDO, Integer workflowReferId, Integer verifyUser) {
        if (workflowTemplateDO == null) {
            return null;
        }
        List<WorkflowNodeDO> workflowNodeDOList = workflowTemplateDO.getWorkflowNodeDOList();
        if (workflowNodeDOList == null || workflowNodeDOList.isEmpty()) {
            return null;
        }
        WorkflowNodeDO thisWorkflowNodeDO = workflowNodeDOList.get(0);

        WorkflowLinkDO workflowLinkDO = new WorkflowLinkDO();
        workflowLinkDO.setWorkflowType(workflowTemplateDO.getWorkflowType());
        workflowLinkDO.setWorkflowTemplateId(workflowTemplateDO.getId());
        workflowLinkDO.setWorkflowReferId(workflowReferId);
        workflowLinkDO.setWorkflowStep(thisWorkflowNodeDO.getWorkflowStep());
        workflowLinkDO.setWorkflowCurrentNodeId(thisWorkflowNodeDO.getId());
        workflowLinkMapper.save(workflowLinkDO);

        WorkflowLinkDetailDO workflowLinkDetailDO = new WorkflowLinkDetailDO();
        workflowLinkDetailDO.setWorkflowLinkId(workflowLinkDO.getId());
        workflowLinkDetailDO.setWorkflowReferId(workflowReferId);
        workflowLinkDetailDO.setWorkflowStep(thisWorkflowNodeDO.getWorkflowStep());
        workflowLinkDetailDO.setWorkflowCurrentNodeId(thisWorkflowNodeDO.getId());
        if (workflowNodeDOList.size() > 1) {
            WorkflowNodeDO nextWorkflowNodeDO = workflowNodeDOList.get(1);
            workflowLinkDetailDO.setWorkflowNextNodeId(nextWorkflowNodeDO.getId());
        }
        workflowLinkDetailDO.setVerifyUser(verifyUser);
        workflowLinkDetailMapper.save(workflowLinkDetailDO);

        return workflowLinkDO.getId();
    }

    /**
     * 继续工作流
     */
    private void continueWorkflowLink(WorkflowLinkDO oldWorkflowLinkDO, Integer verifyUser) {
        if (oldWorkflowLinkDO == null) {
            return;
        }
        List<WorkflowLinkDetailDO> workflowLinkDetailDOList = oldWorkflowLinkDO.getWorkflowLinkDetailDOList();
        if (workflowLinkDetailDOList == null || workflowLinkDetailDOList.isEmpty()) {
            return;
        }

        WorkflowLinkDetailDO lastWorkflowLinkDetailDO = workflowLinkDetailDOList.get(workflowLinkDetailDOList.size() - 1);
        if (!VerifyStatus.VERIFY_STATUS_BACK.equals(lastWorkflowLinkDetailDO.getVerifyStatus())) {
            return;
        }


        WorkflowTemplateDO workflowTemplateDO = workflowTemplateMapper.findById(oldWorkflowLinkDO.getWorkflowTemplateId());
        if (workflowTemplateDO == null) {
            return;
        }
        List<WorkflowNodeDO> workflowNodeDOList = workflowTemplateDO.getWorkflowNodeDOList();
        if (workflowNodeDOList == null || workflowNodeDOList.isEmpty()) {
            return;
        }
        WorkflowLinkDO workflowLinkDO = new WorkflowLinkDO();
        workflowLinkDO.setWorkflowType(oldWorkflowLinkDO.getWorkflowType());
        workflowLinkDO.setWorkflowTemplateId(oldWorkflowLinkDO.getWorkflowTemplateId());
        workflowLinkDO.setWorkflowReferId(oldWorkflowLinkDO.getWorkflowReferId());
        workflowLinkDO.setWorkflowStep(oldWorkflowLinkDO.getWorkflowStep());
        workflowLinkDO.setWorkflowCurrentNodeId(oldWorkflowLinkDO.getWorkflowCurrentNodeId());
        workflowLinkMapper.save(workflowLinkDO);

        WorkflowNodeDO thisWorkflowNodeDO = workflowNodeDOList.get(0);
        WorkflowLinkDetailDO workflowLinkDetailDO = new WorkflowLinkDetailDO();
        workflowLinkDetailDO.setWorkflowLinkId(workflowLinkDO.getId());
        workflowLinkDetailDO.setWorkflowReferId(lastWorkflowLinkDetailDO.getWorkflowReferId());
        workflowLinkDetailDO.setWorkflowStep(thisWorkflowNodeDO.getWorkflowStep());
        workflowLinkDetailDO.setWorkflowCurrentNodeId(thisWorkflowNodeDO.getId());
        if (workflowNodeDOList.size() > 1) {
            workflowLinkDetailDO.setWorkflowNextNodeId(workflowNodeDOList.get(1).getId());
        }
        workflowLinkDetailDO.setVerifyUser(verifyUser);
        workflowLinkDetailMapper.save(workflowLinkDetailDO);
    }
}