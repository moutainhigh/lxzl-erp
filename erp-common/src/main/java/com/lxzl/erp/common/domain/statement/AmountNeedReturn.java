package com.lxzl.erp.common.domain.statement;

import com.lxzl.erp.common.util.BigDecimalUtil;

import java.math.BigDecimal;

/**
 * @author: huanglong
 * @date: 2018/6/15/015 11:29
 * @e_mail: huanglong5945@163.com
 * @Description:
 */
public class AmountNeedReturn {
    //已付设备押金
    BigDecimal depositPaidAmount = BigDecimal.ZERO;
    //已付其他费用
    BigDecimal otherPaidAmount = BigDecimal.ZERO;
    // 已付租金
    BigDecimal rentPaidAmount = BigDecimal.ZERO;
    //已付逾期费用
    BigDecimal overduePaidAmount = BigDecimal.ZERO;
    //已付违约金
    BigDecimal penaltyPaidAmount = BigDecimal.ZERO;
    //已付租金押金
    BigDecimal rentDepositPaidAmount = BigDecimal.ZERO;

    public AmountNeedReturn add(AmountNeedReturn amountNeedReturn){
        depositPaidAmount= BigDecimalUtil.add(depositPaidAmount,amountNeedReturn.getDepositPaidAmount());
        otherPaidAmount=BigDecimalUtil.add(otherPaidAmount,amountNeedReturn.getOtherPaidAmount());
        rentPaidAmount=BigDecimalUtil.add(rentPaidAmount,amountNeedReturn.getRentPaidAmount());
        overduePaidAmount=BigDecimalUtil.add(overduePaidAmount,amountNeedReturn.getOverduePaidAmount());
        penaltyPaidAmount=BigDecimalUtil.add(penaltyPaidAmount,amountNeedReturn.getPenaltyPaidAmount());
        rentDepositPaidAmount=BigDecimalUtil.add(rentDepositPaidAmount,amountNeedReturn.getRentDepositPaidAmount());
        return this;
    }

    public BigDecimal getTotalAmount(){
        return BigDecimalUtil.add(depositPaidAmount,otherPaidAmount,rentPaidAmount,overduePaidAmount,penaltyPaidAmount,rentDepositPaidAmount);
    }

    public BigDecimal getDepositPaidAmount() {
        return depositPaidAmount;
    }

    public void setDepositPaidAmount(BigDecimal depositPaidAmount) {
        this.depositPaidAmount = depositPaidAmount;
    }

    public BigDecimal getOtherPaidAmount() {
        return otherPaidAmount;
    }

    public void setOtherPaidAmount(BigDecimal otherPaidAmount) {
        this.otherPaidAmount = otherPaidAmount;
    }

    public BigDecimal getRentPaidAmount() {
        return rentPaidAmount;
    }

    public void setRentPaidAmount(BigDecimal rentPaidAmount) {
        this.rentPaidAmount = rentPaidAmount;
    }

    public BigDecimal getOverduePaidAmount() {
        return overduePaidAmount;
    }

    public void setOverduePaidAmount(BigDecimal overduePaidAmount) {
        this.overduePaidAmount = overduePaidAmount;
    }

    public BigDecimal getPenaltyPaidAmount() {
        return penaltyPaidAmount;
    }

    public void setPenaltyPaidAmount(BigDecimal penaltyPaidAmount) {
        this.penaltyPaidAmount = penaltyPaidAmount;
    }

    public BigDecimal getRentDepositPaidAmount() {
        return rentDepositPaidAmount;
    }

    public void setRentDepositPaidAmount(BigDecimal rentDepositPaidAmount) {
        this.rentDepositPaidAmount = rentDepositPaidAmount;
    }
}
