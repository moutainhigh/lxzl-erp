package com.lxzl.erp.common.domain.workflow.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkflowTemplateDingding extends BasePO {

	private Integer workflowTemplateDingdingId;   //唯一标识
	private String dingdingProcessCode;   //钉钉工作流模板代码
	private String name;   //模版表单名称
	private Integer nameIndex;   //钉钉模板表单元素名所在的位置，从上到下依次为1,2,3以此类推
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人


	public Integer getWorkflowTemplateDingdingId(){
		return workflowTemplateDingdingId;
	}

	public void setWorkflowTemplateDingdingId(Integer workflowTemplateDingdingId){
		this.workflowTemplateDingdingId = workflowTemplateDingdingId;
	}

	public String getDingdingProcessCode(){
		return dingdingProcessCode;
	}

	public void setDingdingProcessCode(String dingdingProcessCode){
		this.dingdingProcessCode = dingdingProcessCode;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public Integer getNameIndex(){
		return nameIndex;
	}

	public void setNameIndex(Integer nameIndex){
		this.nameIndex = nameIndex;
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

}