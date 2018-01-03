package com.lxzl.erp.common.domain.workflow.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;

import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkflowNode extends BasePO {

    private Integer workflowNodeId;   //唯一标识
    private String workflowNodeName;   //工作流子节点名称
    private Integer workflowTemplateId;   //流程模板ID
    private Integer workflowStep;   //流程步骤
    private Integer workflowPreviousNodeId;    // 上级节点
    private Integer workflowNextNodeId;        // 下级节点
    private Integer workflowDepartmentType;   //本步骤可审批部门类型
    private Integer workflowDepartment;   //本步骤可审批部门
    private Integer workflowRole;   //本步骤可审批角色
    private Integer workflowUser;   //本步骤可审批人员
    private Integer dataStatus;   //状态：0不可用；1可用；2删除
    private String remark;   //备注
    private Date createTime;   //添加时间
    private String createUser;   //添加人
    private Date updateTime;   //添加时间
    private String updateUser;   //修改人

    private String departmentName;//可审批部门名字
    private String departmentTypeName;//可审批部门类型名字
    private String roleName;//可审批角色名字
    private String userName;//可审批人员名字

    public Integer getWorkflowNodeId() {
        return workflowNodeId;
    }

    public void setWorkflowNodeId(Integer workflowNodeId) {
        this.workflowNodeId = workflowNodeId;
    }

    public String getWorkflowNodeName() {
        return workflowNodeName;
    }

    public void setWorkflowNodeName(String workflowNodeName) {
        this.workflowNodeName = workflowNodeName;
    }

    public Integer getWorkflowTemplateId() {
        return workflowTemplateId;
    }

    public void setWorkflowTemplateId(Integer workflowTemplateId) {
        this.workflowTemplateId = workflowTemplateId;
    }

    public Integer getWorkflowStep() {
        return workflowStep;
    }

    public void setWorkflowStep(Integer workflowStep) {
        this.workflowStep = workflowStep;
    }

    public Integer getWorkflowDepartment() {
        return workflowDepartment;
    }

    public void setWorkflowDepartment(Integer workflowDepartment) {
        this.workflowDepartment = workflowDepartment;
    }

    public Integer getWorkflowRole() {
        return workflowRole;
    }

    public void setWorkflowRole(Integer workflowRole) {
        this.workflowRole = workflowRole;
    }

    public Integer getWorkflowUser() {
        return workflowUser;
    }

    public void setWorkflowUser(Integer workflowUser) {
        this.workflowUser = workflowUser;
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

    public Integer getWorkflowPreviousNodeId() {
        return workflowPreviousNodeId;
    }

    public void setWorkflowPreviousNodeId(Integer workflowPreviousNodeId) {
        this.workflowPreviousNodeId = workflowPreviousNodeId;
    }

    public Integer getWorkflowNextNodeId() {
        return workflowNextNodeId;
    }

    public void setWorkflowNextNodeId(Integer workflowNextNodeId) {
        this.workflowNextNodeId = workflowNextNodeId;
    }

    public Integer getWorkflowDepartmentType() {
        return workflowDepartmentType;
    }

    public void setWorkflowDepartmentType(Integer workflowDepartmentType) { this.workflowDepartmentType = workflowDepartmentType; }

    public String getDepartmentName() { return departmentName; }

    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public String getRoleName() { return roleName; }

    public void setRoleName(String roleName) { this.roleName = roleName; }

    public String getUserName() { return userName; }

    public void setUserName(String userName) { this.userName = userName; }

    public String getDepartmentTypeName() { return departmentTypeName; }

    public void setDepartmentTypeName(String departmentTypeName) { this.departmentTypeName = departmentTypeName; }
}