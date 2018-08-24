package com.lxzl.erp.common.domain.payment;

import java.math.BigDecimal;

/**
 * @author: huanglong
 * @date: 2018/8/8/008 14:44
 * @e_mail: huanglong5945@163.com
 * @Description:
 */
public class ReturnOtherAndPayDepositParam {

    private String businessAppId	;//业务系统appId
    private String businessAppSecret	;//业务系统秘钥
    private String businessOperateUser;//操作人ID
    private String businessCustomerName;//操作人姓名

    private String businessCustomerNo;
    /**退其他费用*/
    private BigDecimal returnOtherAmount;
    /**需支付租金押金*/
    private BigDecimal payRentDepositAmount;
    /**需支付押金*/
    private BigDecimal payDepositAmount;
    /**备注*/
    private String remark;

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

    public String getBusinessCustomerName() {
        return businessCustomerName;
    }

    public void setBusinessCustomerName(String businessCustomerName) {
        this.businessCustomerName = businessCustomerName;
    }

    public String getBusinessCustomerNo() {
        return businessCustomerNo;
    }

    public void setBusinessCustomerNo(String businessCustomerNo) {
        this.businessCustomerNo = businessCustomerNo;
    }

    public BigDecimal getReturnOtherAmount() {
        return returnOtherAmount;
    }

    public void setReturnOtherAmount(BigDecimal returnOtherAmount) {
        this.returnOtherAmount = returnOtherAmount;
    }

    public BigDecimal getPayRentDepositAmount() {
        return payRentDepositAmount;
    }

    public void setPayRentDepositAmount(BigDecimal payRentDepositAmount) {
        this.payRentDepositAmount = payRentDepositAmount;
    }

    public BigDecimal getPayDepositAmount() {
        return payDepositAmount;
    }

    public void setPayDepositAmount(BigDecimal payDepositAmount) {
        this.payDepositAmount = payDepositAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
