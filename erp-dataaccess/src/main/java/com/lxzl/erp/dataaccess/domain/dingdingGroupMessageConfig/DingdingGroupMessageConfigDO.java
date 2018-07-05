package com.lxzl.erp.dataaccess.domain.dingdingGroupMessageConfig;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class DingdingGroupMessageConfigDO  extends BaseDO {

	private Integer id;
	private Integer sendType;
	private String messageTitle;
	private String messageContent;
	private Integer subCompanyId;
	private String dingdingGroupUrl;
	private Integer dataStatus;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
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

}