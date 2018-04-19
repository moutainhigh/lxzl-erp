package com.lxzl.erp.common.domain.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;


@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageQueryParam extends BasePageParam {

    private Integer messageId;//信息id

    private Integer isRead;//是否已读站内信，0-未读，1-已读

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }
}
