package com.lxzl.erp.common.domain.workbench;

import com.lxzl.erp.common.domain.workflow.WorkflowLinkQueryParam;

import java.util.List;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 19:02 2018/7/18
 * @Modified By:
 */
public class WorkbenchWorkflowQueryParam {
    private List<WorkflowLinkQueryParam> workflowLinkQueryParamList;

    public List<WorkflowLinkQueryParam> getWorkflowLinkQueryParamList() {
        return workflowLinkQueryParamList;
    }

    public void setWorkflowLinkQueryParamList(List<WorkflowLinkQueryParam> workflowLinkQueryParamList) {
        this.workflowLinkQueryParamList = workflowLinkQueryParamList;
    }
}