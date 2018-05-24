package com.lxzl.erp.common.domain.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePageParam;
import com.lxzl.erp.common.domain.validGroup.QueryGroup;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 21:12 2018/4/9
 * @Modified By:
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChargeRecordPageParam  extends BasePageParam {
    private String businessCustomerNo;
    private Date queryStartTime;
    private Date queryEndTime;
    private Integer chargeType;
    private Integer chargeStatus;

    private String businessAppId;
    private String businessAppSecret;
    private String businessOperateUser;

    private Integer subCompanyId;
    private String customerName;
    @NotNull(message = ErrorCode.IS_DAMAGE_NOT_NULL, groups = {QueryGroup.class})
    private String chargeOrderNo;  //支付系统充值订单号
    private String channelNo;  //充值渠道编号 1:快付通 其他待拓展

    public String getChannelNo() {
        return channelNo;
    }

    public void setChannelNo(String channelNo) {
        this.channelNo = channelNo;
    }

    public String getChargeOrderNo() {
        return chargeOrderNo;
    }

    public void setChargeOrderNo(String chargeOrderNo) {
        this.chargeOrderNo = chargeOrderNo;
    }

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

    public Integer getSubCompanyId() {
        return subCompanyId;
    }

    public void setSubCompanyId(Integer subCompanyId) {
        this.subCompanyId = subCompanyId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
