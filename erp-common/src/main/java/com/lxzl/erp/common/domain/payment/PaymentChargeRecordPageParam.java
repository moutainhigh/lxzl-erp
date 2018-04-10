package com.lxzl.erp.common.domain.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;

import java.util.Date;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 20:04 2018/4/9
 * @Modified By:
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentChargeRecordPageParam  extends BasePageParam {
    private String businessCustomerNo;
    private Date queryStartTime;
    private Date queryEndTime;
    private Integer chargeType;
    private Integer chargeStatus;

    private String businessAppId;
    private String businessAppSecret;
    private String businessOperateUser;

    private String businessCustomerName;
    private String chargeBodyId;

    public String getBusinessCustomerNo() {
        return businessCustomerNo;
    }

    public void setBusinessCustomerNo(String businessCustomerNo) {
        this.businessCustomerNo = businessCustomerNo;
    }

    public Date getQueryStartTime() {
        return queryStartTime;
    }

    public void setQueryStartTime(Date queryStartTime) {
        this.queryStartTime = queryStartTime;
    }

    public Date getQueryEndTime() {
        return queryEndTime;
    }

    public void setQueryEndTime(Date queryEndTime) {
        this.queryEndTime = queryEndTime;
    }

    public Integer getChargeType() {
        return chargeType;
    }

    public void setChargeType(Integer chargeType) {
        this.chargeType = chargeType;
    }

    public Integer getChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(Integer chargeStatus) {
        this.chargeStatus = chargeStatus;
    }

    public String getBusinessAppId() {
        return businessAppId;
    }

    public void setBusinessAppId(String businessAppId) {
        this.businessAppId = businessAppId;
    }

    public String getBusinessAppSecret() {
        return businessAppSecret;
    }

    public void setBusinessAppSecret(String businessAppSecret) {
        this.businessAppSecret = businessAppSecret;
    }

    public String getBusinessOperateUser() {
        return businessOperateUser;
    }

    public void setBusinessOperateUser(String businessOperateUser) {
        this.businessOperateUser = businessOperateUser;
    }

    public String getChargeBodyId() {
        return chargeBodyId;
    }

    public void setChargeBodyId(String chargeBodyId) {
        this.chargeBodyId = chargeBodyId;
    }

    public String getBusinessCustomerName() {
        return businessCustomerName;
    }

    public void setBusinessCustomerName(String businessCustomerName) {
        this.businessCustomerName = businessCustomerName;
    }
}
