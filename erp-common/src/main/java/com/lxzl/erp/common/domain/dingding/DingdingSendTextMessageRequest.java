package com.lxzl.erp.common.domain.dingding;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author kai
 * @date 2018-02-05 09:50
 */
public class DingdingSendTextMessageRequest implements Serializable {
    @JSONField(name = "msgtype")
    private String msgtype;
    @JSONField(name = "text")
    private DingdingSendTextMessageContent text;
    @JSONField(name = "at")
    private DingdingSendMessageAt at;

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public DingdingSendTextMessageContent getText() {
        return text;
    }

    public void setText(DingdingSendTextMessageContent text) {
        this.text = text;
    }

    public DingdingSendMessageAt getAt() {
        return at;
    }

    public void setAt(DingdingSendMessageAt at) {
        this.at = at;
    }
}
