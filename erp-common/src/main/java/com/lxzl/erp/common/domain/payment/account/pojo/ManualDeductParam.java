package com.lxzl.erp.common.domain.payment.account.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.payment.PaymentIdentityParam;

import java.math.BigDecimal;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-15 10:43
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ManualDeductParam extends PaymentIdentityParam {
    private String businessCustomerNo;
    private BigDecimal deductAmount;
    private String deductRemark;

    public String getBusinessCustomerNo() {
        return businessCustomerNo;
    }

    public void setBusinessCustomerNo(String businessCustomerNo) {
        this.businessCustomerNo = businessCustomerNo;
    }

    public BigDecimal getDeductAmount() {
        return deductAmount;
    }

    public void setDeductAmount(BigDecimal deductAmount) {
        this.deductAmount = deductAmount;
    }

    public String getDeductRemark() {
        return deductRemark;
    }

    public void setDeductRemark(String deductRemark) {
        this.deductRemark = deductRemark;
    }
}
