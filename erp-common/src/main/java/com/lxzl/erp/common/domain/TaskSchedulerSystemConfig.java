package com.lxzl.erp.common.domain;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/8/6
 * @Time : Created in 20:04
 */
public class TaskSchedulerSystemConfig {

    public static String paymentSystemAppId;
    public static String paymentSystemAppSecret;

    public static void setPaymentSystemAppId(String paymentSystemAppId) {
        TaskSchedulerSystemConfig.paymentSystemAppId = paymentSystemAppId;
    }

    public static void setPaymentSystemAppSecret(String paymentSystemAppSecret) {
        TaskSchedulerSystemConfig.paymentSystemAppSecret = paymentSystemAppSecret;
    }

    public static String getPaymentSystemAppId() {
        return paymentSystemAppId;
    }

    public static String getPaymentSystemAppSecret() {
        return paymentSystemAppSecret;
    }
}
