package com.lxzl.erp.common.domain;

/**
 * 描述: 支付系统配置信息
 *
 * @author gaochao
 * @date 2017-12-14 15:34
 */
public class PaymentSystemConfig {

    public static String paymentSystemQueryCustomerAccountURL;
    public static String paymentSystemAppId;
    public static String paymentSystemAppSecret;

    public void setPaymentSystemQueryCustomerAccountURL(String paymentSystemQueryCustomerAccountURL) {
        PaymentSystemConfig.paymentSystemQueryCustomerAccountURL = paymentSystemQueryCustomerAccountURL;
    }

    public void setPaymentSystemAppId(String paymentSystemAppId) {
        PaymentSystemConfig.paymentSystemAppId = paymentSystemAppId;
    }

    public void setPaymentSystemAppSecret(String paymentSystemAppSecret) {
        PaymentSystemConfig.paymentSystemAppSecret = paymentSystemAppSecret;
    }
}
