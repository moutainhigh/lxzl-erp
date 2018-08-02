package com.lxzl.erp.common.domain;

public class MallSystemConfig {
    public static String getActivityOrderUrl;
    public static String mallSystemAppId;
    public static String mallSystemAppSecret;

    public String getGetActivityOrderUrl() {
        return getActivityOrderUrl;
    }

    public void setGetActivityOrderUrl(String getActivityOrderUrl) {
        MallSystemConfig.getActivityOrderUrl = getActivityOrderUrl;
    }

    public String getMallSystemAppId() {
        return mallSystemAppId;
    }

    public void setMallSystemAppId(String mallSystemAppId) {
        MallSystemConfig.mallSystemAppId = mallSystemAppId;
    }

    public static String getMallSystemAppSecret() {
        return mallSystemAppSecret;
    }

    public static void setMallSystemAppSecret(String mallSystemAppSecret) {
        MallSystemConfig.mallSystemAppSecret = mallSystemAppSecret;
    }
}
