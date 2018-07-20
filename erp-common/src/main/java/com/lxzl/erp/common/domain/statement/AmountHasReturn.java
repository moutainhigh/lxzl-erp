package com.lxzl.erp.common.domain.statement;

import java.math.BigDecimal;

/**
 * @author: huanglong
 * @date: 2018/7/18/018 16:38
 * @e_mail: huanglong5945@163.com
 * @Description:
 */
public class AmountHasReturn {
    private BigDecimal returnStatementAmount = BigDecimal.ZERO;
    private BigDecimal returnStatementRentAmount = BigDecimal.ZERO;
    private BigDecimal returnStatementDepositAmount = BigDecimal.ZERO;
    private BigDecimal returnStatementRentDepositAmount = BigDecimal.ZERO;

    public AmountHasReturn(BigDecimal returnStatementAmount, BigDecimal returnStatementRentAmount, BigDecimal returnStatementDepositAmount, BigDecimal returnStatementRentDepositAmount) {
        this.returnStatementAmount = returnStatementAmount;
        this.returnStatementRentAmount = returnStatementRentAmount;
        this.returnStatementDepositAmount = returnStatementDepositAmount;
        this.returnStatementRentDepositAmount = returnStatementRentDepositAmount;
    }

    public BigDecimal getReturnStatementAmount() {
        return returnStatementAmount;
    }

    public void setReturnStatementAmount(BigDecimal returnStatementAmount) {
        this.returnStatementAmount = returnStatementAmount;
    }

    public BigDecimal getReturnStatementRentAmount() {
        return returnStatementRentAmount;
    }

    public void setReturnStatementRentAmount(BigDecimal returnStatementRentAmount) {
        this.returnStatementRentAmount = returnStatementRentAmount;
    }

    public BigDecimal getReturnStatementDepositAmount() {
        return returnStatementDepositAmount;
    }

    public void setReturnStatementDepositAmount(BigDecimal returnStatementDepositAmount) {
        this.returnStatementDepositAmount = returnStatementDepositAmount;
    }

    public BigDecimal getReturnStatementRentDepositAmount() {
        return returnStatementRentDepositAmount;
    }

    public void setReturnStatementRentDepositAmount(BigDecimal returnStatementRentDepositAmount) {
        this.returnStatementRentDepositAmount = returnStatementRentDepositAmount;
    }
}
