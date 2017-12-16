package com.lxzl.erp.common.domain.payment.account.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.payment.PaymentIdentityParam;

import java.math.BigDecimal;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-16 13:41
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BalancePayParam extends PaymentIdentityParam {

    private String businessCustomerNo;
    private BigDecimal businessOrderAmount;

    public BigDecimal getBusinessOrderAmount() {
        return businessOrderAmount;
    }

    public void setBusinessOrderAmount(BigDecimal businessOrderAmount) {
        this.businessOrderAmount = businessOrderAmount;
    }

    public String getBusinessCustomerNo() {
        return businessCustomerNo;
    }

    public void setBusinessCustomerNo(String businessCustomerNo) {
        this.businessCustomerNo = businessCustomerNo;
    }
}
