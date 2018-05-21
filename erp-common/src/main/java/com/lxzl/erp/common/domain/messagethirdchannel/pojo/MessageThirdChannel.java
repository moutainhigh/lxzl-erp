package com.lxzl.erp.common.domain.messagethirdchannel.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageThirdChannel extends BasePO {

	private Integer messageThirdChannelId;   //唯一标识
	private String messageTitle;   //发送的信息主题
	private String messageContent;   //发送的信息内容
	private Integer messageType;   //消息类型，1:系统通知 其他待拓展
	private Integer messageChannel;   //消息渠道 1:钉钉消息 其他待拓展
	private Integer receiverUserId;   //接受者用户id
	private Integer senderUserId;   //发送者用户id[默认-1 代表系统用户]
	private String senderRemark;   //发送者备注[默认系统用户]
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人


	public Integer getMessageThirdChannelId(){
		return messageThirdChannelId;
	}

	public void setMessageThirdChannelId(Integer messageThirdChannelId){
		this.messageThirdChannelId = messageThirdChannelId;
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