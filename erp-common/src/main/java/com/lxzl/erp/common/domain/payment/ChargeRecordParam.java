package com.lxzl.erp.common.domain.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ChargeRecordParam extends BasePageParam {

    private String businessCustomerNo;
    private String businessAppId;
    private String businessAppSecret;
    private String businessOperateUser;

    private Integer customerId;

    public String getBusinessCustomerNo() { return businessCustomerNo; }

    public void setBusinessCustomerNo(String businessCustomerNo) { this.businessCustomerNo = businessCustomerNo; }

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

    public String getBusinessOperateUser() { return businessOperateUser; }

    public void setBusinessOperateUser(String businessOperateUser) {
        this.businessOperateUser = businessOperateUser;
    }

    public Integer getCustomerId() { return customerId; }

    public void setCustomerId(Integer customerId) { this.customerId = customerId; }
}
