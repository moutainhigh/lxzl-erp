package com.lxzl.erp.common.domain.dingding;

import com.alibaba.fastjson.annotation.JSONField;
import com.lxzl.erp.common.domain.DingDingConfig;

/**
 * 钉钉基础数据传输对象
 * @author daiqi
 * @create 2018-04-25 13:54
 */
public class DingdingBaseDTO {
    /**
     * 审批回调url
     */
    private String callbackUrl;
    private String requestDingdingUrl;

    @JSONField(serialize = false)
    public String getRequestDingdingUrl() {
        return requestDingdingUrl;
    }

    public void setRequestDingdingUrl(String requestDingdingUrl) {
        this.requestDingdingUrl = requestDingdingUrl;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getSecret() {
        return DingDingConfig.dingdingSecret;
    }

}
