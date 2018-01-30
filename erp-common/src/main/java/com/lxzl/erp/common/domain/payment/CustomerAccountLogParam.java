package com.lxzl.erp.common.domain.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;

import java.util.Date;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author kai
 * @date 2018-01-30 14:18
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerAccountLogParam extends BasePageParam {

    private String businessCustomerNo;
    private Integer customerAccountLogType;
    private Date queryStartTime;
    private Date queryEndTime;

    private String businessAppId;
    private String businessAppSecret;

    public String getBusinessCustomerNo() { return businessCustomerNo; }

    public void setBusinessCustomerNo(String businessCustomerNo) { this.businessCustomerNo = businessCustomerNo; }

    public Integer getCustomerAccountLogType() { return customerAccountLogType; }

    public void setCustomerAccountLogType(Integer customerAccountLogType) { this.customerAccountLogType = customerAccountLogType; }

    public Date getQueryStartTime() { return queryStartTime; }

    public void setQueryStartTime(Date queryStartTime) { this.queryStartTime = queryStartTime; }

    public Date getQueryEndTime() { return queryEndTime; }

    public void setQueryEndTime(Date queryEndTime) { this.queryEndTime = queryEndTime; }

    public String getBusinessAppId() { return businessAppId; }

    public void setBusinessAppId(String businessAppId) { this.businessAppId = businessAppId; }

    public String getBusinessAppSecret() { return businessAppSecret; }

    public void setBusinessAppSecret(String businessAppSecret) { this.businessAppSecret = businessAppSecret; }
}
