package com.lxzl.erp.common.domain.workflow.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkflowLink implements Serializable {

	private Integer workflowLinkId;   //唯一标识
	private Integer workflowType;   //工作流类型
	private Integer workflowTemplateId;   //工作流模板ID
	private Integer workflowReferId;   //工作流关联ID
	private Integer workflowStep;   //流程当前步骤
	private Integer workflowLastStep;   //流程最后步骤
	private Integer workflowCurrentNodeId;   //当前结点ID
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //添加时间
	private String updateUser;   //修改人

	private List<WorkflowLinkDetail> workflowLinkDetailList;


	public Integer getWorkflowLinkId(){
		return workflowLinkId;
	}

	public void setWorkflowLinkId(Integer workflowLinkId){
		this.workflowLinkId = workflowLinkId;
	}

	public Integer getWorkflowType(){
		return workflowType;
	}

	public void setWorkflowType(Integer workflowType){
		this.workflowType = workflowType;
	}

	public Integer getWorkflowTemplateId(){
		return workflowTemplateId;
	}

	public void setWorkflowTemplateId(Integer workflowTemplateId){
		this.workflowTemplateId = workflowTemplateId;
	}

	public Integer getWorkflowReferId(){
		return workflowReferId;
	}

	public void setWorkflowReferId(Integer workflowReferId){
		this.workflowReferId = workflowReferId;
	}

	public Integer getWorkflowStep(){
		return workflowStep;
	}

	public void setWorkflowStep(Integer workflowStep){
		this.workflowStep = workflowStep;
	}

	public Integer getWorkflowLastStep(){
		return workflowLastStep;
	}

	public void setWorkflowLastStep(Integer workflowLastStep){
		this.workflowLastStep = workflowLastStep;
	}

	public Integer getWorkflowCurrentNodeId(){
		return workflowCurrentNodeId;
	}

	public void setWorkflowCurrentNodeId(Integer workflowCurrentNodeId){
		this.workflowCurrentNodeId = workflowCurrentNodeId;
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

	public Date getCreateTime(){
		return createTime;
	}

	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public String getCreateUser(){
		return createUser;
	}

	public void setCreateUser(String createUser){
		this.createUser = createUser;
	}

	public Date getUpdateTime(){
		return updateTime;
	}

	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}

	public String getUpdateUser(){
		return updateUser;
	}

	public void setUpdateUser(String updateUser){
		this.updateUser = updateUser;
	}

	public List<WorkflowLinkDetail> getWorkflowLinkDetailList() {
		return workflowLinkDetailList;
	}

	public void setWorkflowLinkDetailList(List<WorkflowLinkDetail> workflowLinkDetailList) {
		this.workflowLinkDetailList = workflowLinkDetailList;
	}
}