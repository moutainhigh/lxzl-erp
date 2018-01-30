package com.lxzl.erp.common.domain.erpInterface.statementOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePageParam;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/1/29
 * @Time : Created in 19:26
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class InterfaceStatementOrderQueryParam  extends BasePageParam {
    private String statementOrderCustomerName;
    private Integer statementOrderCustomerId;
    private String statementOrderCustomerNo;
    private String statementOrderNo;
    private Integer statementOrderStatus;
    private Date statementExpectPayStartTime;
    private Date statementExpectPayEndTime;
    private Integer isNeedToPay;
    private String orderNo;
    private String returnOrderNo;
    private String changeOrderNo;
    private Date createStartTime;
    private Date createEndTime;
    @NotNull(message = ErrorCode.BUSINESS_APP_ID_NOT_NULL)
    private String erpAppId;   //业务系统APP ID由ERP系统生成，提供给业务系统
    @NotNull(message = ErrorCode.BUSINESS_APP_SECRET_NOT_NULL)
    private String erpAppSecret;   //业务系统app secret由ERP系统生成，提供给业务系统
    private String erpOperateUser;   //系统名称

    public String getStatementOrderCustomerName() {
        return statementOrderCustomerName;
    }

    public void setStatementOrderCustomerName(String statementOrderCustomerName) {
        this.statementOrderCustomerName = statementOrderCustomerName;
    }

    public Integer getStatementOrderCustomerId() {
        return statementOrderCustomerId;
    }

    public void setStatementOrderCustomerId(Integer statementOrderCustomerId) {
        this.statementOrderCustomerId = statementOrderCustomerId;
    }

    public String getStatementOrderCustomerNo() {
        return statementOrderCustomerNo;
    }

    public void setStatementOrderCustomerNo(String statementOrderCustomerNo) {
        this.statementOrderCustomerNo = statementOrderCustomerNo;
    }

    public String getStatementOrderNo() {
        return statementOrderNo;
    }

    public void setStatementOrderNo(String statementOrderNo) {
        this.statementOrderNo = statementOrderNo;
    }

    public Integer getStatementOrderStatus() {
        return statementOrderStatus;
    }

    public void setStatementOrderStatus(Integer statementOrderStatus) {
        this.statementOrderStatus = statementOrderStatus;
    }

    public Date getStatementExpectPayStartTime() {
        return statementExpectPayStartTime;
    }

    public void setStatementExpectPayStartTime(Date statementExpectPayStartTime) {
        this.statementExpectPayStartTime = statementExpectPayStartTime;
    }

    public Date getStatementExpectPayEndTime() {
        return statementExpectPayEndTime;
    }

    public void setStatementExpectPayEndTime(Date statementExpectPayEndTime) {
        this.statementExpectPayEndTime = statementExpectPayEndTime;
    }

    public Integer getIsNeedToPay() {
        return isNeedToPay;
    }

    public void setIsNeedToPay(Integer isNeedToPay) {
        this.isNeedToPay = isNeedToPay;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getReturnOrderNo() {
        return returnOrderNo;
    }

    public void setReturnOrderNo(String returnOrderNo) {
        this.returnOrderNo = returnOrderNo;
    }

    public String getChangeOrderNo() {
        return changeOrderNo;
    }

    public void setChangeOrderNo(String changeOrderNo) {
        this.changeOrderNo = changeOrderNo;
    }

    public Date getCreateStartTime() {
        return createStartTime;
    }

    public void setCreateStartTime(Date createStartTime) {
        this.createStartTime = createStartTime;
    }

    public Date getCreateEndTime() {
        return createEndTime;
    }

    public void setCreateEndTime(Date createEndTime) {
        this.createEndTime = createEndTime;
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
