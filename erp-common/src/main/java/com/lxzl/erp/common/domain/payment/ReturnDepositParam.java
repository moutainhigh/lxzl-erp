package com.lxzl.erp.common.domain.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2018-01-01 10:42
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReturnDepositParam extends PaymentIdentityParam {
    private String businessCustomerNo;
    private BigDecimal businessReturnRentDepositAmount;
    private BigDecimal businessReturnDepositAmount;

    public String getBusinessCustomerNo() {
        return businessCustomerNo;
    }

    public void setBusinessCustomerNo(String businessCustomerNo) {
        this.businessCustomerNo = businessCustomerNo;
    }

    public BigDecimal getBusinessReturnRentDepositAmount() {
        return businessReturnRentDepositAmount;
    }

    public void setBusinessReturnRentDepositAmount(BigDecimal businessReturnRentDepositAmount) {
        this.businessReturnRentDepositAmount = businessReturnRentDepositAmount;
    }

    public BigDecimal getBusinessReturnDepositAmount() {
        return businessReturnDepositAmount;
    }

    public void setBusinessReturnDepositAmount(BigDecimal businessReturnDepositAmount) {
        this.businessReturnDepositAmount = businessReturnDepositAmount;
    }
}
