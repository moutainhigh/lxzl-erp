package com.lxzl.erp.dataaccess.domain.statement;

import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

public class StatementOrderDO extends BaseDO {

    private Integer id;
    private String statementOrderNo;
    private Integer customerId;
    private Date statementExpectPayTime;
    private BigDecimal statementAmount;
    private BigDecimal statementPaidAmount;   //结算单已付金额
    private BigDecimal statementOtherAmount;   // 结算单其他金额
    private BigDecimal statementOtherPaidAmount;    // 结算单其他已支付金额
    private BigDecimal statementRentDepositAmount;
    private BigDecimal statementRentDepositPaidAmount;
    private BigDecimal statementRentDepositReturnAmount;
    private BigDecimal statementDepositAmount;   //结算押金金额
    private BigDecimal statementDepositPaidAmount;   //已付押金金额
    private BigDecimal statementDepositReturnAmount;   //退还押金金额
    private BigDecimal statementRentAmount;
    private BigDecimal statementRentPaidAmount;
    private Date statementPaidTime;
    private BigDecimal statementOverdueAmount;
    private BigDecimal statementOverduePaidAmount;
    private Integer statementStatus;
    private Date statementStartTime;
    private Date statementEndTime;
    private BigDecimal statementCorrectAmount;  //结算单冲正金额
    private Integer dataStatus;
    private String remark;
    private List<StatementOrderDetailDO> statementOrderDetailDOList;

    @Transient
    private String customerName;

    @Transient
    private String customerNo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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


    public BigDecimal getStatementPaidAmount() {
        return statementPaidAmount == null || BigDecimal.ZERO.equals(statementPaidAmount) ? BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(statementRentPaidAmount, statementDepositPaidAmount), statementRentDepositPaidAmount),statementOtherPaidAmount) : statementPaidAmount;
    }

    public void setStatementPaidAmount(BigDecimal statementPaidAmount) {
        this.statementPaidAmount = statementPaidAmount;
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

    public List<StatementOrderDetailDO> getStatementOrderDetailDOList() {
        return statementOrderDetailDOList;
    }

    public void setStatementOrderDetailDOList(List<StatementOrderDetailDO> statementOrderDetailDOList) {
        this.statementOrderDetailDOList = statementOrderDetailDOList;
    }

    public BigDecimal getStatementOverdueAmount() {
        return statementOverdueAmount;
    }

    public void setStatementOverdueAmount(BigDecimal statementOverdueAmount) {
        this.statementOverdueAmount = statementOverdueAmount;
    }

    public Date getStatementExpectPayTime() {
        return statementExpectPayTime;
    }

    public void setStatementExpectPayTime(Date statementExpectPayTime) {
        this.statementExpectPayTime = statementExpectPayTime;
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

    public BigDecimal getStatementRentDepositAmount() {
        return statementRentDepositAmount;
    }

    public void setStatementRentDepositAmount(BigDecimal statementRentDepositAmount) {
        this.statementRentDepositAmount = statementRentDepositAmount;
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

    public BigDecimal getStatementOverduePaidAmount() {
        return statementOverduePaidAmount;
    }

    public void setStatementOverduePaidAmount(BigDecimal statementOverduePaidAmount) {
        this.statementOverduePaidAmount = statementOverduePaidAmount;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }
}