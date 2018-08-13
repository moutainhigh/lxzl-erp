package com.lxzl.erp.common.domain.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-16 13:41
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeixinPayParam extends PaymentIdentityParam {

    private String businessCustomerNo;
    private String businessOrderNo;
    private String businessOrderRemark;
    private String businessNotifyUrl;
    private BigDecimal amount;
    private String payName;
    private String payDescription;
    private String openId;
    private String clientIp;
    private String businessCustomerName;//客户名称
    private String chargeBodyName;//分公司名称
    private Integer chargeBodyId;//分公司ID

    private Integer customerId;

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

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public String getPayDescription() {
        return payDescription;
    }

    public void setPayDescription(String payDescription) {
        this.payDescription = payDescription;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getBusinessCustomerName() {
        return businessCustomerName;
    }

    public void setBusinessCustomerName(String businessCustomerName) {
        this.businessCustomerName = businessCustomerName;
    }

    public String getChargeBodyName() {
        return chargeBodyName;
    }

    public void setChargeBodyName(String chargeBodyName) {
        this.chargeBodyName = chargeBodyName;
    }

    public Integer getChargeBodyId() {
        return chargeBodyId;
    }

    public void setChargeBodyId(Integer chargeBodyId) {
        this.chargeBodyId = chargeBodyId;
    }
}
