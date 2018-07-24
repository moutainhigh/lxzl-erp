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
    private List<Integer> workflowLinkStatusList;

    public List<Integer> getWorkflowLinkStatusList() {
        return workflowLinkStatusList;
    }

    public void setWorkflowLinkStatusList(List<Integer> workflowLinkStatusList) {
        this.workflowLinkStatusList = workflowLinkStatusList;
    }
}