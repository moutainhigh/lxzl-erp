package com.lxzl.erp.core.service.workflow.impl;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.user.UserQueryParam;
import com.lxzl.erp.common.domain.user.pojo.Role;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.domain.workflow.WorkflowLinkQueryParam;
import com.lxzl.erp.common.domain.workflow.pojo.WorkflowLink;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.common.util.GenerateNoUtil;
import com.lxzl.erp.common.util.ListUtil;
import com.lxzl.erp.core.service.VerifyReceiver;
import com.lxzl.erp.core.service.message.MessageService;
import com.lxzl.erp.core.service.user.UserService;
import com.lxzl.erp.core.service.workflow.WorkFlowManager;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.dataaccess.dao.mysql.workflow.WorkflowLinkDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.workflow.WorkflowLinkMapper;
import com.lxzl.erp.dataaccess.dao.mysql.workflow.WorkflowNodeMapper;
import com.lxzl.erp.dataaccess.dao.mysql.workflow.WorkflowTemplateMapper;
import com.lxzl.erp.dataaccess.domain.workflow.WorkflowLinkDO;
import com.lxzl.erp.dataaccess.domain.workflow.WorkflowLinkDetailDO;
import com.lxzl.erp.dataaccess.domain.workflow.WorkflowNodeDO;
import com.lxzl.erp.dataaccess.domain.workflow.WorkflowTemplateDO;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.servlet.http.HttpSession;
import java.util.*;

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
    private UserService userService;

    @Autowired(required = false)
    private HttpSession session;

    @Autowired
    private WorkFlowManager workFlowManager;

    @Autowired
    private MessageService messageService;

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> commitWorkFlow(Integer workflowType, String workflowReferNo, Integer verifyUser, String commitRemark) {
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
        WorkflowNodeDO thisWorkflowNodeDO = workflowNodeDOList.get(0);
        if (!verifyVerifyUsers(thisWorkflowNodeDO, verifyUser)) {
            result.setErrorCode(ErrorCode.WORKFLOW_VERIFY_USER_ERROR);
            return result;
        }

        String workflowLinkNo;
        WorkflowLinkDO workflowLinkDO = workflowLinkMapper.findByWorkflowTypeAndReferNo(workflowType, workflowReferNo);
        if (workflowLinkDO == null) {
            workflowLinkNo = generateWorkflowLink(workflowTemplateDO, workflowReferNo, commitRemark, verifyUser, currentTime);
        } else {
            String errorCode = continueWorkflowLink(workflowLinkDO, commitRemark, verifyUser, currentTime);
            if (!ErrorCode.SUCCESS.equals(errorCode)) {
                result.setErrorCode(errorCode);
                return result;
            }
            workflowLinkNo = workflowLinkDO.getWorkflowLinkNo();
        }

        messageService.superSendMessage(MessageContant.WORKFLOW_COMMIT_TITLE, MessageContant.WORKFLOW_COMMIT_CONTENT, verifyUser);

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(workflowLinkNo);
        return result;
    }

    @Override
    public ServiceResult<String, List<User>> getNextVerifyUsers(Integer workflowType, String workflowReferNo) {
        ServiceResult<String, List<User>> result = new ServiceResult<>();
        if (workflowType == null
                || StringUtil.isBlank(workflowReferNo)) {
            result.setErrorCode(ErrorCode.PARAM_IS_NOT_NULL);
            return result;
        }
        WorkflowLinkDO workflowLinkDO = workflowLinkMapper.findByWorkflowTypeAndReferNo(workflowType, workflowReferNo);
        WorkflowNodeDO workflowNodeDO;
        if (workflowLinkDO == null) {
            WorkflowTemplateDO workflowTemplateDO = workflowTemplateMapper.findByWorkflowType(workflowType);
            if (workflowTemplateDO == null || CollectionUtil.isEmpty(workflowTemplateDO.getWorkflowNodeDOList())) {
                result.setErrorCode(ErrorCode.WORKFLOW_LINK_NOT_EXISTS);
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
                // 如果 最后是驳回状态，审核人就要从头来
                WorkflowTemplateDO workflowTemplateDO = workflowTemplateMapper.findByWorkflowType(workflowType);
                if (workflowTemplateDO == null || CollectionUtil.isEmpty(workflowTemplateDO.getWorkflowNodeDOList())) {
                    result.setErrorCode(ErrorCode.WORKFLOW_LINK_NOT_EXISTS);
                    return result;
                }
                workflowNodeDO = workflowTemplateDO.getWorkflowNodeDOList().get(0);
            } else {
                workflowNodeDO = workflowNodeMapper.findById(lastWorkflowLinkDetailDO.getWorkflowNextNodeId());
            }
        }

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
        result.setResult(ConverterUtil.convert(workflowLinkDO, WorkflowLink.class));
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

        WorkflowLinkDO workflowLinkDO = workflowLinkMapper.findByWorkflowTypeAndReferNo(workflowType, workflowReferNo);
        if (workflowLinkDO == null) {
            result.setErrorCode(ErrorCode.WORKFLOW_LINK_NOT_EXISTS);
            return result;
        }
        result.setResult(ConverterUtil.convert(workflowLinkDO, WorkflowLink.class));
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, Page<WorkflowLink>> getWorkflowLinkPage(WorkflowLinkQueryParam workflowLinkQueryParam) {
        ServiceResult<String, Page<WorkflowLink>> result = new ServiceResult<>();

        PageQuery pageQuery = new PageQuery(workflowLinkQueryParam.getPageNo(), workflowLinkQueryParam.getPageSize());
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("start", pageQuery.getStart());
        paramMap.put("pageSize", pageQuery.getPageSize());
        paramMap.put("workflowQueryParam", workflowLinkQueryParam);
        Integer dataCount = workflowLinkMapper.listCount(paramMap);
        List<WorkflowLinkDO> dataList = workflowLinkMapper.listPage(paramMap);
        Page<WorkflowLink> page = new Page<>(ConverterUtil.convertList(dataList, WorkflowLink.class), dataCount, workflowLinkQueryParam.getPageNo(), workflowLinkQueryParam.getPageSize());

        result.setResult(page);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, Page<WorkflowLink>> getCurrentUserWorkflowLinkPage(WorkflowLinkQueryParam workflowLinkQueryParam) {
        ServiceResult<String, Page<WorkflowLink>> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        workflowLinkQueryParam.setCurrentVerifyUser(loginUser.getUserId());
        workflowLinkQueryParam.setVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
        PageQuery pageQuery = new PageQuery(workflowLinkQueryParam.getPageNo(), workflowLinkQueryParam.getPageSize());
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("start", pageQuery.getStart());
        paramMap.put("pageSize", pageQuery.getPageSize());
        paramMap.put("workflowQueryParam", workflowLinkQueryParam);
        Integer dataCount = workflowLinkMapper.listCount(paramMap);
        List<WorkflowLinkDO> dataList = workflowLinkMapper.listPage(paramMap);
        Page<WorkflowLink> page = new Page<>(ConverterUtil.convertList(dataList, WorkflowLink.class), dataCount, workflowLinkQueryParam.getPageNo(), workflowLinkQueryParam.getPageSize());

        result.setResult(page);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;

    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, Integer> verifyWorkFlow(String workflowLinkNo, Integer verifyStatus, Integer returnType, String verifyOpinion, Integer nextVerifyUser) {
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Date currentTime = new Date();

        ServiceResult<String, Integer> result = new ServiceResult<>();
        WorkflowLinkDO workflowLinkDO = workflowLinkMapper.findByNo(workflowLinkNo);
        if (workflowLinkDO == null) {
            result.setErrorCode(ErrorCode.WORKFLOW_LINK_NOT_EXISTS);
            return result;
        }

        List<WorkflowLinkDetailDO> workflowLinkDetailDOList = workflowLinkDO.getWorkflowLinkDetailDOList();
        if (workflowLinkDetailDOList == null || workflowLinkDetailDOList.isEmpty()) {
            result.setErrorCode(ErrorCode.WORKFLOW_LINK_HAVE_NO_DETAIL);
            return result;
        }

        WorkflowLinkDetailDO lastWorkflowLinkDetailDO = workflowLinkDetailDOList.get(0);
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
        // 如果审核通过并且下一步审核不为空的时候，判断下一步的审核人是否正确
        if (VerifyStatus.VERIFY_STATUS_PASS.equals(verifyStatus) && nextWorkflowNodeDO != null) {
            if (!verifyVerifyUsers(nextWorkflowNodeDO, nextVerifyUser)) {
                result.setErrorCode(ErrorCode.WORKFLOW_VERIFY_USER_ERROR);
                return result;
            }
        } else {
            lastWorkflowLinkDetailDO.setWorkflowCurrentNodeId(0);
        }

        lastWorkflowLinkDetailDO.setVerifyStatus(verifyStatus);
        lastWorkflowLinkDetailDO.setVerifyTime(currentTime);
        lastWorkflowLinkDetailDO.setVerifyOpinion(verifyOpinion);
        lastWorkflowLinkDetailDO.setUpdateUser(loginUser.getUserId().toString());
        lastWorkflowLinkDetailDO.setUpdateTime(currentTime);
        workflowLinkDetailMapper.update(lastWorkflowLinkDetailDO);

        if (VerifyStatus.VERIFY_STATUS_PASS.equals(verifyStatus)) {

            // 审核通过并且有下一步的情况
            if (nextWorkflowNodeDO != null) {
                workflowLinkDO.setCurrentVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);

                WorkflowLinkDetailDO workflowLinkDetailDO = new WorkflowLinkDetailDO();
                workflowLinkDetailDO.setWorkflowLinkId(workflowLinkDO.getId());
                workflowLinkDetailDO.setWorkflowReferNo(lastWorkflowLinkDetailDO.getWorkflowReferNo());
                workflowLinkDetailDO.setWorkflowStep(nextWorkflowNodeDO.getWorkflowStep());
                workflowLinkDetailDO.setWorkflowPreviousNodeId(lastWorkflowLinkDetailDO.getWorkflowCurrentNodeId());
                workflowLinkDetailDO.setWorkflowCurrentNodeId(nextWorkflowNodeDO.getId());
                workflowLinkDetailDO.setWorkflowNextNodeId(nextWorkflowNodeDO.getWorkflowNextNodeId());
                workflowLinkDetailDO.setVerifyUser(nextVerifyUser);
                workflowLinkDetailDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
                workflowLinkDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                workflowLinkDetailDO.setUpdateUser(loginUser.getUserId().toString());
                workflowLinkDetailDO.setCreateUser(loginUser.getUserId().toString());
                workflowLinkDetailDO.setUpdateTime(currentTime);
                workflowLinkDetailDO.setCreateTime(currentTime);
                workflowLinkDetailMapper.save(workflowLinkDetailDO);
                workflowLinkDO.setWorkflowStep(nextWorkflowNodeDO.getWorkflowStep());
                messageService.superSendMessage(MessageContant.WORKFLOW_COMMIT_TITLE, MessageContant.WORKFLOW_COMMIT_CONTENT, nextVerifyUser);
            } else {
                workflowLinkDO.setCurrentVerifyStatus(VerifyStatus.VERIFY_STATUS_PASS);
                noticeBusinessModule = true;
            }
            workflowLinkDO.setCurrentVerifyUser(nextVerifyUser);
        } else {
            workflowLinkDO.setCurrentVerifyUser(null);
            workflowLinkDO.setCurrentVerifyStatus(VerifyStatus.VERIFY_STATUS_BACK);
            // 拒绝并且一步驳回到底
            if (returnType == null || WorkflowReturnType.RETURN_TYPE_ROOT.equals(returnType)) {
                noticeBusinessModule = true;
                workflowLinkDO.setWorkflowStep(0);
                workflowLinkDO.setCurrentVerifyUser(CommonConstant.SUPER_USER_ID);
            } else if (previousWorkflowNodeDO != null) {
                WorkflowLinkDetailDO workflowLinkDetailDO = new WorkflowLinkDetailDO();
                if (workflowLinkDetailDOList.size() > 1) {
                    WorkflowLinkDetailDO previousWorkflowLinkDetailDO = workflowLinkDetailDOList.get(1);
                    workflowLinkDetailDO.setVerifyUser(previousWorkflowLinkDetailDO.getVerifyUser());
                    workflowLinkDO.setCurrentVerifyUser(previousWorkflowLinkDetailDO.getVerifyUser());
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
                workflowLinkDetailDO.setUpdateUser(loginUser.getUserId().toString());
                workflowLinkDetailDO.setCreateUser(loginUser.getUserId().toString());
                workflowLinkDetailDO.setUpdateTime(currentTime);
                workflowLinkDetailDO.setCreateTime(currentTime);
                workflowLinkDetailMapper.save(workflowLinkDetailDO);
                workflowLinkDO.setWorkflowStep(previousWorkflowNodeDO.getWorkflowStep());
                messageService.superSendMessage(MessageContant.WORKFLOW_VERIFY_BACK_TITLE, MessageContant.WORKFLOW_VERIFY_BACK_CONTENT, workflowLinkDetailDO.getVerifyUser());
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
                result.setErrorCode(ErrorCode.WORKFLOW_LINK_NOT_EXISTS);
                return result;
            }
            boolean receiveResult = verifyReceiver.receiveVerifyResult(VerifyStatus.VERIFY_STATUS_PASS.equals(verifyStatus), workflowLinkDO.getWorkflowReferNo());
            if (!receiveResult) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  // 回滚
                result.setErrorCode(ErrorCode.SYSTEM_ERROR);
                return result;
            }
        }
        workflowLinkDO.setUpdateUser(loginUser.getUserId().toString());
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
    private String generateWorkflowLink(WorkflowTemplateDO workflowTemplateDO, String workflowReferNo, String commitRemark, Integer verifyUser, Date currentTime) {
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
        workflowLinkDO.setWorkflowLinkNo(GenerateNoUtil.generateWorkflowLinkNo(currentTime));
        workflowLinkDO.setWorkflowType(workflowTemplateDO.getWorkflowType());
        workflowLinkDO.setWorkflowTemplateId(workflowTemplateDO.getId());
        workflowLinkDO.setWorkflowReferNo(workflowReferNo);
        workflowLinkDO.setWorkflowStep(thisWorkflowNodeDO.getWorkflowStep());
        workflowLinkDO.setWorkflowLastStep(lastWorkflowNodeDO.getWorkflowStep());
        workflowLinkDO.setWorkflowCurrentNodeId(thisWorkflowNodeDO.getId());
        workflowLinkDO.setCommitUser(loginUser.getUserId());
        workflowLinkDO.setCurrentVerifyUser(verifyUser);
        workflowLinkDO.setCurrentVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
        workflowLinkDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        workflowLinkDO.setUpdateUser(loginUser.getUserId().toString());
        workflowLinkDO.setCreateUser(loginUser.getUserId().toString());
        workflowLinkDO.setUpdateTime(currentTime);
        workflowLinkDO.setCreateTime(currentTime);
        workflowLinkMapper.save(workflowLinkDO);

        // 生成提交人工作流
        WorkflowLinkDetailDO commitWorkflowLinkDetailDO = new WorkflowLinkDetailDO();
        commitWorkflowLinkDetailDO.setWorkflowLinkId(workflowLinkDO.getId());
        commitWorkflowLinkDetailDO.setWorkflowReferNo(workflowReferNo);
        commitWorkflowLinkDetailDO.setWorkflowCurrentNodeId(0);
        commitWorkflowLinkDetailDO.setWorkflowStep(0);
        commitWorkflowLinkDetailDO.setWorkflowNextNodeId(thisWorkflowNodeDO.getId());
        commitWorkflowLinkDetailDO.setVerifyUser(loginUser.getUserId());
        commitWorkflowLinkDetailDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_PASS);
        commitWorkflowLinkDetailDO.setVerifyOpinion(commitRemark);
        commitWorkflowLinkDetailDO.setVerifyTime(currentTime);
        commitWorkflowLinkDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        commitWorkflowLinkDetailDO.setUpdateUser(loginUser.getUserId().toString());
        commitWorkflowLinkDetailDO.setCreateUser(loginUser.getUserId().toString());
        commitWorkflowLinkDetailDO.setUpdateTime(currentTime);
        commitWorkflowLinkDetailDO.setCreateTime(currentTime);
        workflowLinkDetailMapper.save(commitWorkflowLinkDetailDO);

        // 生成审批人工作流
        WorkflowLinkDetailDO workflowLinkDetailDO = new WorkflowLinkDetailDO();
        workflowLinkDetailDO.setWorkflowLinkId(workflowLinkDO.getId());
        workflowLinkDetailDO.setWorkflowReferNo(workflowReferNo);
        workflowLinkDetailDO.setWorkflowStep(thisWorkflowNodeDO.getWorkflowStep());
        workflowLinkDetailDO.setWorkflowCurrentNodeId(thisWorkflowNodeDO.getId());
        if (workflowNodeDOList.size() > 1) {
            WorkflowNodeDO nextWorkflowNodeDO = workflowNodeDOList.get(1);
            workflowLinkDetailDO.setWorkflowNextNodeId(nextWorkflowNodeDO.getId());
        }
        workflowLinkDetailDO.setVerifyUser(verifyUser);
        workflowLinkDetailDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
        workflowLinkDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        workflowLinkDetailDO.setUpdateUser(loginUser.getUserId().toString());
        workflowLinkDetailDO.setCreateUser(loginUser.getUserId().toString());
        workflowLinkDetailDO.setUpdateTime(currentTime);
        workflowLinkDetailDO.setCreateTime(currentTime);
        workflowLinkDetailMapper.save(workflowLinkDetailDO);

        return workflowLinkDO.getWorkflowLinkNo();
    }

    /**
     * 继续工作流
     */
    private String continueWorkflowLink(WorkflowLinkDO workflowLinkDO, String commitRemark, Integer verifyUser, Date currentTime) {
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
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
        workflowLinkMapper.update(workflowLinkDO);

        // 生成提交人工作流
        WorkflowLinkDetailDO commitWorkflowLinkDetailDO = new WorkflowLinkDetailDO();
        commitWorkflowLinkDetailDO.setWorkflowLinkId(workflowLinkDO.getId());
        commitWorkflowLinkDetailDO.setWorkflowReferNo(workflowLinkDO.getWorkflowReferNo());
        commitWorkflowLinkDetailDO.setWorkflowCurrentNodeId(0);
        commitWorkflowLinkDetailDO.setWorkflowStep(0);
        commitWorkflowLinkDetailDO.setWorkflowNextNodeId(thisWorkflowNodeDO.getId());
        commitWorkflowLinkDetailDO.setVerifyUser(loginUser.getUserId());
        commitWorkflowLinkDetailDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_PASS);
        commitWorkflowLinkDetailDO.setVerifyOpinion(commitRemark);
        commitWorkflowLinkDetailDO.setVerifyTime(currentTime);
        commitWorkflowLinkDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        commitWorkflowLinkDetailDO.setUpdateUser(loginUser.getUserId().toString());
        commitWorkflowLinkDetailDO.setCreateUser(loginUser.getUserId().toString());
        commitWorkflowLinkDetailDO.setUpdateTime(currentTime);
        commitWorkflowLinkDetailDO.setCreateTime(currentTime);
        workflowLinkDetailMapper.save(commitWorkflowLinkDetailDO);

        WorkflowLinkDetailDO workflowLinkDetailDO = new WorkflowLinkDetailDO();
        workflowLinkDetailDO.setWorkflowLinkId(workflowLinkDO.getId());
        workflowLinkDetailDO.setWorkflowReferNo(lastWorkflowLinkDetailDO.getWorkflowReferNo());
        workflowLinkDetailDO.setWorkflowStep(thisWorkflowNodeDO.getWorkflowStep());
        workflowLinkDetailDO.setWorkflowCurrentNodeId(thisWorkflowNodeDO.getId());
        if (workflowNodeDOList.size() > 1) {
            workflowLinkDetailDO.setWorkflowNextNodeId(workflowNodeDOList.get(1).getId());
        }
        workflowLinkDetailDO.setVerifyUser(verifyUser);
        workflowLinkDetailDO.setVerifyStatus(VerifyStatus.VERIFY_STATUS_COMMIT);
        workflowLinkDetailDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        workflowLinkDetailDO.setUpdateUser(loginUser.getUserId().toString());
        workflowLinkDetailDO.setCreateUser(loginUser.getUserId().toString());
        workflowLinkDetailDO.setUpdateTime(currentTime);
        workflowLinkDetailDO.setCreateTime(currentTime);
        workflowLinkDetailMapper.save(workflowLinkDetailDO);

        return ErrorCode.SUCCESS;
    }

    private boolean verifyVerifyUsers(WorkflowNodeDO workflowNodeDO, Integer userId) {
        List<User> userList = getUserListByNode(workflowNodeDO);
        if (CollectionUtil.isNotEmpty(userList)) {
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
        UserQueryParam userQueryParam = new UserQueryParam();
        if (workflowNodeDO.getWorkflowUser() != null) {
            ServiceResult<String, User> userResult = userService.getUserById(workflowNodeDO.getWorkflowUser());
            if (ErrorCode.SUCCESS.equals(userResult.getErrorCode())) {
                userList.add(userResult.getResult());
            }
        } else if (workflowNodeDO.getWorkflowRole() != null) {
            userQueryParam.setRoleId(workflowNodeDO.getWorkflowRole());
            ServiceResult<String, List<User>> userResult = userService.getUserListByParam(userQueryParam);
            if (ErrorCode.SUCCESS.equals(userResult.getErrorCode())) {
                userList = userResult.getResult();
            }
        } else if (workflowNodeDO.getWorkflowDepartment() != null) {
            userQueryParam.setDepartmentId(workflowNodeDO.getWorkflowDepartment());
            ServiceResult<String, List<User>> departmentUserResult = userService.getUserListByParam(userQueryParam);
            if (ErrorCode.SUCCESS.equals(departmentUserResult.getErrorCode())) {
                userList = departmentUserResult.getResult();
            }
        } else if (workflowNodeDO.getWorkflowDepartmentType() != null) {
            userQueryParam.setDepartmentType(workflowNodeDO.getWorkflowDepartmentType());
            User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
            List<Integer> subCompanyIdList = new ArrayList<>();
            if (CollectionUtil.isNotEmpty(loginUser.getRoleList())) {
                for (Role role : loginUser.getRoleList()) {
                    subCompanyIdList.add(role.getSubCompanyId());
                }
            }
            userQueryParam.setSubCompanyIdList(subCompanyIdList);
            ServiceResult<String, List<User>> subCompanyUserResult = userService.getUserListByParam(userQueryParam);
            if (ErrorCode.SUCCESS.equals(subCompanyUserResult.getErrorCode())) {
                userList = subCompanyUserResult.getResult();
            }
        }

        return userList;
    }
}
