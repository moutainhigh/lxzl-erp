package com.lxzl.erp.common.domain.statement.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.util.BigDecimalUtil;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StatementOrder extends BasePO {

    private Integer statementOrderId;   //唯一标识
    private String statementOrderNo;   //结算单编码
    private Integer customerId;   //客户ID
    private String customerName;    // 客户姓名
    private Date statementExpectPayTime; // 结算单预计支付时间
    private BigDecimal statementAmount;   //结算单金额，结算单明细总和
    private BigDecimal statementOtherAmount;   // 结算单其他金额
    private BigDecimal statementOtherPaidAmount;    // 结算单其他已支付金额
    private BigDecimal statementPaidAmount;   //结算单已付金额
    private BigDecimal statementRentDepositAmount;      // 结算租金押金金额
    private BigDecimal statementRentDepositPaidAmount;  // 已付租金押金金额
    private BigDecimal statementRentDepositReturnAmount;    // 退还租金押金金额
    private BigDecimal statementDepositAmount;   //结算押金金额
    private BigDecimal statementDepositPaidAmount;   //已付押金金额
    private BigDecimal statementDepositReturnAmount;   //退还押金金额
    private BigDecimal statementRentAmount;             // 结算单租金金额
    private BigDecimal statementRentPaidAmount;         // 租金已付金额
    private Date statementPaidTime;        // 结算单支付时间
    private BigDecimal statementOverdueAmount;    // 逾期金额
    private Integer statementStatus;   //结算状态，0未结算，1已结算
    private Date statementStartTime;   //结算开始时间，结算单明细最早的一个
    private Date statementEndTime;   //结算结束时间，结算单明细最晚的一个
    private BigDecimal statementCorrectAmount;  //结算单冲正金额
    private Integer dataStatus;   //状态：0不可用；1可用；2删除
    private String remark;   //备注
    private Date createTime;   //添加时间
    private String createUser;   //添加人
    private Date updateTime;   //添加时间
    private String updateUser;   //修改人

    private List<StatementOrderDetail> statementOrderDetailList;


    public Integer getStatementOrderId() {
        return statementOrderId;
    }

    public void setStatementOrderId(Integer statementOrderId) {
        this.statementOrderId = statementOrderId;
    }

    public String getStatementOrderNo() {
        return statementOrderNo;
    }

    public void setStatementOrderNo(String statementOrderNo) {
        this.statementOrderNo = statementOrderNo;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getStatementAmount() {
        return statementAmount;
    }

    public void setStatementAmount(BigDecimal statementAmount) {
        this.statementAmount = statementAmount;
    }

    public Integer getStatementStatus() {
        return statementStatus;
    }

    public void setStatementStatus(Integer statementStatus) {
        this.statementStatus = statementStatus;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public List<StatementOrderDetail> getStatementOrderDetailList() {
        return statementOrderDetailList;
    }

    public void setStatementOrderDetailList(List<StatementOrderDetail> statementOrderDetailList) {
        this.statementOrderDetailList = statementOrderDetailList;
    }

    public Date getStatementExpectPayTime() {
        return statementExpectPayTime;
    }

    public void setStatementExpectPayTime(Date statementExpectPayTime) {
        this.statementExpectPayTime = statementExpectPayTime;
    }

    public BigDecimal getStatementRentDepositAmount() {
        return statementRentDepositAmount;
    }

    public void setStatementRentDepositAmount(BigDecimal statementRentDepositAmount) {
        this.statementRentDepositAmount = statementRentDepositAmount;
    }

    public BigDecimal getStatementRentDepositPaidAmount() {
        return statementRentDepositPaidAmount;
    }

    public void setStatementRentDepositPaidAmount(BigDecimal statementRentDepositPaidAmount) {
        this.statementRentDepositPaidAmount = statementRentDepositPaidAmount;
    }

    public BigDecimal getStatementRentDepositReturnAmount() {
        return statementRentDepositReturnAmount;
    }

    public void setStatementRentDepositReturnAmount(BigDecimal statementRentDepositReturnAmount) {
        this.statementRentDepositReturnAmount = statementRentDepositReturnAmount;
    }

    public BigDecimal getStatementRentAmount() {
        return statementRentAmount;
    }

    public void setStatementRentAmount(BigDecimal statementRentAmount) {
        this.statementRentAmount = statementRentAmount;
    }

    public BigDecimal getStatementRentPaidAmount() {
        return statementRentPaidAmount;
    }

    public void setStatementRentPaidAmount(BigDecimal statementRentPaidAmount) {
        this.statementRentPaidAmount = statementRentPaidAmount;
    }

    public BigDecimal getStatementOverdueAmount() {
        return statementOverdueAmount;
    }

    public void setStatementOverdueAmount(BigDecimal statementOverdueAmount) {
        this.statementOverdueAmount = statementOverdueAmount;
    }

    public Date getStatementPaidTime() {
        return statementPaidTime;
    }

    public void setStatementPaidTime(Date statementPaidTime) {
        this.statementPaidTime = statementPaidTime;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public BigDecimal getStatementDepositAmount() {
        return statementDepositAmount;
    }

    public void setStatementDepositAmount(BigDecimal statementDepositAmount) {
        this.statementDepositAmount = statementDepositAmount;
    }

    public BigDecimal getStatementDepositPaidAmount() {
        return statementDepositPaidAmount;
    }

    public void setStatementDepositPaidAmount(BigDecimal statementDepositPaidAmount) {
        this.statementDepositPaidAmount = statementDepositPaidAmount;
    }

    public BigDecimal getStatementDepositReturnAmount() {
        return statementDepositReturnAmount;
    }

    public void setStatementDepositReturnAmount(BigDecimal statementDepositReturnAmount) {
        this.statementDepositReturnAmount = statementDepositReturnAmount;
    }

    public BigDecimal getStatementPaidAmount() {
        return statementPaidAmount == null || BigDecimal.ZERO.equals(statementPaidAmount) ? BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(statementRentPaidAmount, statementDepositPaidAmount), statementRentDepositPaidAmount),statementOtherPaidAmount) : statementPaidAmount;
    }

    public void setStatementPaidAmount(BigDecimal statementPaidAmount) {
        this.statementPaidAmount = statementPaidAmount;
    }

    public BigDecimal getStatementOtherAmount() {
        return statementOtherAmount;
    }

    public void setStatementOtherAmount(BigDecimal statementOtherAmount) {
        this.statementOtherAmount = statementOtherAmount;
    }

    public BigDecimal getStatementOtherPaidAmount() {
        return statementOtherPaidAmount;
    }

    public void setStatementOtherPaidAmount(BigDecimal statementOtherPaidAmount) {
        this.statementOtherPaidAmount = statementOtherPaidAmount;
    }

    public BigDecimal getStatementCorrectAmount() {
        return statementCorrectAmount;
    }

    public void setStatementCorrectAmount(BigDecimal statementCorrectAmount) {
        this.statementCorrectAmount = statementCorrectAmount;
    }
}