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
    private String businessOrderNo;
    private BigDecimal businessOrderAmount;
    private BigDecimal businessOrderRentDepositAmount;
    private BigDecimal businessOrderDepositAmount;
    private String businessOrderRemark;
    private String businessNotifyUrl;

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

    public String getBusinessOrderNo() {
        return businessOrderNo;
    }

    public void setBusinessOrderNo(String businessOrderNo) {
        this.businessOrderNo = businessOrderNo;
    }

    public String getBusinessOrderRemark() {
        return businessOrderRemark;
    }

    public void setBusinessOrderRemark(String businessOrderRemark) {
        this.businessOrderRemark = businessOrderRemark;
    }

    public String getBusinessNotifyUrl() {
        return businessNotifyUrl;
    }

    public void setBusinessNotifyUrl(String businessNotifyUrl) {
        this.businessNotifyUrl = businessNotifyUrl;
    }

    public BigDecimal getBusinessOrderDepositAmount() {
        return businessOrderDepositAmount;
    }

    public void setBusinessOrderDepositAmount(BigDecimal businessOrderDepositAmount) {
        this.businessOrderDepositAmount = businessOrderDepositAmount;
    }

    public BigDecimal getBusinessOrderRentDepositAmount() {
        return businessOrderRentDepositAmount;
    }

    public void setBusinessOrderRentDepositAmount(BigDecimal businessOrderRentDepositAmount) {
        this.businessOrderRentDepositAmount = businessOrderRentDepositAmount;
    }
}
