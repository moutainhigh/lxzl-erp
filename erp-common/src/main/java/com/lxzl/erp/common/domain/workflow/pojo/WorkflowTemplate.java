package com.lxzl.erp.common.domain.workflow.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;

import java.util.Date;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkflowTemplate extends BasePO {

    private Integer workflowTemplateId;   //唯一标识
    private String workflowName;   //工作流名称
    private String workflowDesc;   //工作流描述
    private Integer workflowType;   //工作流类型
    private Integer dataStatus;   //状态：0不可用；1可用；2删除
    private String remark;   //备注
    private Date createTime;   //添加时间
    private String createUser;   //添加人
    private Date updateTime;   //添加时间
    private String updateUser;   //修改人
    private String dingdingProcessCode;   //钉钉模板代码

    private List<WorkflowNode> workflowNodeList;    //工作流节点列表

    public Integer getWorkflowTemplateId() {
        return workflowTemplateId;
    }

    public void setWorkflowTemplateId(Integer workflowTemplateId) {
        this.workflowTemplateId = workflowTemplateId;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getDingdingProcessCode() {
        return dingdingProcessCode;
    }

    public void setDingdingProcessCode(String dingdingProcessCode) {
        this.dingdingProcessCode = dingdingProcessCode;
    }

    public List<WorkflowNode> getWorkflowNodeList() {
        return workflowNodeList;
    }

    public void setWorkflowNodeList(List<WorkflowNode> workflowNodeList) {
        this.workflowNodeList = workflowNodeList;
    }


}