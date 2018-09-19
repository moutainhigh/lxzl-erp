package com.lxzl.erp.common.domain.statement;

import com.lxzl.erp.common.util.BigDecimalUtil;

import java.math.BigDecimal;

/**
 * @author: huanglong
 * @since: 2018/9/13/013
 */
public class AmountNeedPay {
    //已付设备押金
    BigDecimal depositAmount = BigDecimal.ZERO;
    //已付其他费用
    BigDecimal otherAmount = BigDecimal.ZERO;
    // 已付租金
    BigDecimal rentAmount = BigDecimal.ZERO;
    //已付逾期费用
    BigDecimal overdueAmount = BigDecimal.ZERO;
    //已付违约金
    BigDecimal penaltyAmount = BigDecimal.ZERO;
    //已付租金押金
    BigDecimal rentDepositAmount = BigDecimal.ZERO;

    public AmountNeedPay(BigDecimal depositAmount, BigDecimal otherAmount, BigDecimal rentAmount, BigDecimal overdueAmount, BigDecimal penaltyAmount, BigDecimal rentDepositAmount) {
        this.depositAmount = depositAmount;
        this.otherAmount = otherAmount;
        this.rentAmount = rentAmount;
        this.overdueAmount = overdueAmount;
        this.penaltyAmount = penaltyAmount;
        this.rentDepositAmount = rentDepositAmount;
    }

    public BigDecimal getTotalAmount(){
        return BigDecimalUtil.add(depositAmount,otherAmount,rentAmount,overdueAmount,penaltyAmount,rentDepositAmount);
    }
    public BigDecimal getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }

    public BigDecimal getOtherAmount() {
        return otherAmount;
    }

    public void setOtherAmount(BigDecimal otherAmount) {
        this.otherAmount = otherAmount;
    }

    public BigDecimal getRentAmount() {
        return rentAmount;
    }

    public void setRentAmount(BigDecimal rentAmount) {
        this.rentAmount = rentAmount;
    }

    public BigDecimal getOverdueAmount() {
        return overdueAmount;
    }

    public void setOverdueAmount(BigDecimal overdueAmount) {
        this.overdueAmount = overdueAmount;
    }

    public BigDecimal getPenaltyAmount() {
        return penaltyAmount;
    }

    public void setPenaltyAmount(BigDecimal penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

    public BigDecimal getRentDepositAmount() {
        return rentDepositAmount;
    }

    public void setRentDepositAmount(BigDecimal rentDepositAmount) {
        this.rentDepositAmount = rentDepositAmount;
    }
}
