package com.lxzl.erp.core.service.workflow.impl;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.VerifyStatus;
import com.lxzl.erp.common.domain.ServiceResult;
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

import java.util.ArrayList;
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

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> commitWorkFlow(Integer workflowType, Integer workflowReferId) {
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
            workflowLinkId = generateWorkflowLink(workflowTemplateDO, workflowReferId);
        } else {
            workflowLinkId = workflowLinkDO.getId();
            continueWorkflowLink(workflowLinkDO);
        }


        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(workflowLinkId);
        return result;
    }

    @Override
    public ServiceResult<String, Integer> verifyWorkFlow(Integer workflowLinkId) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        WorkflowLinkDO workflowLinkDO = workflowLinkMapper.findById(workflowLinkId);
        if(workflowLinkDO == null){
            result.setErrorCode(ErrorCode.WORKFLOW_LINK_EXISTS);
        }
        return null;
    }


    /**
     * 生成工作流线，只适用于首次创建
     */
    private Integer generateWorkflowLink(WorkflowTemplateDO workflowTemplateDO, Integer workflowReferId) {
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
            WorkflowNodeDO nextWorkflowNodeDO = workflowNodeDOList.get(0);
            workflowLinkDetailDO.setWorkflowNextNodeId(nextWorkflowNodeDO.getId());
        }
        workflowLinkDetailMapper.save(workflowLinkDetailDO);

        return workflowLinkDO.getId();
    }

    /**
     * 继续工作流
     */
    private void continueWorkflowLink(WorkflowLinkDO workflowLinkDO) {
        if (workflowLinkDO == null) {
            return;
        }
        List<WorkflowLinkDetailDO> workflowLinkDetailDOList = workflowLinkDO.getWorkflowLinkDetailDOList();
        if (workflowLinkDetailDOList == null || workflowLinkDetailDOList.isEmpty()) {
            return;
        }

        WorkflowLinkDetailDO workflowLinkDetailDO = workflowLinkDetailDOList.get(workflowLinkDetailDOList.size() - 1);
        if (VerifyStatus.VERIFY_STATUS_BACK.equals(workflowLinkDetailDO.getVerifyStatus())) {

        }


    }
}
