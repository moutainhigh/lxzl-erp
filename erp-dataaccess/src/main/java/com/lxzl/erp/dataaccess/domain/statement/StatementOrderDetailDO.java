package com.lxzl.erp.dataaccess.domain.statement;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;

import java.util.Date;
import java.math.BigDecimal;

public class StatementOrderDetailDO extends BaseDO {

    private Integer id;
    private Integer statementOrderId;
    private Integer customerId;
    private Integer orderType;
    private Integer orderId;
    private Integer orderItemType;
    private Integer orderItemReferId;
    private Date statementExpectPayTime;
    private BigDecimal statementDetailAmount;
    private BigDecimal statementDetailRentDepositAmount;
    private BigDecimal statementDetailRentDepositPaidAmount;
    private BigDecimal statementDetailRentDepositReturnAmount;
    private BigDecimal statementDetailDepositAmount;
    private BigDecimal statementDetailDepositPaidAmount;
    private BigDecimal statementDetailDepositReturnAmount;
    private BigDecimal statementDetailRentAmount;
    private BigDecimal statementDetailRentPaidAmount;
    private Date statementDetailPaidTime;
    private BigDecimal statementDetailOverdueAmount;
    private Integer statementDetailStatus;
    private Date statementStartTime;
    private Date statementEndTime;
    private Integer dataStatus;
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatementOrderId() {
        return statementOrderId;
    }

    public void setStatementOrderId(Integer statementOrderId) {
        this.statementOrderId = statementOrderId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getStatementDetailAmount() {
        return statementDetailAmount;
    }

    public void setStatementDetailAmount(BigDecimal statementDetailAmount) {
        this.statementDetailAmount = statementDetailAmount;
    }

    public Integer getStatementDetailStatus() {
        return statementDetailStatus;
    }

    public void setStatementDetailStatus(Integer statementDetailStatus) {
        this.statementDetailStatus = statementDetailStatus;
    }

    public Date getStatementStartTime() {
        return statementStartTime;
    }

    public void setStatementStartTime(Date statementStartTime) {
        this.statementStartTime = statementStartTime;
    }

    public Date getStatementEndTime() {
        return statementEndTime;
    }

    public void setStatementEndTime(Date statementEndTime) {
        this.statementEndTime = statementEndTime;
    }

    public Integer getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BigDecimal getStatementDetailOverdueAmount() {
        return statementDetailOverdueAmount;
    }

    public void setStatementDetailOverdueAmount(BigDecimal statementDetailOverdueAmount) {
        this.statementDetailOverdueAmount = statementDetailOverdueAmount;
    }

    public Date getStatementExpectPayTime() {
        return statementExpectPayTime;
    }

    public void setStatementExpectPayTime(Date statementExpectPayTime) {
        this.statementExpectPayTime = statementExpectPayTime;
    }

    public BigDecimal getStatementDetailRentDepositAmount() {
        return statementDetailRentDepositAmount;
    }

    public void setStatementDetailRentDepositAmount(BigDecimal statementDetailRentDepositAmount) {
        this.statementDetailRentDepositAmount = statementDetailRentDepositAmount;
    }

    public BigDecimal getStatementDetailRentDepositPaidAmount() {
        return statementDetailRentDepositPaidAmount;
    }

    public void setStatementDetailRentDepositPaidAmount(BigDecimal statementDetailRentDepositPaidAmount) {
        this.statementDetailRentDepositPaidAmount = statementDetailRentDepositPaidAmount;
    }

    public BigDecimal getStatementDetailRentDepositReturnAmount() {
        return statementDetailRentDepositReturnAmount;
    }

    public void setStatementDetailRentDepositReturnAmount(BigDecimal statementDetailRentDepositReturnAmount) {
        this.statementDetailRentDepositReturnAmount = statementDetailRentDepositReturnAmount;
    }

    public BigDecimal getStatementDetailRentAmount() {
        return statementDetailRentAmount;
    }

    public void setStatementDetailRentAmount(BigDecimal statementDetailRentAmount) {
        this.statementDetailRentAmount = statementDetailRentAmount;
    }

    public BigDecimal getStatementDetailRentPaidAmount() {
        return statementDetailRentPaidAmount;
    }

    public void setStatementDetailRentPaidAmount(BigDecimal statementDetailRentPaidAmount) {
        this.statementDetailRentPaidAmount = statementDetailRentPaidAmount;
    }

    public Integer getOrderItemType() {
        return orderItemType;
    }

    public void setOrderItemType(Integer orderItemType) {
        this.orderItemType = orderItemType;
    }

    public Integer getOrderItemReferId() {
        return orderItemReferId;
    }

    public void setOrderItemReferId(Integer orderItemReferId) {
        this.orderItemReferId = orderItemReferId;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Date getStatementDetailPaidTime() {
        return statementDetailPaidTime;
    }

    public void setStatementDetailPaidTime(Date statementDetailPaidTime) {
        this.statementDetailPaidTime = statementDetailPaidTime;
    }

    public BigDecimal getStatementDetailDepositAmount() {
        return statementDetailDepositAmount;
    }

    public void setStatementDetailDepositAmount(BigDecimal statementDetailDepositAmount) {
        this.statementDetailDepositAmount = statementDetailDepositAmount;
    }

    public BigDecimal getStatementDetailDepositPaidAmount() {
        return statementDetailDepositPaidAmount;
    }

    public void setStatementDetailDepositPaidAmount(BigDecimal statementDetailDepositPaidAmount) {
        this.statementDetailDepositPaidAmount = statementDetailDepositPaidAmount;
    }

    public BigDecimal getStatementDetailDepositReturnAmount() {
        return statementDetailDepositReturnAmount;
    }

    public void setStatementDetailDepositReturnAmount(BigDecimal statementDetailDepositReturnAmount) {
        this.statementDetailDepositReturnAmount = statementDetailDepositReturnAmount;
    }
}