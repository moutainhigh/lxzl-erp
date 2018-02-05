package com.lxzl.erp.common.domain.dingding;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author kai
 * @date 2018-02-05 09:50
 */
public class DingdingSendTextMessageContent implements Serializable {
    @JSONField(name = "content")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
