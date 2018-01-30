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
    private Integer accountLogType;
    private Date startTime;
    private Date endTime;

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

    public Integer getAccountLogType() {
        return accountLogType;
    }

    public void setAccountLogType(Integer accountLogType) {
        this.accountLogType = accountLogType;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

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
