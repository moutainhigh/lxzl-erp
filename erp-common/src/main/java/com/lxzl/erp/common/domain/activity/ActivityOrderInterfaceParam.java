package com.lxzl.erp.common.domain.activity;

public class ActivityOrderInterfaceParam {
    private String appId;
    private String appSecret;
    private ActivityOrderParam activityOrderParam;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public ActivityOrderParam getActivityOrderParam() {
        return activityOrderParam;
    }

    public void setActivityOrderParam(ActivityOrderParam activityOrderParam) {
        this.activityOrderParam = activityOrderParam;
    }
}