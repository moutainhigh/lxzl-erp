package com.lxzl.erp.common.domain.payment.account.pojo;

import com.lxzl.erp.common.domain.payment.PaymentIdentityParam;

import java.math.BigDecimal;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-15 10:43
 */
public class ManualChargeParam extends PaymentIdentityParam {
    private String businessCustomerNo;
    private BigDecimal chargeAmount;

    public String getBusinessCustomerNo() {
        return businessCustomerNo;
    }

    public void setBusinessCustomerNo(String businessCustomerNo) {
        this.businessCustomerNo = businessCustomerNo;
    }

    public BigDecimal getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(BigDecimal chargeAmount) {
        this.chargeAmount = chargeAmount;
    }
}
