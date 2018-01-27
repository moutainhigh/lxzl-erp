package com.lxzl.erp.common.domain.statement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;

import java.io.Serializable;
import java.util.Date;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-12 8:51
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatementOrderQueryParam extends BasePageParam {
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

    public String getStatementOrderCustomerName() {
        return statementOrderCustomerName;
    }

    public void setStatementOrderCustomerName(String statementOrderCustomerName) {
        this.statementOrderCustomerName = statementOrderCustomerName;
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

    public Integer getIsNeedToPay() {
        return isNeedToPay;
    }

    public void setIsNeedToPay(Integer isNeedToPay) {
        this.isNeedToPay = isNeedToPay;
    }
}
