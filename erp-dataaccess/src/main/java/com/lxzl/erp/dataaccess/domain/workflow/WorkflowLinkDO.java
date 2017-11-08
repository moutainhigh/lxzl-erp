package com.lxzl.erp.dataaccess.domain.workflow;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;
import java.util.List;


public class WorkflowLinkDO  extends BaseDO {

	private Integer id;
	private Integer workflowType;
	private Integer workflowTemplateId;
	private Integer workflowReferId;
	private Integer workflowStep;
	private Integer workflowLastStep;
	private Integer workflowCurrentNodeId;
	private Integer currentVerifyUser;
	private Integer currentVerifyStatus;
	private Integer dataStatus;
	private String remark;

	private List<WorkflowLinkDetailDO> workflowLinkDetailDOList;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
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

	public List<WorkflowLinkDetailDO> getWorkflowLinkDetailDOList() {
		return workflowLinkDetailDOList;
	}

	public void setWorkflowLinkDetailDOList(List<WorkflowLinkDetailDO> workflowLinkDetailDOList) {
		this.workflowLinkDetailDOList = workflowLinkDetailDOList;
	}

	public Integer getCurrentVerifyUser() {
		return currentVerifyUser;
	}

	public void setCurrentVerifyUser(Integer currentVerifyUser) {
		this.currentVerifyUser = currentVerifyUser;
	}

	public Integer getCurrentVerifyStatus() {
		return currentVerifyStatus;
	}

	public void setCurrentVerifyStatus(Integer currentVerifyStatus) {
		this.currentVerifyStatus = currentVerifyStatus;
	}
}