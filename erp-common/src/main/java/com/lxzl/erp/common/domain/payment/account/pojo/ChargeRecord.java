package com.lxzl.erp.common.domain.payment.account.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;

import java.math.BigDecimal;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChargeRecord extends BasePO {

    private Integer chargeOrderId;
    private String chargeOrderNo;
    private Integer chargeType;
    private Integer customerId;
    private String businessOrderNo;
    private Date chargeTime;
    private BigDecimal chargeAmountExpect;
    private BigDecimal chargeAmountReal;
    private Integer chargeStatus;
    private String chargeName;
    private String chargeDescription;
    private String openId;
    private String businessCustomerNo;
    private String businessCustomerName;


    private String thirdPartyPayOrderId;  //快付通单号字段
    private String remark;   //备注
    private String subCompanyId;    //充值主体ID（分公司ID）
    private String subCompanyName;  //充值主体名称（分公司名称）

    // 冗余没啥用
    private String customerName;

    public Integer getChargeOrderId() { return chargeOrderId; }

    public void setChargeOrderId(Integer chargeOrderId) { this.chargeOrderId = chargeOrderId; }

    public String getChargeOrderNo() { return chargeOrderNo; }

    public void setChargeOrderNo(String chargeOrderNo) { this.chargeOrderNo = chargeOrderNo; }

    public Integer getChargeType() { return chargeType; }

    public void setChargeType(Integer chargeType) { this.chargeType = chargeType; }

    public Integer getCustomerId() { return customerId; }

    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public String getBusinessOrderNo() { return businessOrderNo; }

    public void setBusinessOrderNo(String businessOrderNo) { this.businessOrderNo = businessOrderNo; }

    public Date getChargeTime() { return chargeTime; }

    public void setChargeTime(Date chargeTime) { this.chargeTime = chargeTime; }

    public BigDecimal getChargeAmountExpect() { return chargeAmountExpect; }

    public void setChargeAmountExpect(BigDecimal chargeAmountExpect) { this.chargeAmountExpect = chargeAmountExpect; }

    public BigDecimal getChargeAmountReal() { return chargeAmountReal; }

    public void setChargeAmountReal(BigDecimal chargeAmountReal) { this.chargeAmountReal = chargeAmountReal; }

    public Integer getChargeStatus() { return chargeStatus; }

    public void setChargeStatus(Integer chargeStatus) { this.chargeStatus = chargeStatus; }

    public String getChargeName() { return chargeName; }

    public void setChargeName(String chargeName) { this.chargeName = chargeName; }

    public String getChargeDescription() { return chargeDescription; }

    public void setChargeDescription(String chargeDescription) { this.chargeDescription = chargeDescription; }

    public String getOpenId() { return openId; }

    public void setOpenId(String openId) { this.openId = openId; }

    public String getCustomerName() { return customerName; }

    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getBusinessCustomerNo() { return businessCustomerNo; }

    public void setBusinessCustomerNo(String businessCustomerNo) { this.businessCustomerNo = businessCustomerNo; }

    public String getBusinessCustomerName() {
        return businessCustomerName;
    }

    public void setBusinessCustomerName(String businessCustomerName) {
        this.businessCustomerName = businessCustomerName;
    }

    public String getThirdPartyPayOrderId() {
        return thirdPartyPayOrderId;
    }

    public void setThirdPartyPayOrderId(String thirdPartyPayOrderId) {
        this.thirdPartyPayOrderId = thirdPartyPayOrderId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSubCompanyId() {
        return subCompanyId;
    }

    public void setSubCompanyId(String subCompanyId) {
        this.subCompanyId = subCompanyId;
    }

    public String getSubCompanyName() {
        return subCompanyName;
    }

    public void setSubCompanyName(String subCompanyName) {
        this.subCompanyName = subCompanyName;
    }
}
