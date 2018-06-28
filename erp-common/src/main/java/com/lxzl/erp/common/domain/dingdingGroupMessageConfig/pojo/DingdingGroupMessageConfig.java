package com.lxzl.erp.common.domain.dingdingGroupMessageConfig.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class DingdingGroupMessageConfig extends BasePO {

	private Integer dingdingGroupMessageConfigId;   //唯一标识
	private Integer sendType;   //发送类型：1-续租成功，2-结算单重算成功
	private String messageTitle;   //发送的信息标题
	private String messageContent;   //发送的信息内容
	private Integer subCompanyId;   //所属分公司
	private String dingdingGroupUrl;   //钉钉群URL
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人


	public Integer getDingdingGroupMessageConfigId(){
		return dingdingGroupMessageConfigId;
	}

	public void setDingdingGroupMessageConfigId(Integer dingdingGroupMessageConfigId){
		this.dingdingGroupMessageConfigId = dingdingGroupMessageConfigId;
	}

	public Integer getSendType(){
		return sendType;
	}

	public void setSendType(Integer sendType){
		this.sendType = sendType;
	}

	public String getMessageTitle(){
		return messageTitle;
	}

	public void setMessageTitle(String messageTitle){
		this.messageTitle = messageTitle;
	}

	public String getMessageContent(){
		return messageContent;
	}

	public void setMessageContent(String messageContent){
		this.messageContent = messageContent;
	}

	public Integer getSubCompanyId(){
		return subCompanyId;
	}

	public void setSubCompanyId(Integer subCompanyId){
		this.subCompanyId = subCompanyId;
	}

	public String getDingdingGroupUrl(){
		return dingdingGroupUrl;
	}

	public void setDingdingGroupUrl(String dingdingGroupUrl){
		this.dingdingGroupUrl = dingdingGroupUrl;
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