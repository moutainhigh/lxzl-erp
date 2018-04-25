package com.lxzl.erp.dataaccess.domain.workflow;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

import java.util.List;


public class WorkflowTemplateDO extends BaseDO {

    private Integer id;
    private String workflowName;
    private String workflowDesc;
    private Integer workflowType;
    private Integer dataStatus;
    private String remark;
    private String dingdingProcessCode;   //钉钉模板代码
    @Transient
    private List<WorkflowNodeDO> workflowNodeDOList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

    public String getWorkflowDesc() {
        return workflowDesc;
    }

    public void setWorkflowDesc(String workflowDesc) {
        this.workflowDesc = workflowDesc;
    }

    public Integer getWorkflowType() {
        return workflowType;
    }

    public void setWorkflowType(Integer workflowType) {
        this.workflowType = workflowType;
    }

    public Integer getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDingdingProcessCode() {
        return dingdingProcessCode;
    }

    public void setDingdingProcessCode(String dingdingProcessCode) {
        this.dingdingProcessCode = dingdingProcessCode;
    }

    public List<WorkflowNodeDO> getWorkflowNodeDOList() {
        return workflowNodeDOList;
    }

    public void setWorkflowNodeDOList(List<WorkflowNodeDO> workflowNodeDOList) {
        this.workflowNodeDOList = workflowNodeDOList;
    }

}