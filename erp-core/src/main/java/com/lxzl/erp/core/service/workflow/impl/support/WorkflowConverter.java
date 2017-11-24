package com.lxzl.erp.core.service.workflow.impl.support;

import com.lxzl.erp.common.domain.workflow.pojo.WorkflowLink;
import com.lxzl.erp.common.domain.workflow.pojo.WorkflowLinkDetail;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.dataaccess.domain.workflow.WorkflowLinkDO;
import com.lxzl.erp.dataaccess.domain.workflow.WorkflowLinkDetailDO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-07 20:36
 */
public class WorkflowConverter {
    public static List<WorkflowLink> convertWorkflowLinkDOList(List<WorkflowLinkDO> workflowLinkDOList){
        List<WorkflowLink> workflowLinkList = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(workflowLinkDOList)){
            for(WorkflowLinkDO workflowLinkDO : workflowLinkDOList){
                workflowLinkList.add(convertWorkflowLinkDO(workflowLinkDO));
            }
        }

        return workflowLinkList;
    }
    public static WorkflowLink convertWorkflowLinkDO(WorkflowLinkDO workflowLinkDO){
        WorkflowLink workflowLink = new WorkflowLink();
        if(workflowLinkDO.getId() != null){
            workflowLink.setWorkflowLinkId(workflowLinkDO.getId());
        }
        if(workflowLinkDO.getWorkflowLinkDetailDOList() != null && !workflowLinkDO.getWorkflowLinkDetailDOList().isEmpty()){
            workflowLink.setWorkflowLinkDetailList(convertWorkflowLinkDetailDOList(workflowLinkDO.getWorkflowLinkDetailDOList()));
        }
        BeanUtils.copyProperties(workflowLinkDO, workflowLink);

        return workflowLink;
    }

    public static List<WorkflowLinkDetail> convertWorkflowLinkDetailDOList(List<WorkflowLinkDetailDO> workflowLinkDetailDOList){
        List<WorkflowLinkDetail> workflowLinkDetailList = new ArrayList<>();
        if(workflowLinkDetailDOList != null && !workflowLinkDetailDOList.isEmpty()){
            for(WorkflowLinkDetailDO workflowLinkDetailDO : workflowLinkDetailDOList){
                workflowLinkDetailList.add(convertWorkflowLinkDetailDO(workflowLinkDetailDO));
            }
        }
        return workflowLinkDetailList;
    }

    public static WorkflowLinkDetail convertWorkflowLinkDetailDO(WorkflowLinkDetailDO workflowLinkDetailDO){
        WorkflowLinkDetail workflowLinkDetail = new WorkflowLinkDetail();
        if(workflowLinkDetailDO.getId() != null){
            workflowLinkDetail.setWorkflowLinkDetailId(workflowLinkDetailDO.getId());
        }
        BeanUtils.copyProperties(workflowLinkDetailDO, workflowLinkDetail);

        return workflowLinkDetail;
    }
}
