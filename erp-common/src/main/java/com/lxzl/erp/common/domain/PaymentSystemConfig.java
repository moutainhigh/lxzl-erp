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
    public static String paymentSystemManualDeductURL;
    public static String paymentSystemBalancePayURL;
    public static String paymentSystemWeixinPayURL;
    public static String paymentSystemWeixinPayCallbackURL;
    public static String paymentSystemReturnDepositURL;
    public static String paymentSystemAppId;
    public static String paymentSystemAppSecret;
    public static String paymentSystemAppWeixinChargeAppSecret;

    public static String paymentSystemWeixinChargeURL;

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

    public void setPaymentSystemManualDeductURL(String paymentSystemManualDeductURL) {
        PaymentSystemConfig.paymentSystemManualDeductURL = paymentSystemManualDeductURL;
    }

    public void setPaymentSystemReturnDepositURL(String paymentSystemReturnDepositURL) {
        PaymentSystemConfig.paymentSystemReturnDepositURL = paymentSystemReturnDepositURL;
    }

    public void setPaymentSystemWeixinPayURL(String paymentSystemWeixinPayURL) {
        PaymentSystemConfig.paymentSystemWeixinPayURL = paymentSystemWeixinPayURL;
    }

    public void setPaymentSystemWeixinPayCallbackURL(String paymentSystemWeixinPayCallbackURL) {
        PaymentSystemConfig.paymentSystemWeixinPayCallbackURL = paymentSystemWeixinPayCallbackURL;
    }

    public void setPaymentSystemWeixinChargeURL(String paymentSystemWeixinChargeURL) {
        PaymentSystemConfig.paymentSystemWeixinChargeURL = paymentSystemWeixinChargeURL;
    }

    public void setPaymentSystemAppWeixinChargeAppSecret(String paymentSystemAppWeixinChargeAppSecret) {
        PaymentSystemConfig.paymentSystemAppWeixinChargeAppSecret = paymentSystemAppWeixinChargeAppSecret;
    }
}
