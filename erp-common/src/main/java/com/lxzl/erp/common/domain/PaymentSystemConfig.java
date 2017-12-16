package com.lxzl.erp.common.domain;

/**
 * 描述: 支付系统配置信息
 *
 * @author gaochao
 * @date 2017-12-14 15:34
 */
public class PaymentSystemConfig {

    public static String paymentSystemQueryCustomerAccountURL;
    public static String paymentSystemManualChargeURL;
    public static String paymentSystemBalancePayURL;
    public static String paymentSystemAppId;
    public static String paymentSystemAppSecret;

    public void setPaymentSystemManualChargeURL(String paymentSystemManualChargeURL) {
        PaymentSystemConfig.paymentSystemManualChargeURL = paymentSystemManualChargeURL;
    }

    public void setPaymentSystemQueryCustomerAccountURL(String paymentSystemQueryCustomerAccountURL) {
        PaymentSystemConfig.paymentSystemQueryCustomerAccountURL = paymentSystemQueryCustomerAccountURL;
    }

    public void setPaymentSystemBalancePayURL(String paymentSystemBalancePayURL) {
        PaymentSystemConfig.paymentSystemBalancePayURL = paymentSystemBalancePayURL;
    }

    public void setPaymentSystemAppId(String paymentSystemAppId) {
        PaymentSystemConfig.paymentSystemAppId = paymentSystemAppId;
    }

    public void setPaymentSystemAppSecret(String paymentSystemAppSecret) {
        PaymentSystemConfig.paymentSystemAppSecret = paymentSystemAppSecret;
    }
}
