package com.lxzl.erp.common.domain.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.message.pojo.Message;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.util.validate.constraints.CollectionNotNull;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageBatchReadParam implements Serializable {

    @Valid
    @CollectionNotNull(message = ErrorCode.MESSAGE_READ_NULL,groups = {IdGroup.class})
    private List<Message> messageList;

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }
}
