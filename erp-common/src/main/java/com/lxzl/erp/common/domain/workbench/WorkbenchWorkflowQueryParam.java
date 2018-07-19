package com.lxzl.erp.common.domain.workbench;

import java.util.List;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 19:02 2018/7/18
 * @Modified By:
 */
public class WorkbenchWorkflowQueryParam {
    private List<Integer> workflowStatusList;

    public List<Integer> getWorkflowStatusList() {
        return workflowStatusList;
    }

    public void setWorkflowStatusList(List<Integer> workflowStatusList) {
        this.workflowStatusList = workflowStatusList;
    }
}