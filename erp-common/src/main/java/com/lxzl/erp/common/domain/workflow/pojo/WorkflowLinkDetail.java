package com.lxzl.erp.common.domain.workflow.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.system.pojo.Image;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkflowLinkDetail extends BasePO {

	private Integer workflowLinkDetailId;   //唯一标识
	private Integer workflowLinkId;   //工作流线ID
	private String workflowReferNo;   //工作流关联ID
	private Integer workflowStep;   //流程当前步骤
	private Integer workflowCurrentNodeId;   //当前结点ID
	private Integer workflowPreviousNodeId;   //上节点ID
	private Integer workflowNextNodeId;   //下节点ID
	private Integer verifyUser;   //审核人
	private String verifyUserGroupId;		// 审核人组UUID，审核人为空时，该字段有值
	private String verifyUserName;   //审核人
	private Date verifyTime;   //审核时间
	private Integer verifyStatus;   //审核状态
	private String verifyOpinion;   //审核意见
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //添加时间
	private String updateUser;   //修改人

	private String workflowCurrentNodeName;
	private String workflowPreviousNodeName;
	private String workflowNextNodeName;

	private List<WorkflowVerifyUserGroup> workflowVerifyUserGroupList;

	private List<Image> imageList;
	public Integer getWorkflowLinkDetailId(){
		return workflowLinkDetailId;
	}

	public void setWorkflowLinkDetailId(Integer workflowLinkDetailId){
		this.workflowLinkDetailId = workflowLinkDetailId;
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

	public String getVerifyUserName() {
		return verifyUserName;
	}

	public void setVerifyUserName(String verifyUserName) {
		this.verifyUserName = verifyUserName;
	}

	public String getWorkflowCurrentNodeName() {
		return workflowCurrentNodeName;
	}

	public void setWorkflowCurrentNodeName(String workflowCurrentNodeName) { this.workflowCurrentNodeName = workflowCurrentNodeName; }

	public String getWorkflowPreviousNodeName() {
		return workflowPreviousNodeName;
	}

	public void setWorkflowPreviousNodeName(String workflowPreviousNodeName) { this.workflowPreviousNodeName = workflowPreviousNodeName; }

	public String getWorkflowNextNodeName() {
		return workflowNextNodeName;
	}

	public void setWorkflowNextNodeName(String workflowNextNodeName) {
		this.workflowNextNodeName = workflowNextNodeName;
	}

	public List<Image> getImageList() {
		return imageList;
	}

	public void setImageList(List<Image> imageList) {
		this.imageList = imageList;
	}

	public List<WorkflowVerifyUserGroup> getWorkflowVerifyUserGroupList() { return workflowVerifyUserGroupList; }

	public void setWorkflowVerifyUserGroupList(List<WorkflowVerifyUserGroup> workflowVerifyUserGroupList) { this.workflowVerifyUserGroupList = workflowVerifyUserGroupList; }

	public String getVerifyUserGroupId() { return verifyUserGroupId; }

	public void setVerifyUserGroupId(String verifyUserGroupId) { this.verifyUserGroupId = verifyUserGroupId; }
}