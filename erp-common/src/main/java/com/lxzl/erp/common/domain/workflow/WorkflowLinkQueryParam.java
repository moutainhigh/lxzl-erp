package com.lxzl.erp.common.domain.workflow;

import com.lxzl.erp.common.domain.base.BasePageParam;

import java.io.Serializable;

/**
 * 描述: 查询工作流
 *
 * @author gaochao
 * @date 2017-11-16 10:45
 */
public class WorkflowLinkQueryParam extends BasePageParam implements Serializable {

    private Integer workflowType;
    private Integer workflowReferId;

    public Integer getWorkflowType() {
        return workflowType;
    }

    public void setWorkflowType(Integer workflowType) {
        this.workflowType = workflowType;
    }

    public Integer getWorkflowReferId() {
        return workflowReferId;
    }

    public void setWorkflowReferId(Integer workflowReferId) {
        this.workflowReferId = workflowReferId;
    }
}
