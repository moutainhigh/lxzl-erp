package com.lxzl.erp.core.service.workflow.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.VerifyStatus;
import com.lxzl.erp.common.constant.WorkflowType;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.company.pojo.CompanyDepartmentTree;
import com.lxzl.erp.common.domain.user.DepartmentQueryParam;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.domain.workflow.pojo.WorkflowLink;
import com.lxzl.erp.common.util.ListUtil;
import com.lxzl.erp.core.service.company.CompanyService;
import com.lxzl.erp.core.service.purchase.PurchaseOrderService;
import com.lxzl.erp.core.service.user.UserRoleService;
import com.lxzl.erp.core.service.user.UserService;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.core.service.workflow.impl.support.WorkflowConverter;
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
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private CompanyService companyService;

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> commitWorkFlow(Integer workflowType, Integer workflowReferId, Integer verifyUser) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        Date currentTime = new Date();

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
            WorkflowNodeDO thisWorkflowNodeDO = workflowNodeDOList.get(0);
            if (!verifyVerifyUsers(thisWorkflowNodeDO, verifyUser)) {
                result.setErrorCode(ErrorCode.WORKFLOW_VERIFY_USER_ERROR);
                return result;
            }
            workflowLinkId = generateWorkflowLink(workflowTemplateDO, workflowReferId, verifyUser, currentTime);
        } else {
            List<WorkflowLinkDetailDO> workflowLinkDetailDOList = workflowLinkDO.getWorkflowLinkDetailDOList();
            WorkflowLinkDetailDO lastWorkflowLinkDetailDO = workflowLinkDetailDOList.get(workflowLinkDetailDOList.size() - 1);
            WorkflowNodeDO thisWorkflowNodeDO = workflowNodeMapper.findById(lastWorkflowLinkDetailDO.getWorkflowNextNodeId());
            if (!verifyVerifyUsers(thisWorkflowNodeDO, verifyUser)) {
                result.setErrorCode(ErrorCode.WORKFLOW_VERIFY_USER_ERROR);
                return result;
            }
            workflowLinkId = continueWorkflowLink(workflowLinkDO, verifyUser, currentTime);
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(workflowLinkId);
        return result;
    }

    @Override
    public ServiceResult<String, List<User>> getNextVerifyUsers(Integer workflowType, Integer workflowReferId) {
        ServiceResult<String, List<User>> result = new ServiceResult<>();
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
        List<User> userList = getUserListByNode(workflowNodeDO);

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(userList);
        return result;
    }

    @Override
    public ServiceResult<String, WorkflowLink> getWorkflowLink(Integer workflowType, Integer workflowReferId) {
        ServiceResult<String, WorkflowLink> result = new ServiceResult<>();
        if (workflowType == null || workflowReferId == null) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_ENOUGH);
            return result;
        }

        WorkflowLinkDO workflowLinkDO = workflowLinkMapper.findByWorkflowTypeAndReferId(workflowType, workflowReferId);
        if (workflowLinkDO == null) {
            result.setErrorCode(ErrorCode.WORKFLOW_LINK_NOT_EXISTS);
            return result;
        }
        result.setResult(WorkflowConverter.convertWorkflowLinkDO(workflowLinkDO));
        result.setErrorCode(ErrorCode.SUCCESS);
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
        lastWorkflowLinkDetailDO.setUpdateUser(loginUser.getUserId().toString());
        lastWorkflowLinkDetailDO.setUpdateTime(currentTime);
        workflowLinkDetailMapper.update(lastWorkflowLinkDetailDO);

        boolean thisWorkflowStep = false;           // 是否是当前步骤
        WorkflowNodeDO thisWorkflowNodeDO = null;       // 本步骤节点
        WorkflowNodeDO nextWorkflowNodeDO = null;       // 下一步节点
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

        if (VerifyStatus.VERIFY_STATUS_PASS.equals(verifyStatus)) {
            if (nextWorkflowNodeDO != null) {
                workflowLinkDO.setCurrentVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);

                WorkflowLinkDetailDO workflowLinkDetailDO = new WorkflowLinkDetailDO();
                workflowLinkDetailDO.setWorkflowLinkId(workflowLinkDO.getId());
                workflowLinkDetailDO.setWorkflowReferId(lastWorkflowLinkDetailDO.getWorkflowReferId());
                workflowLinkDetailDO.setWorkflowStep(nextWorkflowNodeDO.getWorkflowStep());
                workflowLinkDetailDO.setWorkflowPreviousNodeId(lastWorkflowLinkDetailDO.getId());
                workflowLinkDetailDO.setWorkflowCurrentNodeId(thisWorkflowNodeDO.getId());
                workflowLinkDetailDO.setWorkflowNextNodeId(nextWorkflowNodeDO.getId());
                workflowLinkDetailDO.setVerifyUser(nextVerifyUser);
                workflowLinkDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                workflowLinkDetailDO.setUpdateUser(loginUser.getUserId().toString());
                workflowLinkDetailDO.setCreateUser(loginUser.getUserId().toString());
                workflowLinkDetailDO.setUpdateTime(currentTime);
                workflowLinkDetailDO.setCreateTime(currentTime);
                workflowLinkDetailMapper.save(workflowLinkDetailDO);
            } else {
                workflowLinkDO.setCurrentVerifyStatus(VerifyStatus.VERIFY_STATUS_PASS);
            }
            workflowLinkDO.setCurrentVerifyUser(nextVerifyUser);
        } else {
            workflowLinkDO.setCurrentVerifyUser(null);
            workflowLinkDO.setCurrentVerifyStatus(VerifyStatus.VERIFY_STATUS_BACK);
        }

        // 拒绝或者最后一步，发通知
        if (VerifyStatus.VERIFY_STATUS_BACK.equals(workflowLinkDO.getCurrentVerifyStatus()) || VerifyStatus.VERIFY_STATUS_PASS.equals(workflowLinkDO.getCurrentVerifyStatus())) {
            // 根据不同业务，回调业务系统
            if (WorkflowType.WORKFLOW_TYPE_PURCHASE.equals(workflowLinkDO.getWorkflowType())) {
                boolean receiveResult = purchaseOrderService.receiveVerifyResult(VerifyStatus.VERIFY_STATUS_PASS.equals(verifyStatus), workflowLinkDO.getWorkflowReferId());
                if (!receiveResult) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  // 回滚
                    result.setErrorCode(ErrorCode.SYSTEM_ERROR);
                    return result;
                }
            }
        }
        workflowLinkDO.setUpdateUser(loginUser.getUserId().toString());
        workflowLinkDO.setUpdateTime(currentTime);
        workflowLinkMapper.update(workflowLinkDO);

        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }


    /**
     * 生成工作流线，只适用于首次创建
     */
    private Integer generateWorkflowLink(WorkflowTemplateDO workflowTemplateDO, Integer workflowReferId, Integer verifyUser, Date currentTime) {
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        if (workflowTemplateDO == null) {
            return null;
        }
        List<WorkflowNodeDO> workflowNodeDOList = workflowTemplateDO.getWorkflowNodeDOList();
        if (workflowNodeDOList == null || workflowNodeDOList.isEmpty()) {
            return null;
        }
        WorkflowNodeDO thisWorkflowNodeDO = workflowNodeDOList.get(0);
        WorkflowNodeDO lastWorkflowNodeDO = workflowNodeDOList.get(workflowNodeDOList.size() - 1);

        WorkflowLinkDO workflowLinkDO = new WorkflowLinkDO();
        workflowLinkDO.setWorkflowType(workflowTemplateDO.getWorkflowType());
        workflowLinkDO.setWorkflowTemplateId(workflowTemplateDO.getId());
        workflowLinkDO.setWorkflowReferId(workflowReferId);
        workflowLinkDO.setWorkflowStep(thisWorkflowNodeDO.getWorkflowStep());
        workflowLinkDO.setWorkflowLastStep(lastWorkflowNodeDO.getWorkflowStep());
        workflowLinkDO.setWorkflowCurrentNodeId(thisWorkflowNodeDO.getId());
        workflowLinkDO.setCurrentVerifyUser(verifyUser);
        workflowLinkDO.setCurrentVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
        workflowLinkDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        workflowLinkDO.setUpdateUser(loginUser.getUserId().toString());
        workflowLinkDO.setCreateUser(loginUser.getUserId().toString());
        workflowLinkDO.setUpdateTime(currentTime);
        workflowLinkDO.setCreateTime(currentTime);
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
        workflowLinkDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        workflowLinkDetailDO.setUpdateUser(loginUser.getUserId().toString());
        workflowLinkDetailDO.setCreateUser(loginUser.getUserId().toString());
        workflowLinkDetailDO.setUpdateTime(currentTime);
        workflowLinkDetailDO.setCreateTime(currentTime);
        workflowLinkDetailMapper.save(workflowLinkDetailDO);

        return workflowLinkDO.getId();
    }

    /**
     * 继续工作流
     */
    private Integer continueWorkflowLink(WorkflowLinkDO oldWorkflowLinkDO, Integer verifyUser, Date currentTime) {
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        if (oldWorkflowLinkDO == null) {
            return null;
        }
        List<WorkflowLinkDetailDO> workflowLinkDetailDOList = oldWorkflowLinkDO.getWorkflowLinkDetailDOList();
        if (workflowLinkDetailDOList == null || workflowLinkDetailDOList.isEmpty()) {
            return null;
        }

        WorkflowLinkDetailDO lastWorkflowLinkDetailDO = workflowLinkDetailDOList.get(workflowLinkDetailDOList.size() - 1);
        if (!VerifyStatus.VERIFY_STATUS_BACK.equals(lastWorkflowLinkDetailDO.getVerifyStatus())) {
            return null;
        }


        WorkflowTemplateDO workflowTemplateDO = workflowTemplateMapper.findById(oldWorkflowLinkDO.getWorkflowTemplateId());
        if (workflowTemplateDO == null) {
            return null;
        }
        List<WorkflowNodeDO> workflowNodeDOList = workflowTemplateDO.getWorkflowNodeDOList();
        if (workflowNodeDOList == null || workflowNodeDOList.isEmpty()) {
            return null;
        }
        WorkflowLinkDO workflowLinkDO = new WorkflowLinkDO();
        workflowLinkDO.setWorkflowType(oldWorkflowLinkDO.getWorkflowType());
        workflowLinkDO.setWorkflowTemplateId(oldWorkflowLinkDO.getWorkflowTemplateId());
        workflowLinkDO.setWorkflowReferId(oldWorkflowLinkDO.getWorkflowReferId());
        workflowLinkDO.setWorkflowStep(oldWorkflowLinkDO.getWorkflowStep());
        workflowLinkDO.setWorkflowLastStep(oldWorkflowLinkDO.getWorkflowLastStep());
        workflowLinkDO.setWorkflowCurrentNodeId(oldWorkflowLinkDO.getWorkflowCurrentNodeId());
        workflowLinkDO.setCurrentVerifyUser(verifyUser);
        workflowLinkDO.setCurrentVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
        workflowLinkDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        workflowLinkDO.setUpdateUser(loginUser.getUserId().toString());
        workflowLinkDO.setCreateUser(loginUser.getUserId().toString());
        workflowLinkDO.setUpdateTime(currentTime);
        workflowLinkDO.setCreateTime(currentTime);
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
        workflowLinkDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        workflowLinkDetailDO.setUpdateUser(loginUser.getUserId().toString());
        workflowLinkDetailDO.setCreateUser(loginUser.getUserId().toString());
        workflowLinkDetailDO.setUpdateTime(currentTime);
        workflowLinkDetailDO.setCreateTime(currentTime);
        workflowLinkDetailMapper.save(workflowLinkDetailDO);

        return workflowLinkDO.getId();
    }

    private boolean verifyVerifyUsers(WorkflowNodeDO workflowNodeDO, Integer userId) {
        List<User> userList = getUserListByNode(workflowNodeDO);
        if (userList != null && !userList.isEmpty()) {
            Map<Integer, User> userMap = ListUtil.listToMap(userList, "userId");
            if (userMap.containsKey(userId)) {
                return true;
            }
        }
        return false;
    }

    private List<User> getUserListByNode(WorkflowNodeDO workflowNodeDO) {
        List<User> userList = new ArrayList<>();
        if (workflowNodeDO == null) {
            return userList;
        }
        if (workflowNodeDO.getWorkflowUser() != null) {
            ServiceResult<String, User> userResult = userService.getUserById(workflowNodeDO.getWorkflowUser());
            if (ErrorCode.SUCCESS.equals(userResult.getErrorCode())) {
                userList.add(userResult.getResult());
            }
        } else if (workflowNodeDO.getWorkflowRole() != null) {
            ServiceResult<String, List<User>> userResult = userRoleService.getUserByRoleId(workflowNodeDO.getWorkflowRole());
            if (ErrorCode.SUCCESS.equals(userResult.getErrorCode())) {
                userList = userResult.getResult();
            }
        } else if (workflowNodeDO.getWorkflowDepartment() != null) {
            DepartmentQueryParam departmentQueryParam = new DepartmentQueryParam();
            departmentQueryParam.setDepartmentId(workflowNodeDO.getWorkflowDepartment());
            ServiceResult<String, List<User>> departmentTreeServiceResult = userService.getUserByDepartmentId(workflowNodeDO.getWorkflowDepartment());
            if (ErrorCode.SUCCESS.equals(departmentTreeServiceResult.getErrorCode())) {
                userList = departmentTreeServiceResult.getResult();
            }
        }

        return userList;
    }
}
