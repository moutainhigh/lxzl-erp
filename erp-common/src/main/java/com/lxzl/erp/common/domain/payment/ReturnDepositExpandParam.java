package com.lxzl.erp.common.domain.payment;

import java.math.BigDecimal;

/**
 * @author lk
 * @Description: TODO
 * @date 2018/5/10 14:11
 */
public class ReturnDepositExpandParam {

    private String businessAppId	;//业务系统appId
    private String businessAppSecret	;//业务系统秘钥
    private String businessCustomerNo	;//业务系统客户编号
    private BigDecimal businessReturnRentAmount	;//业务系统退款金额—-租金金额 (必须大于0)
    private BigDecimal businessReturnOtherAmount	;//业务系统退款金额—-其他金额（包括但不限于运费、服务费等）(必须大于0)
    private BigDecimal businessReturnRentDepositAmount	;//退款金额—-设备押金金额(必须大于0)
    private BigDecimal businessReturnDepositAmount	;//退款金额—-租金押金金额(必须大于0)

    private String businessOperateUser;//操作人ID
    private String businessCustomerName;//操作人姓名
    private String remark	;//退款备注

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

    public String getBusinessCustomerNo() {
        return businessCustomerNo;
    }

    public void setBusinessCustomerNo(String businessCustomerNo) {
        this.businessCustomerNo = businessCustomerNo;
    }

    public BigDecimal getBusinessReturnRentAmount() {
        return businessReturnRentAmount;
    }

    public void setBusinessReturnRentAmount(BigDecimal businessReturnRentAmount) {
        this.businessReturnRentAmount = businessReturnRentAmount;
    }

    public BigDecimal getBusinessReturnOtherAmount() {
        return businessReturnOtherAmount;
    }

    public void setBusinessReturnOtherAmount(BigDecimal businessReturnOtherAmount) {
        this.businessReturnOtherAmount = businessReturnOtherAmount;
    }

    public BigDecimal getBusinessReturnRentDepositAmount() {
        return businessReturnRentDepositAmount;
    }

    public void setBusinessReturnRentDepositAmount(BigDecimal businessReturnRentDepositAmount) {
        this.businessReturnRentDepositAmount = businessReturnRentDepositAmount;
    }

    public BigDecimal getBusinessReturnDepositAmount() {
        return businessReturnDepositAmount;
    }

    public void setBusinessReturnDepositAmount(BigDecimal businessReturnDepositAmount) {
        this.businessReturnDepositAmount = businessReturnDepositAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
}
