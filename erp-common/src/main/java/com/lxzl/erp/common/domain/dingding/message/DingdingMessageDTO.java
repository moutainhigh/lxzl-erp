package com.lxzl.erp.common.domain.dingding.message;

import com.lxzl.erp.common.domain.dingding.DingdingBaseDTO;
import com.lxzl.erp.common.domain.messagethirdchannel.pojo.MessageThirdChannel;

/**
 * @author daiqi
 * @create 2018-05-21 13:44
 */
public class DingdingMessageDTO extends DingdingBaseDTO {

    private Integer receiverUserId;
    private String messageContent;
    private String messageTitle;

    public DingdingMessageDTO() {

    }

    public DingdingMessageDTO(MessageThirdChannel messageThirdChannel) {
        this.receiverUserId = messageThirdChannel.getReceiverUserId();
        this.messageContent = messageThirdChannel.getMessageContent();
        this.messageTitle = messageThirdChannel.getMessageTitle();
    }

    public Integer getReceiverUserId() {
        return receiverUserId;
    }

    public void setReceiverUserId(Integer receiverUserId) {
        this.receiverUserId = receiverUserId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }
}
