package com.lxzl.erp.common.domain.payment;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2018-01-29 14:52
 */
public class PayResultQueryParam extends PaymentIdentityParam {

    private String businessCustomerNo;
    private String businessOrderNo;
    private String payType;

    public String getBusinessCustomerNo() {
        return businessCustomerNo;
    }

    public void setBusinessCustomerNo(String businessCustomerNo) {
        this.businessCustomerNo = businessCustomerNo;
    }

    public String getBusinessOrderNo() {
        return businessOrderNo;
    }

    public void setBusinessOrderNo(String businessOrderNo) {
        this.businessOrderNo = businessOrderNo;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }
}
