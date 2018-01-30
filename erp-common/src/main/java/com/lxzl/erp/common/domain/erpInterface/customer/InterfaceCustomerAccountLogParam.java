package com.lxzl.erp.common.domain.erpInterface.customer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePageParam;
import com.lxzl.erp.common.domain.validGroup.customer.QueryCustomerNoGroup;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author : kai
 * @Date : Created in 2018/1/30
 * @Time : Created in 18:21
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class InterfaceCustomerAccountLogParam extends BasePageParam {

    @NotNull(message = ErrorCode.CUSTOMER_NO_NOT_NULL,groups = {QueryCustomerNoGroup.class})
    private String CustomerNo;
    private Integer customerAccountLogType;
    private Date queryStartTime;
    private Date queryEndTime;

    @NotNull(message = ErrorCode.BUSINESS_APP_ID_NOT_NULL)
    private String erpAppId;   //业务系统APP ID由ERP系统生成，提供给业务系统
    @NotNull(message = ErrorCode.BUSINESS_APP_SECRET_NOT_NULL)
    private String erpAppSecret;   //业务系统app secret由ERP系统生成，提供给业务系统
    private String erpOperateUser;   //系统名称

    public String getCustomerNo() {
        return CustomerNo;
    }

    public void setCustomerNo(String customerNo) {
        CustomerNo = customerNo;
    }

    public Integer getCustomerAccountLogType() { return customerAccountLogType; }

    public void setCustomerAccountLogType(Integer customerAccountLogType) { this.customerAccountLogType = customerAccountLogType; }

    public Date getQueryStartTime() { return queryStartTime; }

    public void setQueryStartTime(Date queryStartTime) { this.queryStartTime = queryStartTime; }

    public Date getQueryEndTime() { return queryEndTime; }

    public void setQueryEndTime(Date queryEndTime) { this.queryEndTime = queryEndTime; }

    public String getErpAppId() {
        return erpAppId;
    }

    public void setErpAppId(String erpAppId) {
        this.erpAppId = erpAppId;
    }

    public String getErpAppSecret() {
        return erpAppSecret;
    }

    public void setErpAppSecret(String erpAppSecret) {
        this.erpAppSecret = erpAppSecret;
    }

    public String getErpOperateUser() {
        return erpOperateUser;
    }

    public void setErpOperateUser(String erpOperateUser) {
        this.erpOperateUser = erpOperateUser;
    }
}
