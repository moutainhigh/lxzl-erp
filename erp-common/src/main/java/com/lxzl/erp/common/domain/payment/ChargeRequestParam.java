package com.lxzl.erp.common.domain.payment;

import java.math.BigDecimal;

/**
 * @Author: Sunzhipeng
 * @Description:
 * @Date: Created in 2018\6\9 0009 14:00
 */
public class ChargeRequestParam {
    private String businessCustomerNo;
    private BigDecimal chargeAmount;
    private String chargeRemark;

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

    public String getChargeRemark() {
        return chargeRemark;
    }

    public void setChargeRemark(String chargeRemark) {
        this.chargeRemark = chargeRemark;
    }
}
