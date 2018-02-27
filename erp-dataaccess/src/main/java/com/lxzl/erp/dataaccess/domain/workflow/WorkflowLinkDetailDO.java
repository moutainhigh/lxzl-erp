package com.lxzl.erp.dataaccess.domain.workflow;

import com.lxzl.erp.dataaccess.domain.system.ImageDO;
import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

import java.util.Date;
import java.util.List;


public class WorkflowLinkDetailDO  extends BaseDO {

	private Integer id;
	private Integer workflowLinkId;
	private String workflowReferNo;
	private Integer workflowStep;
	private Integer workflowCurrentNodeId;
	private Integer workflowPreviousNodeId;
	private Integer workflowNextNodeId;
	private Integer verifyUser;
	private String verifyUserName;
	private Date verifyTime;
	private Integer verifyStatus;
	private String verifyOpinion;
	private Integer dataStatus;
	private String remark;


	@Transient
	private String workflowCurrentNodeName;
	@Transient
	private String workflowPreviousNodeName;
	@Transient
	private String workflowNextNodeName;

	private List<ImageDO> imageDOList;

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

	public String getWorkflowReferNo() {
		return workflowReferNo;
	}

	public void setWorkflowReferNo(String workflowReferNo) {
		this.workflowReferNo = workflowReferNo;
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

	public String getVerifyUserName() {
		return verifyUserName;
	}

	public void setVerifyUserName(String verifyUserName) {
		this.verifyUserName = verifyUserName;
	}

	public String getWorkflowCurrentNodeName() {
		return workflowCurrentNodeName;
	}

	public void setWorkflowCurrentNodeName(String workflowCurrentNodeName) {
		this.workflowCurrentNodeName = workflowCurrentNodeName;
	}

	public String getWorkflowPreviousNodeName() {
		return workflowPreviousNodeName;
	}

	public void setWorkflowPreviousNodeName(String workflowPreviousNodeName) {
		this.workflowPreviousNodeName = workflowPreviousNodeName;
	}

	public String getWorkflowNextNodeName() {
		return workflowNextNodeName;
	}

	public void setWorkflowNextNodeName(String workflowNextNodeName) {
		this.workflowNextNodeName = workflowNextNodeName;
	}

	public List<ImageDO> getImageDOList() {
		return imageDOList;
	}

	public void setImageDOList(List<ImageDO> imageDOList) {
		this.imageDOList = imageDOList;
	}
}