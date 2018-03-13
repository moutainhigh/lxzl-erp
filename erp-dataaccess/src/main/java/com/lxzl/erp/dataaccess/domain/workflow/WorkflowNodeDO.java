package com.lxzl.erp.dataaccess.domain.workflow;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

import java.util.Date;


public class WorkflowNodeDO  extends BaseDO {

	private Integer id;
	private String workflowNodeName;
	private Integer workflowTemplateId;
	private Integer workflowStep;
	private Integer workflowPreviousNodeId;
	private Integer workflowNextNodeId;
	private Integer workflowDepartmentType;
	private Integer workflowDepartment;
	private Integer workflowRole;
	private Integer workflowUser;
	private Integer dataStatus;
	private String remark;
	private Integer workflowRoleType;   //本步骤可审批角色类型

	@Transient
	private String departmentName;
	@Transient
	private String roleName;
	@Transient
	private String userName;
	@Transient
	private String departmentTypeName;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getWorkflowNodeName(){
		return workflowNodeName;
	}

	public void setWorkflowNodeName(String workflowNodeName){
		this.workflowNodeName = workflowNodeName;
	}

	public Integer getWorkflowTemplateId(){
		return workflowTemplateId;
	}

	public void setWorkflowTemplateId(Integer workflowTemplateId){
		this.workflowTemplateId = workflowTemplateId;
	}

	public Integer getWorkflowStep(){
		return workflowStep;
	}

	public void setWorkflowStep(Integer workflowStep){
		this.workflowStep = workflowStep;
	}

	public Integer getWorkflowDepartment(){
		return workflowDepartment;
	}

	public void setWorkflowDepartment(Integer workflowDepartment){
		this.workflowDepartment = workflowDepartment;
	}

	public Integer getWorkflowRole(){
		return workflowRole;
	}

	public void setWorkflowRole(Integer workflowRole){
		this.workflowRole = workflowRole;
	}

	public Integer getWorkflowUser(){
		return workflowUser;
	}

	public void setWorkflowUser(Integer workflowUser){
		this.workflowUser = workflowUser;
	}

	public Integer getDataStatus(){
		return dataStatus;
	}

	public void setDataStatus(Integer dataStatus){
		this.dataStatus = dataStatus;
	}

	public String getRemark(){
		return remark;
	}

	public void setRemark(String remark){
		this.remark = remark;
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

	public Integer getWorkflowRoleType() { return workflowRoleType; }

	public void setWorkflowRoleType(Integer workflowRoleType) { this.workflowRoleType = workflowRoleType; }
}