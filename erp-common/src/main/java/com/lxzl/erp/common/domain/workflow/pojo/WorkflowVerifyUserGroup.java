package com.lxzl.erp.common.domain.workflow.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkflowVerifyUserGroup extends BasePO {

	private Integer workflowVerifyUserGroupId;   //唯一标识
	private Integer verifyUserGroupId;   //审核组ID
	private Integer verifyType;   //审核类型，1-本条审核通过则直接通过，2-相同审核组的所有2状态的审核通过才算通过
	private Integer verifyUser;   //审核人
	private Date verifyTime;   //审核时间
	private Integer verifyStatus;   //审核状态
	private String verifyOpinion;   //审核意见
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人

	private String verifyUserName;

	public Integer getWorkflowVerifyUserGroupId(){
		return workflowVerifyUserGroupId;
	}

	public void setWorkflowVerifyUserGroupId(Integer workflowVerifyUserGroupId){ this.workflowVerifyUserGroupId = workflowVerifyUserGroupId; }

	public Integer getVerifyUserGroupId(){
		return verifyUserGroupId;
	}

	public void setVerifyUserGroupId(Integer verifyUserGroupId){
		this.verifyUserGroupId = verifyUserGroupId;
	}

	public Integer getVerifyType(){
		return verifyType;
	}

	public void setVerifyType(Integer verifyType){
		this.verifyType = verifyType;
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

	public String getVerifyUserName() { return verifyUserName; }

	public void setVerifyUserName(String verifyUserName) { this.verifyUserName = verifyUserName; }
}