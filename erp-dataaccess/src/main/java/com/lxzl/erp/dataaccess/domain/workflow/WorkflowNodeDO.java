package com.lxzl.erp.dataaccess.domain.workflow;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
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

	public void setWorkflowDepartmentType(Integer workflowDepartmentType) {
		this.workflowDepartmentType = workflowDepartmentType;
	}
}