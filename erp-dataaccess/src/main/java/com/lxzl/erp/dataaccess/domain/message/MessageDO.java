package com.lxzl.erp.dataaccess.domain.message;


import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

import java.util.Date;

public class MessageDO extends BaseDO {

    private Integer id;
    private Integer senderUserId;
    private Integer receiverUserId;
    private Date sendTime;
    private Date readTime;
    private String title;
    private String messageText;
    private Integer dataStatus;
    private String remark;


    @Transient
    private String senderName;
    @Transient
    private String receiverName;
    @Transient
    private Integer isRead;

    public Integer getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }


    public Integer getId() {
        return id;
    }

    public Integer getSenderUserId() {
        return senderUserId;
    }

    public Integer getReceiverUserId() {
        return receiverUserId;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public Date getReadTime() {
        return readTime;
    }

    public String getTitle() {
        return title;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setSenderUserId(Integer senderUserId) {
        this.senderUserId = senderUserId;
    }

    public void setReceiverUserId(Integer receiverUserId) {
        this.receiverUserId = receiverUserId;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }
}
