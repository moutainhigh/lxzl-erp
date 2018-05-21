package com.lxzl.erp.dataaccess.domain.messagethirdchannel;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class MessageThirdChannelDO  extends BaseDO {

	private Integer id;
	private String messageTitle;
	private String messageContent;
	private Integer messageType;
	private Integer messageChannel;
	private Integer receiverUserId;
	private Integer senderUserId;
	private String senderRemark;
	private Integer dataStatus;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
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

	public Integer getMessageType(){
		return messageType;
	}

	public void setMessageType(Integer messageType){
		this.messageType = messageType;
	}

	public Integer getMessageChannel(){
		return messageChannel;
	}

	public void setMessageChannel(Integer messageChannel){
		this.messageChannel = messageChannel;
	}

	public Integer getReceiverUserId(){
		return receiverUserId;
	}

	public void setReceiverUserId(Integer receiverUserId){
		this.receiverUserId = receiverUserId;
	}

	public Integer getSenderUserId(){
		return senderUserId;
	}

	public void setSenderUserId(Integer senderUserId){
		this.senderUserId = senderUserId;
	}

	public String getSenderRemark(){
		return senderRemark;
	}

	public void setSenderRemark(String senderRemark){
		this.senderRemark = senderRemark;
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