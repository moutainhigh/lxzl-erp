package com.lxzl.erp.dataaccess.domain.workflow;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class WorkflowLinkDetailDO  extends BaseDO {

	private Integer id;
	private Integer workflowLinkId;
	private Integer workflowReferId;
	private Integer workflowStep;
	private Integer workflowCurrentNodeId;
	private Integer workflowPreviousNodeId;
	private Integer workflowNextNodeId;
	private Integer verifyUser;
	private Date verifyTime;
	private Integer verifyStatus;
	private String verifyOpinion;
	private Integer dataStatus;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getWorkflowLinkId(){
		return workflowLinkId;
	}

	public void setWorkflowLinkId(Integer workflowLinkId){
		this.workflowLinkId = workflowLinkId;
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

	public Integer getWorkflowCurrentNodeId(){
		return workflowCurrentNodeId;
	}

	public void setWorkflowCurrentNodeId(Integer workflowCurrentNodeId){
		this.workflowCurrentNodeId = workflowCurrentNodeId;
	}

	public Integer getWorkflowPreviousNodeId(){
		return workflowPreviousNodeId;
	}

	public void setWorkflowPreviousNodeId(Integer workflowPreviousNodeId){
		this.workflowPreviousNodeId = workflowPreviousNodeId;
	}

	public Integer getWorkflowNextNodeId(){
		return workflowNextNodeId;
	}

	public void setWorkflowNextNodeId(Integer workflowNextNodeId){
		this.workflowNextNodeId = workflowNextNodeId;
	}

	public Integer getVerifyUser(){
		return verifyUser;
	}

	public void setVerifyUser(Integer verifyUser){
		this.verifyUser = verifyUser;
	}

	public Date getVerifyTime(){
		return verifyTime;
	}

	public void setVerifyTime(Date verifyTime){
		this.verifyTime = verifyTime;
	}

	public Integer getVerifyStatus(){
		return verifyStatus;
	}

	public void setVerifyStatus(Integer verifyStatus){
		this.verifyStatus = verifyStatus;
	}

	public String getVerifyOpinion(){
		return verifyOpinion;
	}

	public void setVerifyOpinion(String verifyOpinion){
		this.verifyOpinion = verifyOpinion;
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

}